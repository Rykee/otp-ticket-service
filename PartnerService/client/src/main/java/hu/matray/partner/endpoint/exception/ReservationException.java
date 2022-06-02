package hu.matray.partner.endpoint.exception;

import lombok.Getter;

@Getter
public class ReservationException extends RuntimeException {

    private final int errorCode;

    public ReservationException(int errorCode) {
        this.errorCode = errorCode;
    }

}
