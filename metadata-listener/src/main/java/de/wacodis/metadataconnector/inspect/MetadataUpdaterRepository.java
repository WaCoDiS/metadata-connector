/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.inspect;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link AbstractMetadataUpdater} that are specific to instances
 * of {@link AbstractDataEnvelope}
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class MetadataUpdaterRepository {

    @Autowired
    private List<AbstractMetadataUpdater> metadataUpdaterList;

    /**
     * Retrieves a {@link AbstractMetadataUpdater} depending on the
     * {@link AbstractDataEnvelope} type
     *
     * @param dateEnvelope the {@link AbstractDataEnvelope} to retrieve the
     * {@link AbstractMetadataUpdater} for
     * @return the {@link AbstractMetadataUpdater} corresponding to a
     * {@link AbstractDataEnvelope} type
     */
    public AbstractMetadataUpdater getMetadataUpdater(AbstractDataEnvelope dateEnvelope) {
        AbstractMetadataUpdater candidate = null;
        for (AbstractMetadataUpdater updater : metadataUpdaterList) {
            if (updater.supportsDataEnvelope(dateEnvelope)) {
                candidate = updater;
                break;
            }
        }
        return candidate;
    }
}
