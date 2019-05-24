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
import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.CopernicusDataEnvelope;
import de.wacodis.metadataconnector.model.GdiDeDataEnvelope;
import de.wacodis.metadataconnector.model.SensorWebDataEnvelope;
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
    SensorWebMetadataUpdater.class,})
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
                Arguments.of(new SensorWebDataEnvelope(), SensorWebMetadataUpdater.class)
        );
    }

}
