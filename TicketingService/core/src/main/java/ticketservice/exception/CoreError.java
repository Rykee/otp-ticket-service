package ticketservice.exception;

import lombok.Getter;

@Getter
public class CoreError {

    private final String errorMessage;
    private final int errorCode;

    public CoreError(CoreErrorCode coreErrorCode) {
        this.errorMessage = coreErrorCode.getErrorMessage();
        this.errorCode = coreErrorCode.getErrorCode();
    }

    public CoreError(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

}
