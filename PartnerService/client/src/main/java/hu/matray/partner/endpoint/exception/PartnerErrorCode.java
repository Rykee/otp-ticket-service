package hu.matray.partner.endpoint.exception;

import java.util.Arrays;

public enum PartnerErrorCode {

    EVENT_NOT_FOUND(90001, "Nem létezik ilyen esemény!"),
    SEAT_NOT_FOUND(90002, "Nem létezik ilyen szék!"),
    SEAT_ALREADY_TAKEN(90003, "Már lefoglalt székre nem lehet jegyet eladni!"),
    STATIC_JSON_READ_ERROR(90004, "Nem sikerült a statikus JSON választ feldolgozni"),
    INVALID_JSON(90005, "A requestben érkező JSON érvénytelen"),
    INVALID_HTTP_REQUEST(90006, "A request nem értelmezhető");

    private final int errorCode;
    private final String errorMessage;

    PartnerErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static PartnerErrorCode fromErrorCode(int errorCode) {
        return Arrays.stream(values())
                .filter(partnerErrorCode -> partnerErrorCode.getErrorCode() == errorCode)
                .findAny()
                .orElse(null);
    }
}

