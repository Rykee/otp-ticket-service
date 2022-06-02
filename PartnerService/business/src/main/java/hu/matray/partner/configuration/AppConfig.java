package hu.matray.partner.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class AppConfig {

    String getEventsJsonPath;
    String getEventJsonPathPattern;

}
