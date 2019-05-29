/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.configuration;

import de.wacodis.core.engine.utils.http.RequestLoggingInterceptor;
import java.util.Arrays;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        LOGGER.info("Data Access URI set to: " + this.uri);
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
                .additionalInterceptors(new RequestLoggingInterceptor())
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }

}
