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
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.CopernicusDataEnvelope;
import org.springframework.stereotype.Component;

/**
 * MetadataUpdater for {@link CopernicusDataEnvelope} instances
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class CopernicusMetadataUpdater extends AbstractMetadataUpdater<CopernicusDataEnvelope> {

    @Override
    public boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        return dataEnvelope instanceof CopernicusDataEnvelope;
    }

    @Override
    public AbstractDataEnvelope updateDataEnvelope(CopernicusDataEnvelope existingDataEnvelope, CopernicusDataEnvelope newDataEnvelope) {
        newDataEnvelope.setIdentifier(existingDataEnvelope.getIdentifier());
        return newDataEnvelope;
    }

}
