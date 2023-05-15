package ru.jekajops.quadcopterbot.util.transformers;

import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import ru.jekajops.quadcopterbot.dto.ProductRq;
import ru.jekajops.quadcopterbot.models.Order;

import static ru.jekajops.quadcopterbot.util.Callback.*;

public class TGTransformer {
    public static SendPhoto toVitrineProduct(Long chatId, ProductRq productRq) {
        System.out.println("productRq = " + productRq);
        SendPhoto sendPhoto = new SendPhoto(chatId, productRq.getPhotoUrl());
        sendPhoto.caption(productRq.getName());
        sendPhoto.replyMarkup(new InlineKeyboardMarkup(
                new InlineKeyboardButton("в корзину")
                        .callbackData(ADD_CART.func(productRq.getId()))
        ));
        return sendPhoto;
    }//                        .url(String.format("http://localhost:8080/addcard?chatId=%s&productId=%s", chatId, productRq.getId()))
    public static SendPhoto toOrderProduct(Long chatId, ProductRq productRq) {
        System.out.println("productRq = " + productRq);
        SendPhoto sendPhoto = new SendPhoto(chatId, productRq.getPhotoUrl());
        sendPhoto.caption(productRq.getName());
        sendPhoto.replyMarkup(new InlineKeyboardMarkup(
                new InlineKeyboardButton("удалить")
                        .callbackData(DELETE_CART.func(productRq.getId()))
        ));
        return sendPhoto;
    }//

    public static SendMessage toOrderAccept(Long adminId, Order order) {
        return new SendMessage(adminId, "№" + order.getId())
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Принять заказ")
                        .callbackData(ACCEPT_ORDER.func(order.getId()))));
    }//

    public static SendMessage toOrderReject(Long adminId, Order order) {
        return new SendMessage(adminId, "№" + order.getId())
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Принять заказ")
                        .callbackData(REJECT_ORDER.func(order.getId()))));
    }//
}
