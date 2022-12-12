package co.com.example.api;

import co.com.example.usecase.secret.SecretUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final SecretUseCase useCase;

    @NonNull
    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {

        var clazz = getClazz(serverRequest.queryParam("clazz").orElse(""));
        var secretName = serverRequest.queryParam("secretName").orElse("");

        return useCase.getSecretsSource(secretName, clazz)
                .flatMap(data -> ServerResponse.ok().bodyValue(data));
    }

    private Class<?> getClazz(String clazzName) {
        try {
            return Class.forName("co.com.example.model.".concat(clazzName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
