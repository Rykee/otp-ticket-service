package hu.matray.partner.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.matray.partner.endpoint.exception.PartnerException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static hu.matray.partner.endpoint.exception.PartnerErrorCode.STATIC_JSON_READ_ERROR;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JsonReader {

    ObjectMapper objectMapper;

    public <T> T parseFile(String filePath, Class<T> targetClass) {
        try {
            String json = IOUtils.resourceToString(filePath, StandardCharsets.UTF_8);
            return objectMapper.readValue(json, targetClass);
        } catch (IOException e) {
            throw new PartnerException(INTERNAL_SERVER_ERROR.value(), STATIC_JSON_READ_ERROR, e);
        }
    }

}
