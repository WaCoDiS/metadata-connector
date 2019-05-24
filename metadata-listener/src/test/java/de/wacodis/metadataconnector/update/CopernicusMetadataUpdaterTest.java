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
public class CopernicusMetadataUpdaterTest {

    private CopernicusDataEnvelope existingDataEnvelope;
    private CopernicusDataEnvelope newDataEnvelope;
    CopernicusMetadataUpdater updater;

    @BeforeEach
    public void init() {
        existingDataEnvelope = new CopernicusDataEnvelope();
        newDataEnvelope = new CopernicusDataEnvelope();
        updater = new CopernicusMetadataUpdater();

        AbstractDataEnvelopeAreaOfInterest existingAoi = new AbstractDataEnvelopeAreaOfInterest();
        existingAoi.setExtent(Arrays.asList(7.0000f, 52.0000f, 7.0000f, 52.0000f));
        existingDataEnvelope.setAreaOfInterest(existingAoi);

        AbstractDataEnvelopeTimeFrame exisitingTimeFrame = new AbstractDataEnvelopeTimeFrame();
        exisitingTimeFrame.setStartTime(DateTime.parse("2019-01-01T00:00Z"));
        exisitingTimeFrame.setEndTime(DateTime.parse("2019-01-01T00:00Z"));
        existingDataEnvelope.setTimeFrame(exisitingTimeFrame);
    }

    @Test
    public void testSupportDataEnvelopeForSupportedEnvelope() {
        Assertions.assertTrue(updater.supportsDataEnvelope(new CopernicusDataEnvelope()));
    }

    @Test
    public void testSupportDataEnvelopeForUnsupportedEnvelope() {
        Assertions.assertFalse(updater.supportsDataEnvelope(new SensorWebDataEnvelope()));
    }

    @Test
    public void testUpdateDataEnvelope() {
        AbstractDataEnvelopeAreaOfInterest newAoi = new AbstractDataEnvelopeAreaOfInterest();
        newAoi.setExtent(Arrays.asList(7.0000f, 52.0000f, 7.0000f, 52.0000f));
        newDataEnvelope.setAreaOfInterest(newAoi);

        AbstractDataEnvelopeTimeFrame expTimeFrame = new AbstractDataEnvelopeTimeFrame();
        expTimeFrame.setStartTime(DateTime.parse("2019-01-01T00:00Z"));
        expTimeFrame.setEndTime(DateTime.parse("2019-01-01T00:00Z"));
        newDataEnvelope.setTimeFrame(expTimeFrame);

        AbstractDataEnvelope mergedDataenvelope = updater.updateDataEnvelope(existingDataEnvelope, newDataEnvelope);

        Assertions.assertEquals(existingDataEnvelope.getAreaOfInterest(), mergedDataenvelope.getAreaOfInterest());
        Assertions.assertEquals(existingDataEnvelope.getTimeFrame(), mergedDataenvelope.getTimeFrame());
    }

}
