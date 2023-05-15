package ru.jekajops.quadcopterbot.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ClientOrder")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue
    Long id;
    Long chatId;
    @OneToOne
    @JoinColumn(name = "cart_id")
    Cart cart;
    BigDecimal paySum;
    Status status;

    public static Order createNew(Long chatId, Cart cart, BigDecimal paySum) {
        return new Order(null, chatId, cart, paySum, Status.CREATED);
    }

    public enum Status {
        CREATED,
        ACCEPTED,
        REJECTED,
        PAYED,
        DONE
    }

    public static Order withId(Long id) {
        return new Order(id, null, null, null, null);
    }
}
