/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.inspect;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.SensorWebDataEnvelope;

/**
 * MetadataUpdater for SensorWebDataEnvelope instances
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class SensorWebMetadataUpdater implements MetadataUpdater<SensorWebDataEnvelope> {

    @Override
    public boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        return dataEnvelope instanceof SensorWebDataEnvelope;
    }

    @Override
    public SensorWebDataEnvelope updateDataEnvelope(SensorWebDataEnvelope existingDataEnvelope, SensorWebDataEnvelope newDataEnvelope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
