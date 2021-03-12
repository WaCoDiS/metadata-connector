/*
 * Copyright 2019-2021 52°North Initiative for Geospatial Open Source
 * Software GmbH
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
package de.wacodis.metadataconnector.http.dataacess;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for requesting the DataAccess API
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Service
public class DataAccessService implements DataAccessProvider {

    private static final String DATA_ENVELOPES_ENDPOINT = "/dataenvelopes";
    private static final String SEARCH_ENDPOINT = "/search";
    private static final String DATA_ENVELOPES_SEARCH_ENDPOINT = DATA_ENVELOPES_ENDPOINT + "/" + SEARCH_ENDPOINT;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataAccessService.class);

    private RestTemplate dataAccessService;

    @Autowired
    @Qualifier("dataAccessService")
    public void setBuilder(RestTemplate restTemplate) {
        this.dataAccessService = restTemplate;
    }

    @Override
    public AbstractDataEnvelope createDataEnvelope(AbstractDataEnvelope dataEnvelope) throws DataAccessRequestException {
        try {
            ResponseEntity<AbstractDataEnvelope> response = dataAccessService
                    .postForEntity(DATA_ENVELOPES_ENDPOINT, dataEnvelope, AbstractDataEnvelope.class);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            LOGGER.error("POST request for creating a new DataEnvelope {} returned status code: {}.",
                    dataEnvelope, ex.getStatusCode());
            throw new DataAccessRequestException("HTTP client error while sending create DataEnvelope POST request.", ex);
        } catch (RestClientException ex) {
            LOGGER.error(ex.getMessage());
            throw new DataAccessRequestException("Unexpected client error while sending create DataEnvelope POST request.", ex);
        }
    }

    @Override
    public AbstractDataEnvelope updateDataEnvelope(AbstractDataEnvelope dataEnvelope) throws DataAccessRequestException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<AbstractDataEnvelope> entity = new HttpEntity<AbstractDataEnvelope>(dataEnvelope, headers);

        try {
            ResponseEntity<AbstractDataEnvelope> response = dataAccessService
                    .exchange(DATA_ENVELOPES_ENDPOINT + "/" + dataEnvelope.getIdentifier(), HttpMethod.PUT, entity, AbstractDataEnvelope.class);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            LOGGER.error("PUT request for updating DataEnvelope {} returned status code: {}.",
                    dataEnvelope, ex.getStatusCode());
            throw new DataAccessRequestException("HTTP client error while sending update DataEnvelope PUT request.", ex);
        } catch (RestClientException ex) {
            LOGGER.error(ex.getMessage());
            throw new DataAccessRequestException("Unexpected client error while sending update DataEnvelope PUT request.", ex);
        }
    }

    @Override
    public Optional<AbstractDataEnvelope> searchSingleDataEnvelope(AbstractDataEnvelope dataEnvelope) throws DataAccessRequestException {
        try {
            ResponseEntity<AbstractDataEnvelope> response = dataAccessService
                    .postForEntity(DATA_ENVELOPES_SEARCH_ENDPOINT, dataEnvelope, AbstractDataEnvelope.class);
            return Optional.ofNullable(response.getBody());
        } catch (HttpStatusCodeException ex) {
            LOGGER.error("POST request for searching DataEnvelope {} returned status code: {}.",
                    dataEnvelope, ex.getStatusCode());
            throw new DataAccessRequestException("HTTP client error while sending search DataEnvelope POST request.", ex);
        } catch (RestClientException ex) {
            LOGGER.error(ex.getMessage());
            throw new DataAccessRequestException("Unexpected client error while sending search DataEnvelope POST request.", ex);
        }
    }
}
