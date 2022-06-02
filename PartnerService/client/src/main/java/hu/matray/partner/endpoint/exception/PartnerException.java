package hu.matray.partner.endpoint.exception;

import lombok.Getter;

@Getter
public class PartnerException extends RuntimeException {

    private final int statusCode;
    private final PartnerError partnerError;

    public PartnerException(int statusCode, PartnerError partnerError, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.partnerError = partnerError;
    }

    public PartnerException(int statusCode, PartnerError partnerError) {
        this(statusCode, partnerError, null);
    }

    public PartnerException(int statusCode, PartnerErrorCode partnerErrorCode) {
        this(statusCode, partnerErrorCode, null);
    }

    public PartnerException(int statusCode, PartnerErrorCode partnerErrorCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.partnerError = new PartnerError(partnerErrorCode);
    }

}
