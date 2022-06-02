package ticketservice.exception;

import hu.matray.partner.endpoint.exception.PartnerErrorCode;
import hu.matray.partner.endpoint.exception.PartnerException;
import org.springframework.stereotype.Component;

import static ticketservice.exception.CoreErrorCode.UNKNOWN;

@Component
public class ExceptionMapper {

    public CoreException map(PartnerException partnerException) {
        int errorCode = partnerException.getPartnerError().getErrorCode();
        int statusCode = partnerException.getStatusCode();
        switch (PartnerErrorCode.fromErrorCode(errorCode)) {
            case EVENT_NOT_FOUND:
                return new CoreException(statusCode, CoreErrorCode.EVENT_NOT_FOUND);
            case SEAT_NOT_FOUND:
                return new CoreException(statusCode, CoreErrorCode.SEAT_NOT_FOUND);
            case SEAT_ALREADY_TAKEN:
                return new CoreException(statusCode, CoreErrorCode.SEAT_ALREADY_TAKEN);
            case STATIC_JSON_READ_ERROR:
                return new CoreException(statusCode, CoreErrorCode.PARTNER_SERVICE_ERROR);
            case INVALID_JSON:
                return new CoreException(statusCode, CoreErrorCode.INVALID_JSON);
            case INVALID_HTTP_REQUEST:
                return new CoreException(statusCode, CoreErrorCode.INVALID_HTTP_REQUEST);
            default:
                return new CoreException(statusCode, UNKNOWN);
        }
    }

}
