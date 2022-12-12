package co.com.example.secrets;

import co.com.example.secrets.exception.TechnicalException;
import co.com.example.secrets.properties.SecretsManagerProperties;
import co.com.example.usecase.secret.gateway.SecretsSource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClientBuilder;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.net.URI;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class SecretManager implements SecretsSource {

    private static final Logger logger = Logger.getLogger(SecretManager.class.getName());

    private final ObjectMapper objectMapper;
    private final SecretsManagerProperties secretsManagerProperties;

    @Override
    public <T> Mono<T> getSecret(String secretName, Class<T> clazz) {
        return getValue(secretName).map(
                s -> {
                    try {
                        return objectMapper.readValue(s , clazz);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private Mono<String> getValue(String secretName) {

        var client = getAsyncClient();
        var getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();

        return Mono.fromFuture(client.getSecretValue(getSecretValueRequest))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException("Secret value is null"))))
                .flatMap(secretResult -> {
                    if (secretResult.secretString() != null) {
                        String result = secretResult.secretString();
                        return Mono.just(result);
                    }
                    return Mono.error(new TechnicalException("Secret value is not a String"));
                })
                .doOnError((err)->{
                    throw new TechnicalException(err, err.getMessage());
                });
    }

    private SecretsManagerAsyncClient getAsyncClient() {

        SecretsManagerAsyncClientBuilder clientBuilder = SecretsManagerAsyncClient.builder()
                .region(getRegion())
                .credentialsProvider(ProfileCredentialsProvider.create());

        if (secretsManagerProperties.getEndpoint() != null) {
            clientBuilder.endpointOverride(URI.create(secretsManagerProperties.getEndpoint()));
        }
        return clientBuilder.build();
    }

    private Region getRegion() {
        var region = secretsManagerProperties.getRegion();
        return region == null ? Region.US_EAST_1: Region.of(region);
    }

}