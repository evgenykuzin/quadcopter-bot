package ru.jekajops.quadcopterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jekajops.quadcopterbot.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
