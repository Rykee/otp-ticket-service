package ticketservice.manager;

import hu.matray.partner.endpoint.PartnerFeignEndpoint;
import hu.matray.partner.endpoint.exception.PartnerException;
import hu.matray.ticket.springbootserverapi.model.EventDetailResponse;
import hu.matray.ticket.springbootserverapi.model.EventListResponse;
import hu.matray.ticket.springbootserverapi.model.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketservice.exception.ExceptionMapper;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@Transactional(propagation = MANDATORY)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
//Ticket modul
public class TicketService {

    PartnerFeignEndpoint partnerFeignClient;
    ModelMapper modelMapper = new ModelMapper();
    ExceptionMapper exceptionMapper;

    public EventDetailResponse getEvent(long eventId) {
        try {
            val event = partnerFeignClient.getEvent(eventId);
            return modelMapper.map(event.getBody(), EventDetailResponse.class);
        } catch (PartnerException partnerException) {
            throw exceptionMapper.map(partnerException);
        }
    }

    public EventListResponse getEvents() {
        try {
            val events = partnerFeignClient.getEvents();
            return modelMapper.map(events.getBody(), EventListResponse.class);
        } catch (PartnerException partnerException) {
            throw exceptionMapper.map(partnerException);
        }
    }

    public ReservationResponse reserve(Long eventId, Long seatId) {
        val reserveResponse = partnerFeignClient.reserve(eventId, seatId);
        return modelMapper.map(reserveResponse.getBody(), ReservationResponse.class);
    }

}
