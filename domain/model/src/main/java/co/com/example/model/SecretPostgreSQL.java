package co.com.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SecretPostgreSQL {

    private String host;
    private String database;
    private String schema;
    private String username;
    private String port;
    private String password;

}
