package ticketservice.configuration;

import com.fasterxml.jackson.core.JsonParseException;
import hu.matray.partner.endpoint.exception.ReservationException;
import hu.matray.partner.springbootserverapi.model.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ticketservice.exception.CoreError;
import ticketservice.exception.CoreException;
import ticketservice.util.HttpUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.ConnectException;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.io.IOUtils.write;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static ticketservice.exception.CoreErrorCode.INVALID_HTTP_REQUEST;
import static ticketservice.exception.CoreErrorCode.INVALID_JSON;
import static ticketservice.exception.CoreErrorCode.PARTNER_SERVICE_UNAVAIALBLE;

@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CoreErrorHandlerAdvice {

    HttpUtils httpUtils;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleJsonParseException(HttpMessageNotReadableException ex, HttpServletResponse response) throws IOException {
        val errorCode = ex.getMostSpecificCause() instanceof JsonParseException
                ? INVALID_JSON
                : INVALID_HTTP_REQUEST;

        CoreError error = new CoreError(errorCode);
        setErrorResponse(BAD_REQUEST, error, response);
    }

    @ExceptionHandler(ConnectException.class)
    public void handleConnectException(ConnectException exception, HttpServletResponse response) throws IOException {
        CoreError error = new CoreError(PARTNER_SERVICE_UNAVAIALBLE);
        setErrorResponse(INTERNAL_SERVER_ERROR, error, response);
    }

    @ExceptionHandler(CoreException.class)
    public void handleCoreException(CoreException exception, HttpServletResponse response) throws IOException {
        httpUtils.setResponse(exception.getStatusCode(), exception.getCoreError(), response);
    }

    @ExceptionHandler(ReservationException.class)
    public void handleReservationException(ReservationException exception, HttpServletResponse response) throws IOException {
        ReservationResponse reservationResponse = ReservationResponse.builder()
                .success(false)
                .errorCode(exception.getErrorCode())
                .build();
        httpUtils.setResponse(OK.value(), reservationResponse, response);
    }

    private void setErrorResponse(HttpStatus status, CoreError error, HttpServletResponse response) throws IOException {
        httpUtils.setResponse(status.value(), error, response);
    }

}
