/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.inspect;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public interface MetadataUpdater<T extends AbstractDataEnvelope> {
    
    public boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelop);
    
    public AbstractDataEnvelope updateDataEnvelope(T existingDataEnvelope, T newDataEnvelope);
}
