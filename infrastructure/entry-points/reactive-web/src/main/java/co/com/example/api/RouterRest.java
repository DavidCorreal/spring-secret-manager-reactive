package co.com.example.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Log4j2
@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerExample(Handler handler) {
        return route(GET("/api/secret/getSecret"), handler::listenGETUseCase);
    }
}
