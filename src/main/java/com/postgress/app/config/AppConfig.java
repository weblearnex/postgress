package com.postgress.app.config;

import com.azure.identity.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ChainedTokenCredential chainedTokenCredential() {
        AzureCliCredential azureCliCredential = new AzureCliCredentialBuilder()
                .build();

        ManagedIdentityCredential managedIdentityCredential = new ManagedIdentityCredentialBuilder()
                //.clientId("f63574e7-c67d-4d6b-a4ea-78c55e4081c7")
                .build();

        ChainedTokenCredential credentialChain = new ChainedTokenCredentialBuilder()
                .addLast(azureCliCredential)
                .addLast(managedIdentityCredential)
                .build();

        return credentialChain;
    }
}