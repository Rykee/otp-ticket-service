package hu.matray.partner.endpoint;

import hu.matray.partner.springbootserverapi.model.EventDetailResponse;
import hu.matray.partner.springbootserverapi.model.EventListResponse;
import hu.matray.partner.springbootserverapi.model.ReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static hu.matray.partner.endpoint.PartnerFeignConfig.CLIENT_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = CLIENT_NAME, url = "${application.partnerFeignClient.url}", configuration = PartnerFeignConfig.class)
public interface PartnerFeignEndpoint {

    @GetMapping(path = "/getEvents", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<EventListResponse> getEvents();

    @GetMapping(path = "/getEvent", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<EventDetailResponse> getEvent(@RequestParam(value = "EventId") Long eventId);

    @PostMapping(path = "/reserve", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<ReservationResponse> reserve(@RequestParam(value = "EventId") Long eventId, @RequestParam(value = "SeatId") Long seatId);

}
