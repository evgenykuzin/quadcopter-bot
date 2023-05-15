package ru.jekajops.quadcopterbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.jekajops.quadcopterbot.dto.ProductRq;
import ru.jekajops.quadcopterbot.exceptions.ProcessException;
import ru.jekajops.quadcopterbot.models.Cart;
import ru.jekajops.quadcopterbot.models.Order;
import ru.jekajops.quadcopterbot.models.Product;
import ru.jekajops.quadcopterbot.service.CartService;
import ru.jekajops.quadcopterbot.service.OrderService;
import ru.jekajops.quadcopterbot.service.ProductService;
import ru.jekajops.quadcopterbot.util.Callback;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static ru.jekajops.quadcopterbot.util.transformers.TGTransformer.*;

@Component
public class TelegramBotApplication extends TelegramBot {
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ApplicationContext applicationContext;
    private final Long adminId = 328018558L;

    public static final ReplyKeyboardMarkup MENU_KEYBOARD = new ReplyKeyboardMarkup(
            new String[]{"О нас", "Корзина"},
            new String[]{"Каталог"})
            .resizeKeyboard(true);

    @lombok.Builder
    public TelegramBotApplication(BotProperties botProperties, ProductService productService, CartService cartService, OrderService orderService, ApplicationContext applicationContext) {
        super(botProperties.getToken());
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void run() {
        this.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        try {
            Message message = update.message();
            if (message != null) {
                Long chatId = Optional.ofNullable(message.chat()).map(Chat::id).orElse(328018558L);
                System.out.println("chatId = " + chatId);
                System.out.println(message.chat().firstName());
                System.out.println(message.chat().username());
                if (message.text() != null) {
                    String commandName = message.text();
                    if (commandName != null) {
                        this.serveCommand(commandName, chatId);
                    }
                }
            } else {
                CallbackQuery callbackQuery = update.callbackQuery();
                if (callbackQuery != null) {
                    serveCallback(callbackQuery);
                }
            }
        }
        catch (ProcessException pe) {
            pe.printStackTrace();
            execute(new SendMessage(adminId, pe.getStatusType().getDesc()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void serveCallback(CallbackQuery callbackQuery) throws ProcessException {
        String data = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();
        String[] dataSplit = data.split("::");
        Callback operation = Callback.valueOf(dataSplit[0]);
        String args = dataSplit.length > 1 ? dataSplit[1] : null;
        switch (operation) {
            case ADD_CART: {
                if (args == null) break;
                String productId = args;
                cartService.addToCart(Long.valueOf(productId), 1, chatId);
                SendMessage response = new SendMessage(chatId, "Товар добавлен в корзину");
                this.execute(response);
                break;
            }
            case DELETE_CART: {
                if (args == null) break;
                String productId = args;
                cartService.deleteFromCart(Long.valueOf(productId), chatId);
                SendMessage response = new SendMessage(chatId, "Товар удален из корзины");
                this.execute(response);
                break;
            }
            case ACCEPT_ORDER: {
                if (args == null) break;
                String orderId = args;
                Order order = orderService.getReferenceById(Long.valueOf(args));
                orderService.acceptOrder(order);
//                Cart cart = cartService.getById(order.getCart().getId());
//                List<Product> products = cart.getProductOrders().stream().map(po -> {
//                    Product p = productService.getById(po.getProduct().getId());
//                    p.setStocks(p.getStocks()-po.getAmount());
//                    return p;
//                }).collect(Collectors.toList());
//                productService.saveAll(products);
                SendMessage response = new SendMessage(chatId, "Заказ подтвержден");
                this.execute(response);
                break;
            }
            case REJECT_ORDER: {
                if (args == null) break;
                String orderId = args;
                Order order = Order.withId(Long.valueOf(args));
                orderService.rejectOrder(order);
//                Cart cart = cartService.getById(order.getCart().getId());
//                List<Product> products = cart.getProductOrders().stream().map(po -> {
//                    Product p = productService.getById(po.getProduct().getId());
//                    p.setStocks(p.getStocks()+po.getAmount());
//                    return p;
//                }).collect(Collectors.toList());
//                productService.saveAll(products);
                SendMessage response = new SendMessage(chatId, "Заказ отклонен");
                this.execute(response);
                break;
            }
        }
    }

    private void serveCommand(String commandName, Long chatId) throws ProcessException {
        switch (commandName) {
            case "/start": {
                SendMessage response = new SendMessage(chatId, "Добро пожаловать! Я Armyseller bot. Наберите /menu , чтобы открылась клавиатура");
                this.executeWithMenu(response);
                break;
            }
            case "/menu":
            case "Назад": {
                SendMessage response = new SendMessage(chatId, "Вы в главном меню");
                this.executeWithMenu(response);
                break;
            }
            case "О нас": {
                SendMessage response = new SendMessage(chatId, "Мы компания armyseller. 10 лет на рынке, лучшее качество, только у нас, не пропусти! бронижелеты, дроны, ткани! покупаем покупаем! бронижелеты, дроны, ткани!");
                this.executeWithMenu(response);
                break;
            }
            case "Корзина": {
                List<Cart> cart = cartService.getCartsByChatId(chatId);
                String text;
                if (cart.isEmpty()) {
                    text = "Пока пусто, зайдите в каталог.";
                } else {
                    text = "Ваша корзина";
                }
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(new KeyboardButton("Создать заявку"), new KeyboardButton("Назад"));
                SendMessage response = new SendMessage(chatId, text)
                        .replyMarkup(replyKeyboardMarkup);
                this.execute(response);
                List<Long> productIds = cart.stream()
                        .map(Cart::getProducts)
                        .flatMap(Collection::stream)
                        .map(Product::getId)
                        .collect(Collectors.toList());
                System.out.println(productService.getAllProducts());
                System.out.println(productIds);
                List<ProductRq> productRqs = productService.getAllByIds(productIds);
                System.out.println(productRqs);
                productRqs.stream().map(p -> toOrderProduct(chatId, p)).forEach(
                        this::execute
                );
                break;
            }
            case "Каталог": {
                List<SendPhoto> productsSendPhotos = productService.getAllProducts()
                        .getProducts()
                        .stream()
                        .filter(Objects::nonNull)
                        //.filter(p->p.getStocks() > 0)
                        .map(p -> toVitrineProduct(chatId, p))
                        .collect(Collectors.toList());
                if (productsSendPhotos.isEmpty()) {
                    SendMessage response = new SendMessage(chatId, "К сожалению товара еще нет в наличии.");
                    executeWithMenu(response);
                    break;
                }
                productsSendPhotos.forEach(this::execute);
                break;
            }
            case "Создать заявку": {
                Optional<Cart> cart = cartService.getCartByChatId(chatId);

                if (cart.isPresent()) {
                    List<Long> productIds = cart.stream()
                            .map(Cart::getProducts)
                            .flatMap(Collection::stream)
                            .map(Product::getId)
                            .collect(Collectors.toList());
                    List<ProductRq> productRqs = productService.getAllByIds(productIds);
                    BigDecimal paySum = BigDecimal.valueOf(productRqs.stream().mapToInt(ProductRq::getPrice).sum());
                    Order order = Order.createNew(chatId, cart.get(), paySum);
                    orderService.saveOrder(order);

                    //send order to admin
                    this.execute(new SendMessage(adminId, "Новый заказ: " + order.getId()));
                    productRqs.stream().map(p -> toVitrineProduct(adminId, p)).forEach(
                            this::execute
                    );
                    this.execute(toOrderAccept(adminId, order));
                    //send success msg to client
                    SendMessage response = new SendMessage(chatId, "Заявка отправлена");
                    this.executeWithMenu(response);
                }
                break;
            }
            case "/help": {
                SendMessage response = new SendMessage(chatId, "Наберите /menu , чтобы открылась клавиатура");
                this.executeWithMenu(response);
                break;
            }
            default: {
                Random r = new Random();
                String text = r.nextBoolean() ? "Не понял, че?" : "Говори четко сука че тебе надо???";
                SendMessage response = new SendMessage(chatId, text);
                this.executeWithMenu(response);
                break;
            }
        }
    }

    private void executeWithMenu(SendMessage request) {
        this.execute(request.replyMarkup(MENU_KEYBOARD));
    }
}