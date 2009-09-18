/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.table;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "query_result_data")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_qr_data") })
public class QueryResultData extends AbstractDomainObject {
	
	private byte[] data;

	@Lob
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
