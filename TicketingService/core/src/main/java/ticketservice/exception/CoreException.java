package ticketservice.exception;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {

    private final int statusCode;
    private final CoreError coreError;

    public CoreException(int statusCode, CoreError coreError, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.coreError = coreError;
    }

    public CoreException(int statusCode, CoreErrorCode coreErrorCode) {
        this(statusCode, coreErrorCode, null);
    }

    public CoreException(int statusCode, CoreErrorCode coreErrorCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.coreError = new CoreError(coreErrorCode);
    }

    public CoreException(int statusCode, CoreError coreError) {
        this(statusCode, coreError, null);
    }

}
