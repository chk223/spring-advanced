package org.example.expert.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
@Getter
@Configuration
@PropertySource(value = "classpath:dbconfig.yml", factory = YamlPropertySourceFactory.class)
public class DbConfig {
    @Value("${db.name}")
    private String dbName;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;
}
