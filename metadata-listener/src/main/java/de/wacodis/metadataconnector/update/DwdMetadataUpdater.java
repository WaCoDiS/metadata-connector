/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.DwdDataEnvelope;
import org.springframework.stereotype.Component;

/**
 * MetadataUpdater for {@link DwdDataEnvelope} instances
 * 
 * @author Lukas Butzmann
 */
public class DwdMetadataUpdater extends AbstractMetadataUpdater<DwdDataEnvelope>{

    @Override
    public boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        return dataEnvelope instanceof DwdDataEnvelope;
    }

    @Override
    public AbstractDataEnvelope updateDataEnvelope(DwdDataEnvelope existingDataEnvelope, DwdDataEnvelope newDataEnvelope) {
        existingDataEnvelope.setAreaOfInterest(
                this.mergeAreaOfInterest(
                        existingDataEnvelope.getAreaOfInterest(),
                        newDataEnvelope.getAreaOfInterest()));

        existingDataEnvelope.setCreated(
                this.mergeCreated(
                        existingDataEnvelope.getCreated(),
                        newDataEnvelope.getCreated()));

        existingDataEnvelope.setModified(
                this.mergeModified(
                        existingDataEnvelope.getModified(),
                        newDataEnvelope.getModified()));

        existingDataEnvelope.setTimeFrame(
                this.mergeTimeFrame(
                        existingDataEnvelope.getTimeFrame(),
                        newDataEnvelope.getTimeFrame()));

        return existingDataEnvelope;
    }
    
}
