package com.fit.ntu.electronics.config;

import com.fit.ntu.electronics.model.User;
import com.fit.ntu.electronics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin123");
            admin.setFirstName("Admin");
            admin.setLastName("System");
            admin.setRole("ADMIN");
            userRepository.save(admin);

            User user = new User();
            user.setEmail("user@gmail.com");
            user.setPassword("user123");
            user.setFirstName("Md");
            user.setLastName("Rimel");
            user.setRole("USER");
            userRepository.save(user);
        }
    }
}