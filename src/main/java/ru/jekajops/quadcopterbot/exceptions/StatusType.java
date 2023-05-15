package ru.jekajops.quadcopterbot.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusType {
    long code;
    String desc;
}
