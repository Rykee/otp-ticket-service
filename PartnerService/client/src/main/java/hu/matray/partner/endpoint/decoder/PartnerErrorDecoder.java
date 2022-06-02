package hu.matray.partner.endpoint.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import hu.matray.partner.endpoint.exception.PartnerError;
import hu.matray.partner.endpoint.exception.PartnerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class PartnerErrorDecoder implements ErrorDecoder {

    public static final String HEADER_CONTENT_TYPE = "content-type";

    private final ErrorDecoder delegate = new Default();
    private final ObjectMapper mapper;

    public PartnerErrorDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private static boolean isJSONContentType(Response response) {
        Collection<String> contentTypes = response.headers().get(HEADER_CONTENT_TYPE);
        return contentTypes != null
                && !contentTypes.isEmpty()
                && contentTypes.contains(APPLICATION_JSON_VALUE);
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        byte[] body;
        int status = response.status();

        try {
            body = IOUtils.toByteArray(response.body().asInputStream());
            log.error("Body from {}: {}", methodKey, new String(body));
        } catch (IOException ex) {
            return new RuntimeException(String.format("Failed to read response from %s", methodKey), ex);
        }

        try {
            if (status >= 400 && status <= 599 && isJSONContentType(response)) {
                PartnerError error = mapper.readValue(body, PartnerError.class);
                return new PartnerException(status, error);
            }
        } catch (Exception ex) {
            return new RuntimeException(String.format(
                    "Failed to decode JSON response from %s: %s",
                    methodKey, new String(body)), ex);
        }

        return delegate.decode(methodKey, response);
    }
}
