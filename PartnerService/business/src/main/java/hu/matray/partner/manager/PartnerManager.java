package hu.matray.partner.manager;

import hu.matray.partner.configuration.AppConfig;
import hu.matray.partner.endpoint.exception.PartnerException;
import hu.matray.partner.endpoint.exception.ReservationException;
import hu.matray.partner.springbootserverapi.model.EventDetailResponse;
import hu.matray.partner.springbootserverapi.model.EventListResponse;
import hu.matray.partner.springbootserverapi.model.ReservationResponse;
import hu.matray.partner.springbootserverapi.model.SeatInfo;
import hu.matray.partner.util.JsonReader;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Random;

import static hu.matray.partner.endpoint.exception.PartnerErrorCode.EVENT_NOT_FOUND;
import static hu.matray.partner.endpoint.exception.PartnerErrorCode.SEAT_ALREADY_TAKEN;
import static hu.matray.partner.endpoint.exception.PartnerErrorCode.SEAT_NOT_FOUND;
import static java.lang.Boolean.TRUE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class PartnerManager {

    private final static Random RANDOM = new Random();

    JsonReader jsonReader;
    AppConfig appConfig;

    public EventListResponse getEvents() {
        return jsonReader.parseFile(appConfig.getGetEventsJsonPath(), EventListResponse.class);
    }

    public EventDetailResponse getEvent(Long eventId) {
        String eventFilePath = appConfig.getGetEventJsonPathPattern().replace("{id}", eventId.toString());
        URL eventResource = this.getClass().getResource(eventFilePath);
        if (eventResource == null) {
            throw new PartnerException(NOT_FOUND.value(), EVENT_NOT_FOUND);
        }
        return jsonReader.parseFile(eventFilePath, EventDetailResponse.class);
    }

    public ReservationResponse reserve(Long eventId, Long seatId) {
        EventDetailResponse event = getEventForReservation(eventId);
        SeatInfo selectedSeat = event.getData().getSeats().stream()
                .filter(seatInfo -> seatId.equals(Long.valueOf(seatInfo.getId().substring(1))))
                .findAny()
                .orElseThrow(() -> new ReservationException(SEAT_NOT_FOUND.getErrorCode()));
        if (TRUE.equals(selectedSeat.getReserved())) {
            throw new ReservationException(SEAT_ALREADY_TAKEN.getErrorCode());
        }
        return ReservationResponse.builder()
                .reserver(RANDOM.nextLong())
                .success(true)
                .build();
    }

    private EventDetailResponse getEventForReservation(Long eventId) {
        try {
            return getEvent(eventId);
        } catch (PartnerException e) {
            throw new ReservationException(EVENT_NOT_FOUND.getErrorCode());
        }
    }

}
