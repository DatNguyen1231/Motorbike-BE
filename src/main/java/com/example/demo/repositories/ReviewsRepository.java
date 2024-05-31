package com.example.demo.repositories;

import com.example.demo.model.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
   //List<Reviews> findByProduct_Id(Long productId);
   @Query("select r from Reviews r where r.shoppingCartDetail.product.id = ?1")
   List<Reviews> findByShoppingCartDetail_Product_Id(Long id);



   @Query("select (count(r) > 0) from Reviews r where r.status = ?1 and r.shoppingCartDetail.id = ?2")
   boolean existsByStatusAndShoppingCartDetail_Id(int status, Long id);


}
