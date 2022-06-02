package hu.matray.partner.endpoint.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartnerError {
    private String errorMessage;
    private int errorCode;

    public PartnerError(PartnerErrorCode partnerErrorCode) {
        this.errorMessage = partnerErrorCode.getErrorMessage();
        this.errorCode = partnerErrorCode.getErrorCode();
    }

    public PartnerError(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
