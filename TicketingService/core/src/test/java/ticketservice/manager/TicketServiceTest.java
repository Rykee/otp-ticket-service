package ticketservice.manager;

import hu.matray.partner.endpoint.PartnerFeignEndpoint;
import hu.matray.partner.endpoint.exception.PartnerException;
import hu.matray.partner.springbootserverapi.model.EventDetailResponse;
import hu.matray.partner.springbootserverapi.model.EventListResponse;
import hu.matray.partner.springbootserverapi.model.ReservationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ticketservice.exception.CoreErrorCode;
import ticketservice.exception.CoreException;
import ticketservice.exception.ExceptionMapper;

import static hu.matray.partner.endpoint.exception.PartnerErrorCode.EVENT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private PartnerFeignEndpoint partnerFeignClient;

    @Mock
    private ExceptionMapper exceptionMapper;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void getEvent() {
        //GIVEN
        Long eventId = 1L;
        EventDetailResponse response = new EventDetailResponse();
        when(partnerFeignClient.getEvent(eventId)).thenReturn(ResponseEntity.ok(response));

        //WHEN
        assertThatCode(() -> ticketService.getEvent(eventId))
                .doesNotThrowAnyException();

        //THEN
        verify(partnerFeignClient).getEvent(eventId);
    }

    @Test
    void getEvent_Exception() {
        //GIVEN
        Long eventId = 1L;
        PartnerException partnerException = new PartnerException(400, EVENT_NOT_FOUND);
        when(partnerFeignClient.getEvent(eventId)).thenThrow(partnerException);
        when(exceptionMapper.map(partnerException)).thenReturn(new CoreException(400, CoreErrorCode.EVENT_NOT_FOUND));

        //WHEN
        assertThatThrownBy(() -> ticketService.getEvent(eventId))
                //THEN
                .isInstanceOf(CoreException.class);

        verify(partnerFeignClient).getEvent(eventId);
    }

    @Test
    void getEvents() {
        //GIVEN
        when(partnerFeignClient.getEvents()).thenReturn(ResponseEntity.ok(new EventListResponse()));

        //WHEN

        assertThatCode(() -> ticketService.getEvents())
                .doesNotThrowAnyException();

        verify(partnerFeignClient).getEvents();
    }

    @Test
    void getEvents_Exception() {
        //GIVEN
        PartnerException partnerException = new PartnerException(400, EVENT_NOT_FOUND);
        when(partnerFeignClient.getEvents()).thenThrow(partnerException);
        when(exceptionMapper.map(partnerException)).thenReturn(new CoreException(400, CoreErrorCode.EVENT_NOT_FOUND));

        //WHEN
        assertThatThrownBy(() -> ticketService.getEvents())
                //THEN
                .isInstanceOf(CoreException.class);

        verify(partnerFeignClient).getEvents();
    }

    @Test
    void reserve() {
        //GIVEN
        Long eventId = 1L, seatId = 2L;
        when(partnerFeignClient.reserve(eventId, seatId)).thenReturn(ResponseEntity.ok(new ReservationResponse()));

        //WHEN
        assertThatCode(() -> ticketService.reserve(eventId, seatId))
                //THEN
                .doesNotThrowAnyException();

        verify(partnerFeignClient).reserve(eventId, seatId);
    }

}