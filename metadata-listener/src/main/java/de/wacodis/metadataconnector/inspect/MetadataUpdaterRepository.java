/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.inspect;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Repository for DataEnvelope specific MetadatUpdater
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class MetadataUpdaterRepository {

    @Autowired
    List<MetadataUpdater> metadataUpdaterList;

    /**
     * Retrieves a MetadataUpdater depending on the DataEnvelope type
     *
     * @param dateEnvelope the DataEnvelope to retrieve the MetadataUpdater for
     * @return the MetadataUpdater corresponding to a DataEnvelope type
     */
    public MetadataUpdater getMetadataUpdater(AbstractDataEnvelope dateEnvelope) {
        MetadataUpdater candidate = null;
        for (MetadataUpdater updater : metadataUpdaterList) {
            if (updater.supportsDataEnvelope(dateEnvelope)) {
                candidate = updater;
                break;
            }
        }
        return candidate;
    }
}
