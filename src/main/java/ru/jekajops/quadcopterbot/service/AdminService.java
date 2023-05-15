package ru.jekajops.quadcopterbot.service;

import ru.jekajops.quadcopterbot.models.Admin;
import ru.jekajops.quadcopterbot.repository.AdminRepository;

public class AdminService implements DataService<Admin, Long, AdminRepository> {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public AdminRepository repository() {
        return adminRepository;
    }
}
