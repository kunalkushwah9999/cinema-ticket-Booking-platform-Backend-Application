package com.project.BookMyShowApp.init;

import com.project.BookMyShowApp.model.Role;
import com.project.BookMyShowApp.model.User;
import com.project.BookMyShowApp.repository.RoleRepository;
import com.project.BookMyShowApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inject admin details from application.properties
    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;


    @Override
    public void run(String... args) throws Exception {

        Role userRole = null;
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_USER"));
        }
        else {
            userRole = roleRepository.findByName("ROLE_USER").get();
        }

        if (roleRepository.findByName("ROLE_THEATER_ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_THEATER_ADMIN"));
        }

        Role adminRole = null;
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
        } else {
            adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        }

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User adminUser = new User();
            adminUser.setName(adminUsername);
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode(adminPassword)); // Always encode passwords!
            adminUser.setPhoneNumber("6263128466");
            // Assign the admin role to the user
            adminUser.setRoles(Set.of(adminRole, userRole)); // Assumes User entity has a Set<Role> field

            userRepository.save(adminUser);
            System.out.println(" Admin user created successfully!");
        } else {
            System.out.println(" Admin user already exists, skipping creation.");

        }
    }
}