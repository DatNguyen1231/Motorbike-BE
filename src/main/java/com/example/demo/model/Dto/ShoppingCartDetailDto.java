package com.example.demo.model.Dto;


import com.example.demo.model.entity.ShoppingCartDetail;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
public class ShoppingCartDetailDto {

    private Long id;
    private Date dateCreated;
    private long quantityCart;
    private ProductSomeReponseDto productSomeReponseDto;

    public ShoppingCartDetailDto(ShoppingCartDetail shoppingCartDetail) {
        this.id = shoppingCartDetail.getId();
        this.dateCreated = shoppingCartDetail.getDateCreated();
        this.quantityCart = shoppingCartDetail.getQuantityCart();
        this.productSomeReponseDto = new ProductSomeReponseDto(shoppingCartDetail.getProduct());
    }
}
