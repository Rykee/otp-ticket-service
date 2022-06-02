package ticketservice.manager;

import hu.matray.ticket.springbootserverapi.model.ReservationResponse;
import hu.matray.ticket.springbootserverapi.model.SeatInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ticketservice.entity.UserBankCardEntity;
import ticketservice.entity.UserEntity;
import ticketservice.model.ReserveDetails;
import ticketservice.repository.UserRepository;
import ticketservice.validation.ValidatorService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketManagerTest {

    private static final long START_BALANCE = 1500L;
    private static final int TICKET_PRICE = 500;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketService ticketService;

    @Mock
    private ValidatorService validatorService;

    @InjectMocks
    private TicketManager ticketManager;

    @Test
    void getEvent() {
        //WHEN
        ticketManager.getEvent(1L);

        //THEN
        verify(ticketService).getEvent(1L);
    }

    @Test
    void getEvents() {
        //WHEN
        ticketManager.getEvents();

        //THEN
        verify(ticketService).getEvents();
    }

    @Test
    void reserve() {
        //GIVEN
        String userToken = "token";
        Long eventId = 1L;
        Long seatId = 1L;
        Long cardId = 1L;
        UserEntity user = createUser();
        when(userRepository.findByTokensToken(userToken)).thenReturn(Optional.of(user));
        ReserveDetails reserveDetails = createReserveDetails();
        when(validatorService.validateReserveAndGetDetails(eq(user), any(), any(), eq(eventId), eq(seatId), eq(cardId)))
                .thenReturn(reserveDetails);
        when(ticketService.reserve(eventId, seatId)).thenReturn(ReservationResponse.builder()
                .success(true)
                .build());

        //WHEN
        assertThatCode(() -> ticketManager.reserve(userToken, eventId, seatId, cardId))
                //THEN
                .doesNotThrowAnyException();

        verify(userRepository).findByTokensToken(userToken);
        verify(validatorService).validateReserveAndGetDetails(eq(user), any(),
                any(), eq(eventId), eq(seatId), eq(cardId));
        assertEquals(START_BALANCE - TICKET_PRICE, reserveDetails.getBankCard().getAmount());
    }

    @Test
    void reserve_unsuccessful() {
        //GIVEN
        String userToken = "token";
        Long eventId = 1L;
        Long seatId = 1L;
        Long cardId = 1L;
        UserEntity user = createUser();
        when(userRepository.findByTokensToken(userToken)).thenReturn(Optional.of(user));
        ReserveDetails reserveDetails = createReserveDetails();
        when(validatorService.validateReserveAndGetDetails(eq(user), any(), any(), eq(eventId), eq(seatId), eq(cardId)))
                .thenReturn(reserveDetails);
        when(ticketService.reserve(eventId, seatId)).thenReturn(ReservationResponse.builder()
                .success(false)
                .build());

        //WHEN
        assertThatCode(() -> ticketManager.reserve(userToken, eventId, seatId, cardId))
                //THEN
                .doesNotThrowAnyException();

        verify(userRepository).findByTokensToken(userToken);
        verify(validatorService).validateReserveAndGetDetails(eq(user), any(), any(), eq(eventId), eq(seatId), eq(cardId));
        assertEquals(START_BALANCE, reserveDetails.getBankCard().getAmount());
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .id(1L)
                .build();
    }

    private ReserveDetails createReserveDetails() {
        return ReserveDetails.builder()
                .bankCard(UserBankCardEntity.builder()
                        .amount(START_BALANCE)
                        .build())
                .seatInfo(SeatInfo.builder()
                        .price(TICKET_PRICE)
                        .build())
                .build();
    }

}