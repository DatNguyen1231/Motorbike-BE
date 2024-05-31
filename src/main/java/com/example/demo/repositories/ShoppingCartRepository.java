package com.example.demo.repositories;

import com.example.demo.model.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("select s from ShoppingCart s where s.user.username = ?1")
    List<ShoppingCart> findByUser_Username(String username);

    @Query("select (count(s) > 0) from ShoppingCart s where s.user.username = ?1 and s.status = ?2")
    boolean existsByUser_UsernameAndStatus(String username, int status);

    @Query("select s from ShoppingCart s where s.user.username = ?1 and s.status = ?2")
    ShoppingCart findByUsernameAndStatus(String userName, int status);


    @Query("select s from ShoppingCart s where s.user.username = ?1 and s.status = ?2")
    List<ShoppingCart> findListByUsernameAndStatus(String userName, int status);

    @Query("select (count(s) > 0) from ShoppingCart s inner join s.shoppingCartDetails shoppingCartDetails " + "where s.user.username = ?1 and shoppingCartDetails.id = ?2")
    boolean existsByUser_UsernameAndShoppingCartDetails_Id(String username, Long id);

    @Query("select s from ShoppingCart s inner join s.shoppingCartDetails shoppingCartDetails " + "where s.user.username = ?1 and shoppingCartDetails.id = ?2")
    ShoppingCart findByUser_UsernameAndShoppingCartDetails_Id(String username, Long id);

    @Query("select s from ShoppingCart s where s.user.username = ?1 and s.status = ?2")
    Optional<ShoppingCart> findByUser_UsernameAndStatus(String username, int status);
    @Query("select s from ShoppingCart s where s.user.username = ?1 and s.status = ?2")
    List<ShoppingCart> getListByUser_UsernameAndStatus(String username, int status);
    @Query("select s from ShoppingCart s where  s.status = ?1")
    List<ShoppingCart> getAllCartByStatus(int status);

    @Query("select s from ShoppingCart s where s.id = ?1 ")
    ShoppingCart findByIdCart(Long id);

    @Query("select s from ShoppingCart s where s.paymentDate BETWEEN ?1 and ?2 and s.status=?3")
    List<ShoppingCart> findByPaymentDateBetween(Date firstDay, Date lastDay, int status);

    @Query("select s from ShoppingCart s where s.paymentDate BETWEEN ?1 and ?2 ")
    List<ShoppingCart> findByPaymentDate(Date firstDay, Date lastDay);

//    @Query("SELECT scd.product.name, SUM(scd.quantityCart) " +
//            "FROM ShoppingCartDetail scd " +
//            "GROUP BY scd.product.name")
//    List<ProductQuantityProjection> test();
}
