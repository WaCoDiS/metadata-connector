/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeAreaOfInterest;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeTimeFrame;
import de.wacodis.metadataconnector.model.CopernicusDataEnvelope;
import de.wacodis.metadataconnector.model.SensorWebDataEnvelope;
import java.util.Arrays;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class SensorWebMetadataUpdaterTest {

    private SensorWebDataEnvelope existingDataEnvelope;
    private SensorWebDataEnvelope newDataEnvelope;
    private SensorWebMetadataUpdater updater;

    @BeforeEach
    public void init() {
        updater = new SensorWebMetadataUpdater();
        existingDataEnvelope = new SensorWebDataEnvelope();
        newDataEnvelope = new SensorWebDataEnvelope();

        AbstractDataEnvelopeAreaOfInterest existingAoi = new AbstractDataEnvelopeAreaOfInterest();
        existingAoi.setExtent(Arrays.asList(7.0000f, 52.0001f, 7.0001f, 52.0002f));
        existingDataEnvelope.setAreaOfInterest(existingAoi);

        AbstractDataEnvelopeTimeFrame exisitingTimeFrame = new AbstractDataEnvelopeTimeFrame();
        exisitingTimeFrame.setStartTime(DateTime.parse("2019-01-01T01:00Z"));
        exisitingTimeFrame.setEndTime(DateTime.parse("2019-01-01T02:00Z"));
        existingDataEnvelope.setTimeFrame(exisitingTimeFrame);

        existingDataEnvelope.setCreated(DateTime.parse("2019-01-01T01:00Z"));
        existingDataEnvelope.setModified(DateTime.parse("2019-01-10T01:00Z"));
    }

    @Test
    public void testSupportDataEnvelopeForSupportedEnvelope() {
        Assertions.assertTrue(updater.supportsDataEnvelope(new SensorWebDataEnvelope()));
    }

    @Test
    public void testSupportDataEnvelopeForUnsupportedEnvelope() {
        Assertions.assertFalse(updater.supportsDataEnvelope(new CopernicusDataEnvelope()));
    }

    @Test
    public void testUpdateDataEnvelope() {
        AbstractDataEnvelopeAreaOfInterest newAoi = new AbstractDataEnvelopeAreaOfInterest();
        newAoi.setExtent(Arrays.asList(7.0001f, 52.0000f, 7.0002f, 52.0002f));
        newDataEnvelope.setAreaOfInterest(newAoi);
        newDataEnvelope.setCreated(DateTime.parse("2019-01-02T00:00Z"));
        newDataEnvelope.setModified(DateTime.parse("2019-01-12T01:00Z"));

        AbstractDataEnvelopeTimeFrame newTimeFrame = new AbstractDataEnvelopeTimeFrame();
        newTimeFrame.setStartTime(DateTime.parse("2019-01-01T02:00Z"));
        newTimeFrame.setEndTime(DateTime.parse("2019-01-01T04:00Z"));
        newDataEnvelope.setTimeFrame(newTimeFrame);

        AbstractDataEnvelope mergedDataenvelope = updater.updateDataEnvelope(existingDataEnvelope, newDataEnvelope);

        AbstractDataEnvelopeAreaOfInterest expectedAoi = new AbstractDataEnvelopeAreaOfInterest();
        expectedAoi.setExtent(Arrays.asList(7.0000f, 52.0000f, 7.0002f, 52.0002f));

        Assertions.assertEquals(expectedAoi, mergedDataenvelope.getAreaOfInterest());
        Assertions.assertEquals(existingDataEnvelope.getCreated(), mergedDataenvelope.getCreated());
        Assertions.assertEquals(newDataEnvelope.getModified(), mergedDataenvelope.getModified());
        Assertions.assertEquals(existingDataEnvelope.getTimeFrame().getStartTime(), mergedDataenvelope.getTimeFrame().getStartTime());
        Assertions.assertEquals(newDataEnvelope.getTimeFrame().getEndTime(), mergedDataenvelope.getTimeFrame().getEndTime());

    }

}
