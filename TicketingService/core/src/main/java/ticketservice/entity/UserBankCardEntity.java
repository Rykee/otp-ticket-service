package ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "USER_BANK_CARD")
public class UserBankCardEntity {

    @Id
    @SequenceGenerator(name = "UserBankCardGen", sequenceName = "USER_BANK_CARD_S", initialValue = 1000, allocationSize = 1000)
    @GeneratedValue(strategy = SEQUENCE, generator = "UserBankCardGen")
    @Column(name = "ID", unique = true, nullable = false)
    Long id;

    @Column(name = "CARD_ID", nullable = false)
    String cardId;

    @Column(name = "CARD_NUMBER", nullable = false)
    String cardNumber;

    @Column(name = "CVC", nullable = false)
    String cvc;

    @Column(name = "NAME", nullable = false)
    String nameOnCard;

    @Column(name = "AMOUNT", nullable = false)
    long amount;

    @Column(name = "CURRENCY", nullable = false)
    String currency;

}
