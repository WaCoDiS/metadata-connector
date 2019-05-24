/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.inspect;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.SensorWebDataEnvelope;
import org.springframework.stereotype.Component;

/**
 * MetadataUpdater for {@link SensorWebDataEnvelope} instances
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class SensorWebMetadataUpdater extends AbstractMetadataUpdater<SensorWebDataEnvelope> {

    @Override
    public boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        return dataEnvelope instanceof SensorWebDataEnvelope;
    }

    @Override
    public SensorWebDataEnvelope updateDataEnvelope(
            SensorWebDataEnvelope existingDataEnvelope,
            SensorWebDataEnvelope newDataEnvelope) {

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
