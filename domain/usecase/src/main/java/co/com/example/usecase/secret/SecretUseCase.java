package co.com.example.usecase.secret;

import co.com.example.usecase.secret.gateway.SecretsSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SecretUseCase {

    private final SecretsSource secretsSource;

    public <T> Mono<T> getSecretsSource(@NonNull String secretName, @NonNull Class<T> clazz) {
        return secretsSource.getSecret(secretName, clazz);
    }

}