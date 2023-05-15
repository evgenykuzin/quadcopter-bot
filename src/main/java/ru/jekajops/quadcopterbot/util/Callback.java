package ru.jekajops.quadcopterbot.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Callback {
    ADD_CART,
    DELETE_CART,
    ACCEPT_ORDER,
    REJECT_ORDER;

    public String func(Object... args) {
        return this.name() + "::" + Arrays.stream(args).map(Object::toString).collect(Collectors.joining(";"));
    }

    Callback() {
    }
}
