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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.http.dataacess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.wacodis.metadataconnector.configuration.DataAccessConfiguration;
import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeAreaOfInterest;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeTimeFrame;
import de.wacodis.metadataconnector.model.SensorWebDataEnvelope;
import java.util.Arrays;
import java.util.Optional;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@RestClientTest(DataAccessService.class)
@ContextConfiguration(classes = {DataAccessConfiguration.class, DataAccessService.class})
public class DataAccessServiceTest {

    @Autowired
    private DataAccessService service;

    @Autowired
    @Qualifier("dataAccessService")
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockRestServiceServer mockServer;

    private SensorWebDataEnvelope testEnv;

    private SensorWebDataEnvelope expEnv;

    @BeforeEach
    public void init() {
        expEnv = prepareEnvelope();
        testEnv = prepareEnvelope();

        AbstractDataEnvelopeAreaOfInterest testAoi = new AbstractDataEnvelopeAreaOfInterest();
        testAoi.setExtent(Arrays.asList(7.0001f, 52.0001f, 7.0002f, 52.0002f));
        testEnv.setAreaOfInterest(testAoi);

        AbstractDataEnvelopeTimeFrame testTimeFrame = new AbstractDataEnvelopeTimeFrame();
        testTimeFrame.setStartTime(DateTime.parse("2019-01-02T00:00Z"));
        testTimeFrame.setEndTime(DateTime.parse("2019-01-03T00:00Z"));
        testEnv.setTimeFrame(testTimeFrame);

        AbstractDataEnvelopeAreaOfInterest expAoi = new AbstractDataEnvelopeAreaOfInterest();
        expAoi.setExtent(Arrays.asList(7.0000f, 52.0000f, 7.0003f, 52.0003f));
        expEnv.setAreaOfInterest(expAoi);

        AbstractDataEnvelopeTimeFrame expTimeFrame = new AbstractDataEnvelopeTimeFrame();
        expTimeFrame.setStartTime(DateTime.parse("2019-01-01T00:00Z"));
        expTimeFrame.setEndTime(DateTime.parse("2019-01-4T00:00Z"));
        expEnv.setTimeFrame(expTimeFrame);
    }

    @Test
    public void testCreateDataEnvelopeWithSuccess() throws JsonProcessingException, DataAccessRequestException {
        mockServer.expect(ExpectedCount.once(),
                requestTo("/dataenvelopes"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(testEnv))
                );
        AbstractDataEnvelope result = service.createDataEnvelope(testEnv);

        mockServer.verify();
        Assertions.assertEquals(testEnv, result);
    }

    @Test
    public void testCreateDataEnvelopeWithErrorResponse() throws JsonProcessingException, DataAccessRequestException {
        de.wacodis.metadataconnector.model.Error error = new de.wacodis.metadataconnector.model.Error();
        error.code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.message("Requested resource could not be created");

        mockServer.expect(ExpectedCount.once(),
                requestTo("/dataenvelopes"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(error))
                );

        Assertions.assertThrows(DataAccessRequestException.class, () -> {
            service.createDataEnvelope(testEnv);
        });
        mockServer.verify();
    }

    @Test
    public void testSearchDataEnvelopeWithSuccess() throws JsonProcessingException, DataAccessRequestException {
        mockServer.expect(ExpectedCount.once(),
                requestTo("/dataenvelopes/search"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expEnv))
                );
        Optional<AbstractDataEnvelope> result = service.searchSingleDataEnvelope(testEnv);

        mockServer.verify();
        Assertions.assertEquals(expEnv, result.get());
    }

    @Test
    public void testSearchDataEnvelopeWithErrorResponse() throws JsonProcessingException, DataAccessRequestException {
        de.wacodis.metadataconnector.model.Error error = new de.wacodis.metadataconnector.model.Error();
        error.code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.message("Could not search for requestd resource");

        mockServer.expect(ExpectedCount.once(),
                requestTo("/dataenvelopes/search"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(error))
                );

        Assertions.assertThrows(DataAccessRequestException.class, () -> {
            service.searchSingleDataEnvelope(testEnv);
        });
        mockServer.verify();
    }

    @Test
    public void testSearchDataEnvelopeWithNoResult() throws JsonProcessingException, DataAccessRequestException {
        mockServer.expect(ExpectedCount.once(),
                requestTo("/dataenvelopes/search"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        Optional<AbstractDataEnvelope> result = service.searchSingleDataEnvelope(testEnv);

        mockServer.verify();
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateDataEnvelopeWithSuccess() throws JsonProcessingException, DataAccessRequestException {
        mockServer.expect(ExpectedCount.once(),
                requestTo("/dataenvelopes/" + testEnv.getIdentifier()))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(testEnv))
                );
        AbstractDataEnvelope result = service.updateDataEnvelope(testEnv);

        mockServer.verify();
        Assertions.assertEquals(testEnv, result);
    }

    @Test
    public void testUpdateDataEnvelopeWithErrorResponse() throws JsonProcessingException, DataAccessRequestException {
        de.wacodis.metadataconnector.model.Error error = new de.wacodis.metadataconnector.model.Error();
        error.code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.message("Requestd resource could not be updated");

        mockServer.expect(ExpectedCount.once(),
                requestTo("/dataenvelopes/" + testEnv.getIdentifier()))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(error))
                );

        Assertions.assertThrows(DataAccessRequestException.class, () -> {
            service.updateDataEnvelope(testEnv);
        });
        mockServer.verify();
    }

    private SensorWebDataEnvelope prepareEnvelope() {
        SensorWebDataEnvelope envelope = new SensorWebDataEnvelope();
        envelope.setIdentifier("123Test");
        envelope.setOffering("testOffering");
        envelope.setObservedProperty("testProperty");
        envelope.setFeatureOfInterest("testFeature");
        envelope.setProcedure("testProcedure");
        envelope.setSourceType(AbstractDataEnvelope.SourceTypeEnum.SENSORWEBDATAENVELOPE);

        return envelope;
    }

}
