package com.postgress.app.config;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.SimpleTokenCache;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class AzureAdDataSource extends HikariDataSource {
    private static final Logger logger = LoggerFactory.getLogger(AzureAdDataSource.class);

    private final SimpleTokenCache cache;

    public AzureAdDataSource(TokenCredential credential) {
        this.cache = new SimpleTokenCache(() -> credential.getToken(createRequestContext()));
    }

    @Override
    public String getPassword() {
         AccessToken accessToken = cache
                .getToken()
                .retry(1L)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Attempt to retrieve AAD token failed"));
        logger.info(accessToken.getToken());
        return accessToken.getToken();
    }

    private static TokenRequestContext createRequestContext() {

        return new TokenRequestContext().addScopes("https://ossrdbms-aad.database.windows.net");
    }
}