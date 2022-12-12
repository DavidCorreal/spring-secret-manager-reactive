package co.com.example.usecase.secret.gateway;

import reactor.core.publisher.Mono;

public interface SecretsSource {

    <T> Mono<T> getSecret(String secretName, Class<T> cls);

}