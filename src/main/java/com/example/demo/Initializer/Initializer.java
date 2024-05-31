package com.example.demo.Initializer;


import com.example.demo.model.entity.DAOUser;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.TypeProduct;
import com.example.demo.repositories.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private final PasswordEncoder bcryptEncoder;
    private final TypeProductRepository typeProductRepository;
    private final UserRepository userRepository;

    public Initializer(RoleRepository roleRepository, PasswordEncoder bcryptEncoder, TypeProductRepository typeProductRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.bcryptEncoder = bcryptEncoder;
        this.typeProductRepository = typeProductRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!roleRepository.existsByRole("ADMIN") && !roleRepository.existsByRole("USER")) {
            AddRole("ADMIN");
            AddRole("USER");
            System.out.println("Đã thêm role ");
        }
        if (!userRepository.existsByUsername("admin")) {
            addUserAdmin("admin", "Nguyễn Quốc Đạt","admin", "kcosten101@gmail.com","0767372754");
            System.out.println("đã thêm tk admin");
        }
////        if (!typeProductRepository.existsByNameType("Xe máy") && typeProductRepository.existsByNameType("Phụ tùng")) {
//            addType("Xe máy");
//            addType("Phụ tùng");
//            System.out.println("Đã thêm type product ");
////        }
    }

    private void AddRole(String role) {
        Role addRole = new Role();
        addRole.setRole(role);
        roleRepository.save(addRole);
    }

    private void addType(String type) {
        TypeProduct typeProduct = new TypeProduct();
        typeProduct.setNameType(type);
        typeProductRepository.save(typeProduct);
    }

    void addUserAdmin(String username,String fullName, String password, String email,String phoneNumber) {
        DAOUser user = new DAOUser();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setPassword(bcryptEncoder.encode(password));
        user.setEmail(email);
        user.setStatus(0);
        user.setPhoneNumber(phoneNumber);
        Role role = new Role();
        role.setId(1L);
        user.setRole(role);
        userRepository.save(user);
    }
}
