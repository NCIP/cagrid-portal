/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.impromptu;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImpromptuQueryStorage {

    private final Log logger = LogFactory.getLog(getClass());
    
    private void l(Object o) {
        logger.info("--> " + o);
    }

    private void pm(String s, Map<?, ?> m) {
        l("-------------begin printing map "+s+"-------------");
        if (m==null) {
            l("map is null");
        } else {
            Iterator<?> i = m.keySet().iterator();
            while(i.hasNext()) {
                Object k = i.next();
                l(k + " => " + m.get(k));
            }
        }
        l("-------------  end printing map "+s+"-------------");
    }
    
    static public ImpromptuQueryStorage instance = new ImpromptuQueryStorage();
    
    private ImpromptuQueryStorage() {
        //just to make it private
    }

    private Queue<ImpromptuQuery> indexByAccessTime = new PriorityQueue<ImpromptuQuery>(100, new ImpromptuQueryAccessTimeComparator());
    private Map<String, ImpromptuQuery> indexInvertedByUuid = new HashMap<String, ImpromptuQuery>();
    private Map<ImpromptuQuery, UUID> submitted = new HashMap<ImpromptuQuery, UUID>();
    private Map<String, String> results = new HashMap<String, String>();
    private long lastSubmissionTime = 0;

    synchronized private ImpromptuQuery setWithIndexes(ImpromptuQuery q) {
        l("setWithIndexes");
        submitted.put(q, q.getUuid());
        indexInvertedByUuid.put(q.getUuid().toString(), q);
        indexByAccessTime.remove(q);
        indexByAccessTime.add(q);
        return q;
    }

    synchronized private void removeWithIndexes(ImpromptuQuery q) {
        l("removeWithIndexes");
        String storedUuid = submitted.get(q) + "";
        submitted.remove(q);
        results.remove(q.getUuid());
        indexByAccessTime.remove(q);
        indexInvertedByUuid.remove(storedUuid);
        indexInvertedByUuid.remove(q.getUuid()+"");
    }

    synchronized private ImpromptuQuery getOldestAccessedQuery() {
        l("getOldestAccessedQuery");
        return indexByAccessTime.peek();
    }

    synchronized private boolean exists(ImpromptuQuery q) {
        l("exists");
        return submitted.containsKey(q);
    }

    synchronized private boolean wasAnswered(ImpromptuQuery q) {
        l("wasAnswered");
        return ((q != null) && (q.getUuid() != null) && (results.containsKey(q.getUuid())));
    }

    //
    // public interface
    //

    synchronized public ImpromptuQuery submit(ImpromptuQuery q, int maxQueries, long minInterval) throws ImpromptuQueryStorageFullException, ImpromptuQueryStorageSubmissiosTooCloseException {
        l("submit");
        if (q.isClearPrevious()) {
            removeWithIndexes(q);
        }

        if (!exists(q)) {

            if ((new Date().getTime()  - lastSubmissionTime) < minInterval) {
                throw new ImpromptuQueryStorageSubmissiosTooCloseException();
            }

            if (submitted.size() >= maxQueries) {
                if (wasAnswered(getOldestAccessedQuery())) {
                    removeWithIndexes(getOldestAccessedQuery());
                } else {
                    throw new ImpromptuQueryStorageFullException();
                }
            }

            lastSubmissionTime = new Date().getTime();
            q.setUuid(UUID.randomUUID());
            setWithIndexes(q);

            ImpromptuQueryRunner runner = new ImpromptuQueryRunner(q);
            Thread t = new Thread(runner);
            t.start();

        } else {
            q.setUuid(submitted.get(q));
        }

        return q;
    }

    synchronized private void setAccessTimeByUuid(String uuid) {
        l("setAccessTimeByUuid");
        ImpromptuQuery q = indexInvertedByUuid.get(uuid);
        l("setAccessTimeByUuid: indexInvertedByUuid.get(uuid)=" + q);
        if ((q != null) && (submitted.containsKey(q)) && (q.getUuid() != null)) {
            q.setAccessedOn(new Date().getTime());
            setWithIndexes(q);
        }
    }
    synchronized public String getResult(String uuidAsStr) {
        l("getResult, uuidAsStr=" + uuidAsStr);
        setAccessTimeByUuid(uuidAsStr);
        String result = results.get(uuidAsStr);
        l("getResult, result=" + result);
        return result;
    }

    synchronized public void setResult(ImpromptuQuery q, String res) {
        l("setResult, q.getUuid().toString()="+q.getUuid().toString());
        l("submitted.containsKey(q)="+submitted.containsKey(q));
        pm("results", results);
        String s = res == null ? "" : res;
        if ((submitted.containsKey(q)) && (q.getUuid() != null)) {
            results.put(q.getUuid().toString(), s);
        }
        pm("results", results);
        l("end of setResult");
    }

}
