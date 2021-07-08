package com.listener.interestingtweetsfinder.config;

import com.listener.interestingtweetsfinder.utils.CredentialManager;
import com.listener.interestingtweetsfinder.utils.Credentials;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${general.mongo.max_pool_size}")
    private long max_pool_size;

    @Value("${general.mongo.server_selection_timeout}")
    private long server_selection_timeout;

    private final CredentialManager credentialManager= CredentialManager.getInstance();

    @Bean
    @Scope("singleton")
    public MongoClient mongoClient(){
        String connectionString = String.format("mongodb+srv://%s:%s@%s/%s?maxPoolSize=%d&serverSelectionTimeoutMS=%d",
                credentialManager.get(Credentials.MONGODB_USER),
                credentialManager.get(Credentials.MONGODB_PASSWORD),
                credentialManager.get(Credentials.MONGODB_HOST),
                credentialManager.get(Credentials.MONGODB_DATABASE_NAME),
                max_pool_size,
                server_selection_timeout);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoOperations mongoTemplate(){
        return new MongoTemplate(mongoClient(), credentialManager.get(Credentials.MONGODB_DATABASE_NAME));
    }

}
