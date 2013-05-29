/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("tabular")

public class TabularOutputDescriptor extends OutputDescriptor {

    public List<OutputColumnDescriptor> columns = new ArrayList<OutputColumnDescriptor>();

    @OneToMany
    public List<OutputColumnDescriptor> getColumns() {
        return columns;
    }

    public void setColumns(List<OutputColumnDescriptor> columns) {
        this.columns = columns;
    }
}