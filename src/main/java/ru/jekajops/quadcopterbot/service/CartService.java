package ru.jekajops.quadcopterbot.service;

import org.springframework.stereotype.Service;
import ru.jekajops.quadcopterbot.models.Cart;
import ru.jekajops.quadcopterbot.models.Order;
import ru.jekajops.quadcopterbot.models.Product;
import ru.jekajops.quadcopterbot.repository.CartRepository;
import ru.jekajops.quadcopterbot.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class CartService implements DataService<Cart, Long, CartRepository> {
    private final CartRepository cartRepository;
    private final ProductService productService;
    //private final CartRedisRepository cartCash;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        // this.cartRepository = cartRepository;
        //this.cartCash = cartCash;
        this.productService = productService;
    }

    public void addToCart(Long productId, Integer amount, Long chatId) {
        Product product = Product.withId(productId);
        Cart cart = getCartByChatId(chatId).orElse(Cart.builder()
                .products(new ArrayList<>())
                .productsAmount(amount)
                .chatId(chatId)
                        .build());
        //cart.getProducts().stream().map(Product::getId).forEach(i -> productService.);
        //cartCash.save(cart);
        cartRepository.saveAndFlush(cart);
    }

    public void deleteFromCart(Long productId, Long chatId) {
        Product product = productService.getReferenceById(productId);
        Optional<Cart> cart = getCartByChatId(chatId);
        if (cart.isPresent()) {
            cart.get().getProducts().remove(product);
            cartRepository.saveAndFlush(cart.get());
        }
    }

    public void clearCart(Long chatId) {
        cartRepository.deleteCartByChatId(chatId);
    }

    public void executeCart(Long chatId, Consumer<Cart> execFunc) {
        getCartByChatId(chatId).ifPresent(execFunc);
    }

    public List<Cart> getCartsByChatId(Long chatId) {
        return cartRepository.findCartByChatId(chatId);
        //return null;
    }

    public Optional<Cart> getCartByChatId(Long chatId) {
        return cartRepository.findCartByChatId(chatId).stream().findFirst();
       // return cartCash.findById(chatId);

    }

    @Override
    public CartRepository repository() {
        return cartRepository;
    }

}
