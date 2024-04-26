package com.example.demo;

//import com.example.demo.entity.*;

import com.example.demo.Util.PasswordGenerator;
import com.example.demo.model.entity.DAOUser;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.TypeProduct;
import com.example.demo.repositories.*;

import com.example.demo.Util.GmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
//@EnableScheduling
public class DemoApplication {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    TypeProducRepository typeProductRepository;
    @Autowired
    StatusShoppingCartRepository statusRepository;
    @Autowired
    ProductRepository repository;
    //private static final Logger logger = Logger.getLogger(DemoApplication.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    static GmailService gmailService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    //thêm dữ liệu test
    //thêm 1 lần r tắt
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
//   //phải thêm dầu tiên người dùng k tự thêm được
//        System.out.println("hihi");
//        AddRole("ADMIN");
//        AddRole("USER");
//       System.out.println("Đã thêm role ");
//
//
//        addUserAdmin();
//        System.out.println("đã thêm tk admin");
//
//        //trường này người dùng tự thêm
//        addType("xe may");
//        addType("Phụ tùng ");
//        System.out.println("Đã thêm type product ");
    }

    void addUserAdmin() {
        DAOUser user = new DAOUser();
        user.setUsername("test");
        user.setPassword(bcryptEncoder.encode("test"));
        user.setEmail("kcosten1011@gmail.com");
        Role role = new Role();
        role.setId(1L);
        user.setRole(role);
        userRepository.save(user);
    }

    private void addType(String type) {
        TypeProduct typeProduct = new TypeProduct();
        typeProduct.setNameType(type);
        typeProductRepository.save(typeProduct);
    }

    private void AddRole(String role) {
        Role addRole = new Role();
        addRole.setRole(role);
        roleRepository.save(addRole);
    }
}