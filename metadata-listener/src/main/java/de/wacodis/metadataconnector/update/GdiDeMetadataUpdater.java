/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.GdiDeDataEnvelope;
import org.springframework.stereotype.Component;

/**
 * MetadataUpdater for {@link GdiDeDataEnvelope} instances
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class GdiDeMetadataUpdater extends AbstractMetadataUpdater<GdiDeDataEnvelope> {

    @Override
    public boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        return dataEnvelope instanceof GdiDeDataEnvelope;
    }

    @Override
    public AbstractDataEnvelope updateDataEnvelope(GdiDeDataEnvelope existingDataEnvelope, GdiDeDataEnvelope newDataEnvelope) {
        existingDataEnvelope.setAreaOfInterest(
                this.mergeAreaOfInterest(
                        existingDataEnvelope.getAreaOfInterest(),
                        newDataEnvelope.getAreaOfInterest()));

        existingDataEnvelope.setCreated(
                this.mergeCreated(
                        existingDataEnvelope.getCreated(),
                        newDataEnvelope.getCreated()));

        existingDataEnvelope.setModified(
                this.mergeCreated(
                        existingDataEnvelope.getModified(),
                        newDataEnvelope.getModified()));

        existingDataEnvelope.setTimeFrame(
                this.mergeTimeFrame(
                        existingDataEnvelope.getTimeFrame(),
                        newDataEnvelope.getTimeFrame()));

        return existingDataEnvelope;
    }

}
