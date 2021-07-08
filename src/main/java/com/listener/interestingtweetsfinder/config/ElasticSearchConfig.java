package com.listener.interestingtweetsfinder.config;

import com.listener.interestingtweetsfinder.utils.CredentialManager;
import com.listener.interestingtweetsfinder.utils.Credentials;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient client() {
        CredentialManager credentialManager= CredentialManager.getInstance ();
        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .withBasicAuth (
                        credentialManager.get (Credentials.ES_USER_NAME),
                        credentialManager.get (Credentials.ES_PASSWORD))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate (client());
    }
}
