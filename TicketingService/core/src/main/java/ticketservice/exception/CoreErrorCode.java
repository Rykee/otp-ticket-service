package ticketservice.exception;

import hu.matray.partner.endpoint.exception.PartnerErrorCode;

import java.util.Arrays;

public enum CoreErrorCode {

    USER_TOKEN_MISSING(10050, "A felhasználói token header nem található"),
    USER_TOKEN_INVALID(10051, "A felhasználói token lejárt vagy nem értelmezhető"),
    BANK_CARD_NOT_FOUND(10100, "Ez a bankkártya nem ehhez a felhasználóhoz tartozik"),
    INSUFFICIENT_FOUNDS(10101, "A felhasználónak nincs elegendő pénze hogy megvásárolja a jegyet!"),
    INVALID_JSON(10102, "A requestben érkező JSON érvénytelen"),
    INVALID_HTTP_REQUEST(10103, "A request nem értelmezhető"),
    EVENT_NOT_FOUND(20001, "Nem létezik ilyen esemény!"),
    SEAT_NOT_FOUND(20002, "Nem létezik ilyen szék!"),
    SEAT_ALREADY_TAKEN(20010, "Már lefoglalt székre nem lehet jegyet eladni!"),
    EVENT_ALREADY_STARTED(20011, "Olyan eseményre ami már elkezdődött nem lehet jegyet eladni!"),
    PARTNER_SERVICE_ERROR(20012, "A külső rendszer nem volt képes az adatok szolgáltatására"),
    PARTNER_SERVICE_UNAVAIALBLE(20404, "A külső rendszer nem elérhető!"),
    UNKNOWN(29999, "Ismeretlen hiba történt");

    private final int errorCode;
    private final String errorMessage;

    CoreErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static CoreErrorCode fromErrorCode(int errorCode) {
        return Arrays.stream(values())
                .filter((coreErrorCode) -> coreErrorCode.getErrorCode() == errorCode)
                .findAny()
                .orElse(null);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
