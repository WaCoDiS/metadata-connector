/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.listener;

import de.wacodis.metadataconnector.http.dataacess.DataAccessProvider;
import de.wacodis.metadataconnector.http.dataacess.DataAccessRequestException;
import de.wacodis.metadataconnector.update.MetadataUpdaterRepository;
import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.update.AbstractMetadataUpdater;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handler for a {@link AbstractDataEnvelope} creation events that creates a new
 * metadata resource or updates an existing one
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class DataEnvelopeMetadataCreationHandler implements DataEnvelopeHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataEnvelopeMetadataCreationHandler.class);

    @Autowired
    private DataAccessProvider dataAccessProvider;

    @Autowired
    private MetadataUpdaterRepository metadataUpdaterRepository;

    @Override
    public void handleNewDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        try {
            Optional<AbstractDataEnvelope> searchResult = dataAccessProvider.searchSingleDataEnvelope(dataEnvelope);
            if (searchResult.isPresent()) {
                AbstractMetadataUpdater updater = metadataUpdaterRepository.getMetadataUpdater(dataEnvelope);
                AbstractDataEnvelope updatedDataEnvelope = updater.updateDataEnvelope(searchResult.get(), dataEnvelope);
                updatedDataEnvelope = dataAccessProvider.updateDataEnvelope(updatedDataEnvelope);
                LOGGER.info("DataEnvelope succesfully updated: {}", updatedDataEnvelope);
            } else {
                AbstractDataEnvelope newDataEnvelope = dataAccessProvider.createDataEnvelope(dataEnvelope);
                LOGGER.info("DataEnvelope succesfully created: {}", newDataEnvelope);
            }

        } catch (DataAccessRequestException ex) {
            LOGGER.error(ex.getMessage());
            LOGGER.debug("Error while sending DataAcces API request for DataEnvelope: "
                    + dataEnvelope.getIdentifier(), ex);
        }
    }

}
