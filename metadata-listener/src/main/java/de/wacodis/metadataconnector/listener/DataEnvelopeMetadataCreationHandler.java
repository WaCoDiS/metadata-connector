/*
 * Copyright 2019-2022 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.wacodis.metadataconnector.listener;

import de.wacodis.metadataconnector.configuration.DataAccessConfiguration;
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
 * Handler for a {@link AbstractDataEnvelope} creation event that creates a new
 * metadata resource or updates an existing one
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class DataEnvelopeMetadataCreationHandler implements DataEnvelopeHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataEnvelopeMetadataCreationHandler.class);

    private final Object lockObj = new Object();

    @Autowired
    private DataAccessProvider dataAccessProvider;

    @Autowired
    private MetadataUpdaterRepository metadataUpdaterRepository;

    @Autowired
    private DataAccessConfiguration dataAccessConfig;

    @Override
    public void handleNewDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        //ensure sequential processing of DataEnvelopes
        synchronized (lockObj) {
            LOGGER.info("handle new DataEnvelope");
            //delay DataEnvelope search to make sure data access' index has been refreshed
            //otherwise newly indexed DataEnvelopse might not be available for search
            delay();

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
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                LOGGER.debug("Error while handliung new DataEnvelope", ex);
            }
        }
    }

    public void delay() {
        long delay_Millies = this.dataAccessConfig.getDataenvelopeSearchDelay_Millies();

        if (delay_Millies > 0) {
            LOGGER.info("wait {} milliseconds until handling new DataEnvelope to make sure data access' index has been refreshed", delay_Millies);
            try {
                Thread.sleep(delay_Millies);
                LOGGER.info("waited for {} milliseconds, proceed handling new DataEnvelope", delay_Millies);
            } catch (InterruptedException ex) {
                LOGGER.warn("delay interrupted, proceed handling new DataEnvelope, data access' index might be not refreshed before sending search request, recently indexed DataEnvelopse might not yet be available for search");
                LOGGER.error("DataEnvelope-search delay interrupted", ex);
            }
        }
    }

}
