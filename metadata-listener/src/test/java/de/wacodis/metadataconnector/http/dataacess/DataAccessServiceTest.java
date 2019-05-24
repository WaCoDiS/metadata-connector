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
    public void testSearchDataEnvelopeWithSuccess() throws JsonProcessingException, DataAccessRequestException {
        mockServer.expect(ExpectedCount.once(),
                requestTo("/dataenvelopes/search"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expEnv))
                );
        AbstractDataEnvelope result = service.searchSingleDataEnvelope(testEnv);

        mockServer.verify();
        Assertions.assertEquals(expEnv, result);
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
