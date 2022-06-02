package ticketservice.validation;

import hu.matray.partner.endpoint.exception.ReservationException;
import hu.matray.ticket.springbootserverapi.model.Event;
import hu.matray.ticket.springbootserverapi.model.EventDetail;
import hu.matray.ticket.springbootserverapi.model.EventDetailResponse;
import hu.matray.ticket.springbootserverapi.model.EventListResponse;
import hu.matray.ticket.springbootserverapi.model.SeatInfo;
import org.junit.jupiter.api.Test;
import ticketservice.entity.UserBankCardEntity;
import ticketservice.entity.UserEntity;

import java.time.OffsetDateTime;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ticketservice.exception.CoreErrorCode.BANK_CARD_NOT_FOUND;
import static ticketservice.exception.CoreErrorCode.EVENT_ALREADY_STARTED;
import static ticketservice.exception.CoreErrorCode.EVENT_NOT_FOUND;
import static ticketservice.exception.CoreErrorCode.INSUFFICIENT_FOUNDS;
import static ticketservice.exception.CoreErrorCode.SEAT_ALREADY_TAKEN;
import static ticketservice.exception.CoreErrorCode.SEAT_NOT_FOUND;

class ValidatorServiceTest {

    private final ValidatorService validatorService = new ValidatorService();

    @Test
    void validate_InvalidBankCard() {
        //GIVEN
        UserEntity userEntity = UserEntity.builder()
                .bankCards(emptyList())
                .build();

        //WHEN
        assertThatThrownBy(() -> validatorService.validateReserveAndGetDetails(userEntity, () -> null, () -> null, null, null, 1L))
                .isInstanceOfSatisfying(ReservationException.class, e -> assertEquals(BANK_CARD_NOT_FOUND.getErrorCode(), e.getErrorCode()));
    }

    @Test
    void validate_InvalidSeat() {
        //GIVEN
        long cardId = 1L;
        long seatId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .bankCards(singletonList(UserBankCardEntity.builder()
                        .cardId("C00" + cardId)
                        .build()))
                .build();
        EventDetailResponse eventDetailResponse = EventDetailResponse.builder()
                .data(EventDetail.builder()
                        .seats(emptyList())
                        .build())
                .build();

        //WHEN
        assertThatThrownBy(() -> validatorService.validateReserveAndGetDetails(userEntity, () -> eventDetailResponse, () -> null, null, seatId, cardId))
                .isInstanceOfSatisfying(ReservationException.class, e -> assertEquals(SEAT_NOT_FOUND.getErrorCode(), e.getErrorCode()));
    }

    @Test
    void validate_AlreadyReserved() {
        //GIVEN
        long cardId = 1L;
        long seatId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .bankCards(singletonList(UserBankCardEntity.builder()
                        .cardId("C00" + cardId)
                        .build()))
                .build();
        EventDetailResponse eventDetailResponse = EventDetailResponse.builder()
                .data(EventDetail.builder()
                        .seats(singletonList(SeatInfo.builder()
                                .id("S0" + seatId)
                                .reserved(true)
                                .build()))
                        .build())
                .build();

        //WHEN
        assertThatThrownBy(() -> validatorService.validateReserveAndGetDetails(userEntity, () -> eventDetailResponse, () -> null, null, seatId, cardId))
                .isInstanceOfSatisfying(ReservationException.class, e -> assertEquals(SEAT_ALREADY_TAKEN.getErrorCode(), e.getErrorCode()));
    }

    @Test
    void validate_UserIsPoor() {
        //GIVEN
        long cardId = 1L;
        long seatId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .bankCards(singletonList(UserBankCardEntity.builder()
                        .cardId("C00" + cardId)
                        .amount(1000)
                        .build()))
                .build();
        EventDetailResponse eventDetailResponse = EventDetailResponse.builder()
                .data(EventDetail.builder()
                        .seats(singletonList(SeatInfo.builder()
                                .id("S0" + seatId)
                                .reserved(false)
                                .price(1500)
                                .build()))
                        .build())
                .build();

        //WHEN
        assertThatThrownBy(() -> validatorService.validateReserveAndGetDetails(userEntity, () -> eventDetailResponse, () -> null, null, seatId, cardId))
                .isInstanceOfSatisfying(ReservationException.class, e -> assertEquals(INSUFFICIENT_FOUNDS.getErrorCode(), e.getErrorCode()));
    }

    @Test
    void validate_EventMissing() {
        //GIVEN
        long cardId = 1L;
        long seatId = 1L;
        long eventId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .bankCards(singletonList(UserBankCardEntity.builder()
                        .cardId("C00" + cardId)
                        .amount(1000)
                        .build()))
                .build();

        EventDetailResponse eventDetailResponse = EventDetailResponse.builder()
                .data(EventDetail.builder()
                        .seats(singletonList(SeatInfo.builder()
                                .id("S0" + seatId)
                                .reserved(false)
                                .price(500)
                                .build()))
                        .build())
                .build();

        EventListResponse eventListResponse = EventListResponse.builder()
                .data(emptyList())
                .build();

        //WHEN
        assertThatThrownBy(() -> validatorService.validateReserveAndGetDetails(userEntity, () -> eventDetailResponse, () -> eventListResponse, eventId, seatId, cardId))
                .isInstanceOfSatisfying(ReservationException.class, e -> assertEquals(EVENT_NOT_FOUND.getErrorCode(), e.getErrorCode()));
    }

    @Test
    void validate_AlreadyStarted() {
        //GIVEN
        long cardId = 1L;
        long seatId = 1L;
        long eventId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .bankCards(singletonList(UserBankCardEntity.builder()
                        .cardId("C00" + cardId)
                        .amount(1000)
                        .build()))
                .build();

        EventDetailResponse eventDetailResponse = EventDetailResponse.builder()
                .data(EventDetail.builder()
                        .seats(singletonList(SeatInfo.builder()
                                .id("S0" + seatId)
                                .reserved(false)
                                .price(500)
                                .build()))
                        .build())
                .build();

        EventListResponse eventListResponse = EventListResponse.builder()
                .data(singletonList(Event.builder()
                        .eventId(eventId)
                        .startTimeStamp("1591109727")
                        .build()))
                .build();

        //WHEN
        assertThatThrownBy(() -> validatorService.validateReserveAndGetDetails(userEntity, () -> eventDetailResponse, () -> eventListResponse, eventId, seatId, cardId))
                .isInstanceOfSatisfying(ReservationException.class, e -> assertEquals(EVENT_ALREADY_STARTED.getErrorCode(), e.getErrorCode()));
    }

    @Test
    void validate() {
        //GIVEN
        long cardId = 1L;
        long seatId = 1L;
        long eventId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .bankCards(singletonList(UserBankCardEntity.builder()
                        .cardId("C00" + cardId)
                        .amount(1000)
                        .build()))
                .build();

        EventDetailResponse eventDetailResponse = EventDetailResponse.builder()
                .data(EventDetail.builder()
                        .seats(singletonList(SeatInfo.builder()
                                .id("S0" + seatId)
                                .reserved(false)
                                .price(500)
                                .build()))
                        .build())
                .build();

        EventListResponse eventListResponse = EventListResponse.builder()
                .data(singletonList(Event.builder()
                        .eventId(eventId)
                        .startTimeStamp(String.valueOf(OffsetDateTime.now().plusDays(2).toEpochSecond()))
                        .build()))
                .build();

        //WHEN
        assertThatCode(() -> validatorService.validateReserveAndGetDetails(userEntity, () -> eventDetailResponse, () -> eventListResponse, eventId, seatId, cardId))
                .doesNotThrowAnyException();
    }
}