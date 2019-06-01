/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.listener;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public interface DataEnvelopeListenerChannel {
    String DATAENVELOPE_INPUT = "input-data-envelope";

    @Input(DATAENVELOPE_INPUT)
    SubscribableChannel newDataEnvelope();
}
