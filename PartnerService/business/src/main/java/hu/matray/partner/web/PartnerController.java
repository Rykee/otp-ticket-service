package hu.matray.partner.web;

import hu.matray.partner.endpoint.PartnerFeignEndpoint;
import hu.matray.partner.manager.PartnerManager;
import hu.matray.partner.springbootserverapi.model.EventDetailResponse;
import hu.matray.partner.springbootserverapi.model.EventListResponse;
import hu.matray.partner.springbootserverapi.model.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class PartnerController implements PartnerFeignEndpoint {

    PartnerManager partnerManager;

    @Override
    public ResponseEntity<EventListResponse> getEvents() {
        return ResponseEntity.ok(partnerManager.getEvents());
    }

    @Override
    public ResponseEntity<EventDetailResponse> getEvent(Long eventId) {
        return ResponseEntity.ok(partnerManager.getEvent(eventId));
    }

    @Override
    public ResponseEntity<ReservationResponse> reserve(Long eventId, Long seatId) {
        return ResponseEntity.ok(partnerManager.reserve(eventId, seatId));
    }

}
