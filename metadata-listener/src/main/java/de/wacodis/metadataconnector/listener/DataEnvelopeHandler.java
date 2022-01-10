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

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import org.springframework.stereotype.Component;

/**
 * Handler interface for a new {@link AbstractDataEnvelope} creation event
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public interface DataEnvelopeHandler {

    public abstract void handleNewDataEnvelope(AbstractDataEnvelope dataEnvelope);
}
