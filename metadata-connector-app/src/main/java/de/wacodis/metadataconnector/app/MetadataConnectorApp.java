/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
@ComponentScan({
    "de.wacodis.metadataconnector"})
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
