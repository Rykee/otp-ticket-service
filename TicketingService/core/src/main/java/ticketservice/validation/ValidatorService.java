package ticketservice.validation;

import hu.matray.partner.endpoint.exception.ReservationException;
import hu.matray.ticket.springbootserverapi.model.Event;
import hu.matray.ticket.springbootserverapi.model.EventDetailResponse;
import hu.matray.ticket.springbootserverapi.model.EventListResponse;
import hu.matray.ticket.springbootserverapi.model.SeatInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketservice.entity.UserBankCardEntity;
import ticketservice.entity.UserEntity;
import ticketservice.model.ReserveDetails;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static java.lang.Boolean.TRUE;
import static java.lang.Long.parseLong;
import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static ticketservice.exception.CoreErrorCode.BANK_CARD_NOT_FOUND;
import static ticketservice.exception.CoreErrorCode.EVENT_ALREADY_STARTED;
import static ticketservice.exception.CoreErrorCode.EVENT_NOT_FOUND;
import static ticketservice.exception.CoreErrorCode.INSUFFICIENT_FOUNDS;
import static ticketservice.exception.CoreErrorCode.SEAT_ALREADY_TAKEN;
import static ticketservice.exception.CoreErrorCode.SEAT_NOT_FOUND;
import static ticketservice.util.StringUtils.toLongId;

@Service
@Transactional(propagation = MANDATORY)
public class ValidatorService {

    public ReserveDetails validateReserveAndGetDetails(UserEntity user, Supplier<EventDetailResponse> eventDetailSupplier, Supplier<EventListResponse> eventsSupplier,
                                                       Long eventId, Long seatId, Long cardId) {
        UserBankCardEntity bankCard = validateAndGetBankCard(user, cardId);
        EventDetailResponse eventDetail = eventDetailSupplier.get();
        SeatInfo seatInfo = validateAndGetSeat(seatId, eventDetail);
        if (seatInfo.getPrice() > bankCard.getAmount()) {
            throw new ReservationException(INSUFFICIENT_FOUNDS.getErrorCode());
        }
        EventListResponse eventListResponse = eventsSupplier.get();
        Event event = eventListResponse.getData().stream()
                .filter(e -> e.getEventId().equals(eventId))
                .findAny()
                .orElseThrow(() -> new ReservationException(EVENT_NOT_FOUND.getErrorCode()));
        OffsetDateTime startTime = OffsetDateTime.of(ofEpochSecond(parseLong(event.getStartTimeStamp()), 0, UTC), UTC);
        if (startTime.isBefore(LocalDateTime.now().atOffset(UTC))) {
            throw new ReservationException(EVENT_ALREADY_STARTED.getErrorCode());
        }
        return ReserveDetails.builder()
                .bankCard(bankCard)
                .event(event)
                .seatInfo(seatInfo)
                .build();
    }

    private UserBankCardEntity validateAndGetBankCard(UserEntity user, Long cardId) {
        return user.getBankCards().stream()
                .filter(userBankCardEntity -> toLongId(userBankCardEntity.getCardId()).equals(cardId))
                .findAny()
                .orElseThrow(() -> new ReservationException(BANK_CARD_NOT_FOUND.getErrorCode()));
    }

    private SeatInfo validateAndGetSeat(Long seatId, EventDetailResponse event) {
        SeatInfo seatInfo = event.getData().getSeats().stream()
                .filter(seat -> toLongId(seat.getId()).equals(seatId))
                .findAny()
                .orElseThrow(() -> new ReservationException(SEAT_NOT_FOUND.getErrorCode()));
        if (TRUE.equals(seatInfo.getReserved())) {
            throw new ReservationException(SEAT_ALREADY_TAKEN.getErrorCode());
        }
        return seatInfo;
    }

}
