package ru.jekajops.quadcopterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jekajops.quadcopterbot.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
