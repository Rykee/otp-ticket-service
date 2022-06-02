package ticketservice.manager;

import hu.matray.ticket.springbootserverapi.model.EventDetailResponse;
import hu.matray.ticket.springbootserverapi.model.EventListResponse;
import hu.matray.ticket.springbootserverapi.model.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketservice.entity.UserEntity;
import ticketservice.model.ReserveDetails;
import ticketservice.repository.UserRepository;
import ticketservice.validation.ValidatorService;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Transactional(propagation = REQUIRED)
//core
public class TicketManager {

    UserRepository userRepository;
    TicketService ticketService;
    ValidatorService validatorService;

    public EventDetailResponse getEvent(long eventId) {
        return ticketService.getEvent(eventId);
    }

    public EventListResponse getEvents() {
        return ticketService.getEvents();
    }

    public ReservationResponse reserve(String userToken, Long eventId, Long seatId, Long cardId) {
        //We can use .get as the Interceptor already verified that a user exists with this token
        UserEntity user = userRepository.findByTokensToken(userToken).get();
        synchronized (user.getId()) {
            synchronized (Integer.valueOf(Objects.hash(eventId, seatId))) {
                ReserveDetails reserveDetails = validatorService.validateReserveAndGetDetails(user, () -> ticketService.getEvent(eventId),
                        ticketService::getEvents, eventId, seatId, cardId);
                ReservationResponse reserveResponse = ticketService.reserve(eventId, seatId);
                if (reserveResponse.getSuccess()) {
                    reserveDetails.getBankCard().setAmount(reserveDetails.getBankCard().getAmount() - reserveDetails.getSeatInfo().getPrice());
                }
                return reserveResponse;
            }
        }
    }

}
