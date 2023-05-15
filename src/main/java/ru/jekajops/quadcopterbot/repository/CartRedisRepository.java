package ru.jekajops.quadcopterbot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jekajops.quadcopterbot.models.Cart;

//@Repository
public interface CartRedisRepository extends CrudRepository<Cart, Long> {
}
