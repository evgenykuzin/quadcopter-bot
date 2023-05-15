package ru.jekajops.quadcopterbot.models;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer amount;
}
