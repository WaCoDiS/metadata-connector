/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.listener;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import org.springframework.stereotype.Component;

/**
 * Handler interface for a new DataEnvelope creation event
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public interface DataEnvelopeHandler {

    public abstract void handleNewDataEnvelope(AbstractDataEnvelope dataEnvelope);
}
