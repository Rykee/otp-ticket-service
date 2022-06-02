package ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @SequenceGenerator(name = "UsersSeqGen", sequenceName = "USERS_S", initialValue = 1000, allocationSize = 1000)
    @GeneratedValue(strategy = SEQUENCE, generator = "UsersSeqGen")
    @Column(name = "USER_ID", unique = true, nullable = false)
    Long id;

    @Column(name = "NAME", nullable = false)
    String name;

    @Column(name = "EMAIL", nullable = false)
    String email;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    List<DeviceHashEntity> deviceHashes;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    List<UserTokenEntity> tokens;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    List<UserBankCardEntity> bankCards;

}


