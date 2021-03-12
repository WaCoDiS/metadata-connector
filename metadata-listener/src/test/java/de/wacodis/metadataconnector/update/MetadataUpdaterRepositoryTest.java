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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.update.GdiDeMetadataUpdater;
import de.wacodis.metadataconnector.update.AbstractMetadataUpdater;
import de.wacodis.metadataconnector.update.SensorWebMetadataUpdater;
import de.wacodis.metadataconnector.update.MetadataUpdaterRepository;
import de.wacodis.metadataconnector.update.CopernicusMetadataUpdater;
import de.wacodis.metadataconnector.update.DwdMetadataUpdater;
import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.CopernicusDataEnvelope;
import de.wacodis.metadataconnector.model.GdiDeDataEnvelope;
import de.wacodis.metadataconnector.model.SensorWebDataEnvelope;
import de.wacodis.metadataconnector.model.DwdDataEnvelope;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@ContextConfiguration(classes = {
    CopernicusMetadataUpdater.class,
    GdiDeMetadataUpdater.class,
    MetadataUpdaterRepository.class,
    SensorWebMetadataUpdater.class,
    DwdMetadataUpdater.class})
@SpringBootTest
public class MetadataUpdaterRepositoryTest {

    @Autowired
    private MetadataUpdaterRepository repository;

    @ParameterizedTest
    @MethodSource("provideMetadataUpdater")
    public void testGetMetadataUpdater(AbstractDataEnvelope envelope, Class<AbstractMetadataUpdater> updaterClass) {
        AbstractMetadataUpdater updater = repository.getMetadataUpdater(envelope);
        Assertions.assertTrue(updaterClass.isInstance(updater));
    }

    private static Stream<Arguments> provideMetadataUpdater() {
        return Stream.of(
                Arguments.of(new CopernicusDataEnvelope(), CopernicusMetadataUpdater.class),
                Arguments.of(new GdiDeDataEnvelope(), GdiDeMetadataUpdater.class),
                Arguments.of(new SensorWebDataEnvelope(), SensorWebMetadataUpdater.class),
                Arguments.of(new DwdDataEnvelope(), DwdMetadataUpdater.class)
        );
    }

}
