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
@Table(name = "TgAdmin")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Admin {
    @Id
    Long id;
    String name;
    Long chatId;
}
