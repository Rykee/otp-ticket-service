package hu.matray.partner.manager;

import hu.matray.partner.configuration.AppConfig;
import hu.matray.partner.endpoint.exception.PartnerException;
import hu.matray.partner.endpoint.exception.ReservationException;
import hu.matray.partner.springbootserverapi.model.EventDetail;
import hu.matray.partner.springbootserverapi.model.EventDetailResponse;
import hu.matray.partner.springbootserverapi.model.EventListResponse;
import hu.matray.partner.springbootserverapi.model.ReservationResponse;
import hu.matray.partner.springbootserverapi.model.SeatInfo;
import hu.matray.partner.util.JsonReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hu.matray.partner.endpoint.exception.PartnerErrorCode.EVENT_NOT_FOUND;
import static hu.matray.partner.endpoint.exception.PartnerErrorCode.SEAT_ALREADY_TAKEN;
import static hu.matray.partner.endpoint.exception.PartnerErrorCode.SEAT_NOT_FOUND;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class PartnerManagerTest {

    public static final String EVENT_PATH = "/getEvent{id}.json";
    @Mock
    private JsonReader jsonReader;

    @Mock
    private AppConfig appConfig;

    @InjectMocks
    private PartnerManager partnerManager;

    @Test
    void getEvents() {
        //GIVEN
        String filePath = "path";
        when(appConfig.getGetEventsJsonPath()).thenReturn(filePath);

        //WHEN
        assertThatCode(() -> partnerManager.getEvents())
                //THEN
                .doesNotThrowAnyException();

        verify(jsonReader).parseFile(filePath, EventListResponse.class);
    }

    @Test
    void getEvent() {
        //GIVEN
        long eventId = 1L;
        when(appConfig.getGetEventJsonPathPattern()).thenReturn(EVENT_PATH);
        ArgumentCaptor<String> filePathCaptor = ArgumentCaptor.forClass(String.class);

        //WHEN
        assertThatCode(() -> partnerManager.getEvent(eventId))
                //THEN
                .doesNotThrowAnyException();

        verify(jsonReader).parseFile(filePathCaptor.capture(), eq(EventDetailResponse.class));
    }

    @Test
    void getEvent_NotFound() {
        //GIVEN
        long eventId = 10L;
        when(appConfig.getGetEventJsonPathPattern()).thenReturn(EVENT_PATH);

        //WHEN
        assertThatThrownBy(() -> partnerManager.getEvent(eventId))
                //THEN
                .isInstanceOfSatisfying(PartnerException.class, e -> {
                    assertEquals(NOT_FOUND.value(), e.getStatusCode());
                    assertEquals(EVENT_NOT_FOUND.getErrorCode(), e.getPartnerError().getErrorCode());
                });
    }

    @Test
    void reserve() {
        //GIVEN
        long eventId = 1L;
        long seatId = 2L;
        when(appConfig.getGetEventJsonPathPattern()).thenReturn(EVENT_PATH);
        ArgumentCaptor<String> filePathCaptor = ArgumentCaptor.forClass(String.class);
        when(jsonReader.parseFile(anyString(), any())).thenReturn(createEventDetailResponse(eventId, seatId, false));

        //WHEN
        ReservationResponse response = partnerManager.reserve(eventId, seatId);

        //THEN
        verify(jsonReader).parseFile(filePathCaptor.capture(), eq(EventDetailResponse.class));
        assertTrue(response.getSuccess());
        assertNotNull(response.getReserver());
        assertNull(response.getErrorCode());
    }

    @Test
    void reserve_EventNotFound() {
        //GIVEN
        long eventId = 10L;
        when(appConfig.getGetEventJsonPathPattern()).thenReturn(EVENT_PATH);

        //WHEN
        assertThatThrownBy(() -> partnerManager.reserve(eventId, 2L))
                //THEN
                .isInstanceOfSatisfying(ReservationException.class, e ->
                        assertEquals(EVENT_NOT_FOUND.getErrorCode(), e.getErrorCode()));
    }

    @Test
    void reserve_SeatNotFound() {
        //GIVEN
        long eventId = 1L;
        long seatId = 2L;
        when(appConfig.getGetEventJsonPathPattern()).thenReturn(EVENT_PATH);
        when(jsonReader.parseFile(anyString(), any()))
                .thenReturn(createEventDetailResponse(eventId, seatId + 2, false));

        //WHEN
        assertThatThrownBy(() -> partnerManager.reserve(eventId, seatId))
                //THEN
                .isInstanceOfSatisfying(ReservationException.class, e ->
                        assertEquals(SEAT_NOT_FOUND.getErrorCode(), e.getErrorCode()));
    }

    @Test
    void reserve_AlreadyReserved() {
        //GIVEN
        long eventId = 1L;
        long seatId = 2L;
        when(appConfig.getGetEventJsonPathPattern()).thenReturn(EVENT_PATH);
        when(jsonReader.parseFile(anyString(), any()))
                .thenReturn(createEventDetailResponse(eventId, seatId, true));

        //WHEN
        assertThatThrownBy(() -> partnerManager.reserve(eventId, seatId))
                //THEN
                .isInstanceOfSatisfying(ReservationException.class, e ->
                        assertEquals(SEAT_ALREADY_TAKEN.getErrorCode(), e.getErrorCode()));
    }

    private EventDetailResponse createEventDetailResponse(Long eventId, Long seatId, boolean reserved) {
        return EventDetailResponse.builder()
                .data(EventDetail.builder()
                        .eventId(eventId)
                        .seats(singletonList(SeatInfo.builder()
                                .id("S" + seatId)
                                .reserved(reserved)
                                .build()))
                        .build())
                .build();
    }
}