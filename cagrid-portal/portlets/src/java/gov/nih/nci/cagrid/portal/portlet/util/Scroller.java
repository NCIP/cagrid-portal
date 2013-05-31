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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class Scroller<T> {

	private int pageSize;

	private List<T> objects;

	private int index = 0;

	/**
	 * 
	 */
	public Scroller() {
		this(new ArrayList<T>(), 10);
	}

	public Scroller(List<T> objects, int pageSize) {

		assertObjects(objects);
		assertPageSize(pageSize);

		this.objects = objects;
		this.pageSize = pageSize;
	}

	private void assertPageSize(int pageSize) {
		if (pageSize < 1) {
			throw new IllegalArgumentException(
					"page size must be positive integer");
		}
	}

	private void assertObjects(List<T> objects) {
		if (objects == null) {
			throw new IllegalArgumentException("objects must not be null");
		}
	}

	public List<T> getPage() {
		List<T> page = new ArrayList<T>();
		synchronized (objects) {
			if (objects.size() > 0) {
				int endIndex = getEndIndex();
				for (int i = index; i < endIndex; i++) {
					page.add(objects.get(i));
				}
			}
		}
		return page;
	}

	public int getEndIndex() {
		synchronized (objects) {
			return Math.min(index + pageSize, objects.size());
		}
	}

	public int getIndex() {
		return index;
	}

	public void last() {
		while (isNextAvailable()) {
			next();
		}
	}

	public boolean isNextAvailable() {
		return index + pageSize < objects.size();
	}

	public void next() {
		if (isNextAvailable()) {
			index += pageSize;
		}
	}

	public void previous() {
		if (isPreviousAvailable()) {
			index -= pageSize;
		}
	}

	public boolean isPreviousAvailable() {
		return index - pageSize >= 0;
	}

	public void first() {
		while (isPreviousAvailable()) {
			previous();
		}
	}

	public List getObjects() {
		return objects;
	}

	public void setObjects(List<T> objects) {
		assertObjects(objects);
		this.objects = objects;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		assertPageSize(pageSize);
		this.pageSize = pageSize;
	}
	
	public void scroll(ScrollCommand command){
		if(ScrollOperation.FIRST.equals(command.getScrollOp())){
			first();
		}else if(ScrollOperation.PREVIOUS.equals(command.getScrollOp())){
			previous();
		}else if(ScrollOperation.NEXT.equals(command.getScrollOp())){
			next();
		}else if(ScrollOperation.LAST.equals(command.getScrollOp())){
			last();
		}
	}

}
