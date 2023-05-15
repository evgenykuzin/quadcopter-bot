package ru.jekajops.quadcopterbot.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String productName;
    Integer price;
    Integer stocksCount;
    @Lob
    String photoUrl;
    String productCategory;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "products")
    List<Cart> carts;

    public static Product withId(Long id) {
        return new Product(id, null, null, null, null, null, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return id != null && Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
