package ru.jekajops.quadcopterbot.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@Entity
//@RedisHash("Cart")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
//    @OneToMany
//    private List<ProductOrder> productOrders;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    //@JoinColumn(name="updated_by", referencedColumnName = "id")
    @JoinTable(
            name = "carts",
            joinColumns = @JoinColumn(name = "productId"),
            inverseJoinColumns = @JoinColumn(name = "cartId"))
    private List<Product> products;
    private Integer productsAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cart cart = (Cart) o;
        return id != null && Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
