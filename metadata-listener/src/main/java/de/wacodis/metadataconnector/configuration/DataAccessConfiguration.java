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
package de.wacodis.metadataconnector.configuration;

import de.wacodis.metadataconnector.http.dataacess.DataAccessLoggingInterceptor;
import java.util.Arrays;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for the DataAccess component
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("dataaccess")
public class DataAccessConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataAccessConfiguration.class);

    private String uri;
    private long dataenvelopeSearchDelay_Millies;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        LOGGER.info("Data Access URI set to: " + this.uri);
    }

    public long getDataenvelopeSearchDelay_Millies() {
        return dataenvelopeSearchDelay_Millies;
    }

    public void setDataenvelopeSearchDelay_Millies(long dataenvelopeSearchDelay_Millies) {
        this.dataenvelopeSearchDelay_Millies = dataenvelopeSearchDelay_Millies;
        LOGGER.info("DataEnvelope-Search delay set to (milliseconds): " + this.getDataenvelopeSearchDelay_Millies());
    }
    
    

    private RestTemplateBuilder builder;

    @Autowired
    public void setBuilder(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    @Bean
    @Qualifier("dataAccessService")
    public RestTemplate configureDataAccessService() {

        List<Header> defaultHeaders = Arrays.asList(
                new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(1000)
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultHeaders(defaultHeaders)
                .build();

        return builder
                .rootUri(getUri())
                .additionalInterceptors(new DataAccessLoggingInterceptor())
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }

}
