package ticketservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration;

import java.io.IOException;

import static java.util.Collections.singleton;
import static lombok.AccessLevel.PRIVATE;
import static org.zalando.logbook.BodyFilter.merge;
import static org.zalando.logbook.BodyFilters.defaultValue;
import static org.zalando.logbook.json.JsonBodyFilters.replaceJsonStringProperty;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Slf4j
public class SpringConfig {

    LogbookAutoConfiguration logbookAutoConfiguration;

    @Bean
    public BodyFilter bodyFilter() {
        return merge(
                defaultValue(),
                logbookAutoConfiguration.bodyFilter());
    }
}
