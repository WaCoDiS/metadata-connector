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
package de.wacodis.metadataconnector.app;

import de.wacodis.metadataconnector.configuration.DataAccessConfiguration;
import java.net.MalformedURLException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@SpringBootApplication
@ComponentScan({"de.wacodis.metadataconnector"
})
@RefreshScope
public class MetadataConnectorApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataConnectorApp.class);

    @Autowired
    private DataAccessConfiguration dataAccessConfiguration;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MetadataConnectorApp.class, args);
    }

    @PostConstruct
    private void initialize() throws MalformedURLException {
        LOGGER.info("Metadata Connector started");
    }
}
