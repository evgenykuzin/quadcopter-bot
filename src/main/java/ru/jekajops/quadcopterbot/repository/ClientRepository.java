package ru.jekajops.quadcopterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jekajops.quadcopterbot.models.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
