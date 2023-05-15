package ru.jekajops.quadcopterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jekajops.quadcopterbot.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
