package ru.jekajops.quadcopterbot.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRq {
    Long id;
    String name;
    Integer price;
    Integer stocks;
    String photoUrl;
    String category;
}
