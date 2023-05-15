package ru.jekajops.quadcopterbot.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TgClient")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {
    @Id
    @GeneratedValue
    Long id;
    String name;
    String phone;
    String tgLink;
    Boolean isNew;
}
