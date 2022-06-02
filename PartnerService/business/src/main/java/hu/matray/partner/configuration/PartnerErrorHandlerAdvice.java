package hu.matray.partner.configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.matray.partner.endpoint.exception.PartnerError;
import hu.matray.partner.endpoint.exception.PartnerException;
import hu.matray.partner.endpoint.exception.ReservationException;
import hu.matray.partner.springbootserverapi.model.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static hu.matray.partner.endpoint.exception.PartnerErrorCode.INVALID_HTTP_REQUEST;
import static hu.matray.partner.endpoint.exception.PartnerErrorCode.INVALID_JSON;
import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.io.IOUtils.write;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@ControllerAdvice
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PartnerErrorHandlerAdvice {

    ObjectMapper objectMapper;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleJsonParseException(HttpMessageNotReadableException ex, HttpServletResponse response) throws IOException {
        val errorCode = ex.getMostSpecificCause() instanceof JsonParseException
                ? INVALID_JSON
                : INVALID_HTTP_REQUEST;

        PartnerError error = new PartnerError(errorCode);
        sendErrorResponse(BAD_REQUEST, error, response);
    }

    @ExceptionHandler(PartnerException.class)
    public void handlePartnerException(PartnerException exception, HttpServletResponse response) throws IOException {
        sendErrorResponse(exception.getStatusCode(), exception.getPartnerError(), response);
    }

    @ExceptionHandler(ReservationException.class)
    public void handleReservationException(ReservationException exception, HttpServletResponse response) throws IOException {
        ReservationResponse reservationResponse = ReservationResponse.builder()
                .success(false)
                .errorCode(exception.getErrorCode())
                .build();
        response.setStatus(200);
        response.setContentType(APPLICATION_JSON_VALUE);
        write(objectMapper.writeValueAsString(reservationResponse), response.getOutputStream(), UTF_8);
    }

    private void sendErrorResponse(HttpStatus status, PartnerError error, HttpServletResponse response) throws IOException {
        sendErrorResponse(status.value(), error, response);
    }

    private void sendErrorResponse(int status, PartnerError error, HttpServletResponse response) throws IOException {
        response.setStatus(status);
        response.setContentType(APPLICATION_JSON_VALUE);
        write(objectMapper.writeValueAsString(error), response.getOutputStream(), UTF_8);
    }

}
