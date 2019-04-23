/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the DataAccess component
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("spring.dataaccess")
public class DataAccessConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataAccessConfiguration.class);

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        LOGGER.info("Data Access URI set to: " + this.uri);
    }

}
