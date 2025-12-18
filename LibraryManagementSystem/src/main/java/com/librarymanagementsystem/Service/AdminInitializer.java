package com.librarymanagementsystem.Service;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.librarymanagementsystem.Entity.Categories;
import com.librarymanagementsystem.Entity.Roles;
import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Repository.CategoriesRepository;
import com.librarymanagementsystem.Repository.RolesRepository;
import com.librarymanagementsystem.Repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UsersRepository userRepository;
    private final RolesRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoriesRepository categoriesRepository;

    @Override
    public void run(String... args) {
        Roles adminRole = roleRepository.findById(1)
                .orElseGet(() -> {
                    Roles role = new Roles();
                    role.setRole("ADMIN");
                    return roleRepository.save(role);
                });
        Roles librarianRole = roleRepository.findById(2)
                .orElseGet(() -> {
                    Roles role = new Roles();
                    role.setRole("LIBRARIAN");
                    return roleRepository.save(role);
                });
        userRepository.findById("admin@gmail.com").orElseGet(() -> {
            Users admin = new Users();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setVerified(true);
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@gmail.com / password: admin");
            return admin;
        });
        createCategoryIfNotExists("Action");
        createCategoryIfNotExists("Drama");
        createCategoryIfNotExists("Thriller");
    }

    private void createCategoryIfNotExists(String name) {
    	Optional<Categories> categoryy=categoriesRepository.findByCategoryName(name);
           if (categoryy.isEmpty()) {
        	Categories category = new Categories();
            category.setCategoryName(name);;
            categoriesRepository.save(category);
            System.out.println("✅ Category created: " + name);
        }
    }

}
