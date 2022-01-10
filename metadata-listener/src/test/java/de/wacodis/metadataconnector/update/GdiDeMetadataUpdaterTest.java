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
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeAreaOfInterest;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeTimeFrame;
import de.wacodis.metadataconnector.model.GdiDeDataEnvelope;
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
public class GdiDeMetadataUpdaterTest {

    private GdiDeMetadataUpdater updater;

    private GdiDeDataEnvelope existingDataEnvelope;
    private GdiDeDataEnvelope newDataEnvelope;

    @BeforeEach
    public void init() {
        updater = new GdiDeMetadataUpdater();
        existingDataEnvelope = new GdiDeDataEnvelope();
        newDataEnvelope = new GdiDeDataEnvelope();

        AbstractDataEnvelopeAreaOfInterest existingAoi = new AbstractDataEnvelopeAreaOfInterest();
        existingAoi.setExtent(Arrays.asList(7.0003f, 52.0003f, 7.0006f, 52.0006f));
        existingDataEnvelope.setAreaOfInterest(existingAoi);

        AbstractDataEnvelopeTimeFrame exisitingTimeFrame = new AbstractDataEnvelopeTimeFrame();
        exisitingTimeFrame.setStartTime(DateTime.parse("2019-01-01T01:00Z"));
        exisitingTimeFrame.setEndTime(DateTime.parse("2019-01-01T05:00Z"));
        existingDataEnvelope.setTimeFrame(exisitingTimeFrame);

        existingDataEnvelope.setCreated(DateTime.parse("2019-01-02T01:00Z"));
        existingDataEnvelope.setModified(null);
    }

    @Test
    public void testSupportDataEnvelopeForSupportedEnvelope() {
        Assertions.assertTrue(updater.supportsDataEnvelope(new GdiDeDataEnvelope()));
    }

    @Test
    public void testSupportDataEnvelopeForUnsupportedEnvelope() {
        Assertions.assertFalse(updater.supportsDataEnvelope(new SensorWebDataEnvelope()));
    }

    @Test
    public void testUpdateDataEnvelope() {
        AbstractDataEnvelopeAreaOfInterest newAoi = new AbstractDataEnvelopeAreaOfInterest();
        newAoi.setExtent(Arrays.asList(7.0001f, 52.0005f, 7.0007f, 52.0002f));
        newDataEnvelope.setAreaOfInterest(newAoi);
        newDataEnvelope.setCreated(DateTime.parse("2019-01-01T00:00Z"));
        newDataEnvelope.setModified(DateTime.parse("2019-01-12T01:00Z"));

        AbstractDataEnvelopeTimeFrame newTimeFrame = new AbstractDataEnvelopeTimeFrame();
        newTimeFrame.setStartTime(DateTime.parse("2019-01-01T00:00Z"));
        newTimeFrame.setEndTime(DateTime.parse("2019-01-01T04:00Z"));
        newDataEnvelope.setTimeFrame(newTimeFrame);

        AbstractDataEnvelope mergedDataenvelope = updater.updateDataEnvelope(existingDataEnvelope, newDataEnvelope);

        AbstractDataEnvelopeAreaOfInterest expectedAoi = new AbstractDataEnvelopeAreaOfInterest();
        expectedAoi.setExtent(Arrays.asList(7.0001f, 52.0003f, 7.0007f, 52.0006f));

        Assertions.assertEquals(expectedAoi, mergedDataenvelope.getAreaOfInterest());
        Assertions.assertEquals(newDataEnvelope.getCreated(), mergedDataenvelope.getCreated());
        Assertions.assertEquals(newDataEnvelope.getModified(), mergedDataenvelope.getModified());
        Assertions.assertEquals(newDataEnvelope.getTimeFrame().getStartTime(), mergedDataenvelope.getTimeFrame().getStartTime());
        Assertions.assertEquals(existingDataEnvelope.getTimeFrame().getEndTime(), mergedDataenvelope.getTimeFrame().getEndTime());

    }

}
