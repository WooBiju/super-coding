package com.github.supercoding.repository.users;

import com.github.supercoding.repository.passenger.Passenger;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id @Column(name = "user_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "user_name", length = 20)
    private String userName;

    @Column(name = "like_travel_place",length = 30)
    private String likeTravelPlace;

    @Column(name = "phone_num",length = 30)
    private String phoneNum;

    @OneToOne(mappedBy = "user") // passenger 에서 바라보는 내 필드 이름
    private Passenger passenger;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return userId != null && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
