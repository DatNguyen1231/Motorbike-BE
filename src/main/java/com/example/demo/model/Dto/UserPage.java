package com.example.demo.model.Dto;

import com.example.demo.model.entity.DAOUser;
import com.example.demo.model.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserPage {
    private List<UserDTO> userDTOS;
    private int page;
    private int size;
    private long totalElements;
    private long totalPages;

    public UserPage(Page<DAOUser> users){this.userDTOS=getUserDto(users);
       this.page=users.getNumber();
       this.size=users.getSize();
       this.totalElements=users.getTotalElements();
       this.totalPages=users.getTotalPages();
   }
    private static List<UserDTO> getUserDto(Page<DAOUser> users) {
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }
}
