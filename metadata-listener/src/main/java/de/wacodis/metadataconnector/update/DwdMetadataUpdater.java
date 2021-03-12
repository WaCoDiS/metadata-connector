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
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.DwdDataEnvelope;
import org.springframework.stereotype.Component;

/**
 * MetadataUpdater for {@link DwdDataEnvelope} instances
 * 
 * @author Lukas Butzmann
 */
public class DwdMetadataUpdater extends AbstractMetadataUpdater<DwdDataEnvelope>{

    @Override
    public boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        return dataEnvelope instanceof DwdDataEnvelope;
    }

    @Override
    public AbstractDataEnvelope updateDataEnvelope(DwdDataEnvelope existingDataEnvelope, DwdDataEnvelope newDataEnvelope) {
        existingDataEnvelope.setAreaOfInterest(
                this.mergeAreaOfInterest(
                        existingDataEnvelope.getAreaOfInterest(),
                        newDataEnvelope.getAreaOfInterest()));

        existingDataEnvelope.setCreated(
                this.mergeCreated(
                        existingDataEnvelope.getCreated(),
                        newDataEnvelope.getCreated()));

        existingDataEnvelope.setModified(
                this.mergeModified(
                        existingDataEnvelope.getModified(),
                        newDataEnvelope.getModified()));

        existingDataEnvelope.setTimeFrame(
                this.mergeTimeFrame(
                        existingDataEnvelope.getTimeFrame(),
                        newDataEnvelope.getTimeFrame()));

        return existingDataEnvelope;
    }
    
}
