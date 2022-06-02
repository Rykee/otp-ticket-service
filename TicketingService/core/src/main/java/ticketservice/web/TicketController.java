package ticketservice.web;

import hu.matray.ticket.springbootserverapi.GetEventApi;
import hu.matray.ticket.springbootserverapi.GetEventsApi;
import hu.matray.ticket.springbootserverapi.PayApi;
import hu.matray.ticket.springbootserverapi.model.EventDetailResponse;
import hu.matray.ticket.springbootserverapi.model.EventListResponse;
import hu.matray.ticket.springbootserverapi.model.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import ticketservice.manager.TicketManager;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class TicketController implements GetEventsApi, GetEventApi, PayApi {

    TicketManager ticketManager;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return GetEventsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<EventDetailResponse> getEvent(String userToken, Long eventId) {
        return ResponseEntity.ok(ticketManager.getEvent(eventId));
    }

    @Override
    public ResponseEntity<EventListResponse> getEvents(String userToken) {
        return ResponseEntity.ok(ticketManager.getEvents());
    }

    @Override
    public ResponseEntity<ReservationResponse> reserve(Long eventId, Long seatId, Long cardId, String userToken) {
        return ResponseEntity.ok(ticketManager.reserve(userToken, eventId, seatId, cardId));
    }

}
