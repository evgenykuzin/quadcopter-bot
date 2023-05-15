package ru.jekajops.quadcopterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jekajops.quadcopterbot.models.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findCartByChatId(Long chatId);
    List<Long> deleteCartByChatId(Long chatId);
}
