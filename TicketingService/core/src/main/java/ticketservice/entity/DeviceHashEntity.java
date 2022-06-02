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
@Table(name = "USER_DEVICE")
public class DeviceHashEntity {

    @Id
    @SequenceGenerator(name = "UserDeviceSeqGen", sequenceName = "USER_DEVICE_S", initialValue = 1000, allocationSize = 1000)
    @GeneratedValue(strategy = SEQUENCE, generator = "UserDeviceSeqGen")
    @Column(name = "ID", unique = true, nullable = false)
    Long id;

    @Column(name = "DEVICE_HASH")
    String deviceHash;

}
