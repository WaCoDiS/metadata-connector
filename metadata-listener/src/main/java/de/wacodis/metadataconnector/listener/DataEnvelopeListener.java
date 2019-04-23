package de.wacodis.metadataconnector.listener;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@EnableBinding(DataEnvelopeListenerChannel.class)
public class DataEnvelopeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataEnvelopeListener.class);

    @Autowired
    private List<DataEnvelopeHandler> dataEnvelopeHandlerList;

    @StreamListener(DataEnvelopeListenerChannel.DATAENVELOPE_INPUT)
    public void evaluateDateEnvelope(AbstractDataEnvelope dataEnvelope) {
        LOGGER.info("Received :" + System.lineSeparator() + dataEnvelope.toString());
        dataEnvelopeHandlerList.forEach(handler -> handler.handleNewDataEnvelope(dataEnvelope));
    }

}
