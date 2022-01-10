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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link AbstractMetadataUpdater} that are specific to instances
 * of {@link AbstractDataEnvelope}
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class MetadataUpdaterRepository {

    @Autowired
    private List<AbstractMetadataUpdater> metadataUpdaterList;

    /**
     * Retrieves a {@link AbstractMetadataUpdater} depending on the
     * {@link AbstractDataEnvelope} type
     *
     * @param dateEnvelope the {@link AbstractDataEnvelope} to retrieve the
     * {@link AbstractMetadataUpdater} for
     * @return the {@link AbstractMetadataUpdater} corresponding to a
     * {@link AbstractDataEnvelope} type
     */
    public AbstractMetadataUpdater getMetadataUpdater(AbstractDataEnvelope dateEnvelope) {
        AbstractMetadataUpdater candidate = null;
        for (AbstractMetadataUpdater updater : metadataUpdaterList) {
            if (updater.supportsDataEnvelope(dateEnvelope)) {
                candidate = updater;
                break;
            }
        }
        return candidate;
    }
}
