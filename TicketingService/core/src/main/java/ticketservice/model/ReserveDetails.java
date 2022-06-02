package ticketservice.model;

import hu.matray.ticket.springbootserverapi.model.Event;
import hu.matray.ticket.springbootserverapi.model.SeatInfo;
import lombok.Builder;
import lombok.Getter;
import ticketservice.entity.UserBankCardEntity;

@Builder
@Getter
public class ReserveDetails {

    private UserBankCardEntity bankCard;
    private SeatInfo seatInfo;
    private Event event;

}
