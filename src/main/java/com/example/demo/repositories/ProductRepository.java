package com.example.demo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTypeProduct_NameTypeContainingIgnoreCase(String partialName);

    //search by idTypeProduct
    @Query("SELECT p FROM Product p WHERE p.typeProduct.id = :id OR :id IS NULL")
    Page<Product> findByTypeProduct_Id(@Param("id") Long id, Pageable pageable);


    //search by DetailType
    Page<Product> findByDetailType(Long detailType, Pageable pageable);

    //Search by DetailsType high to low
    @Query("select p from Product p where p.detailType = ?1  ORDER BY p.price DESC")
    Page<Product> findByDetailTypeOrderByPriceDesc(Long detailType, Pageable pageable);

    //Search by DetailType high to low
    @Query("select p from Product p where p.detailType = ?1  ORDER BY p.price ASC")
    Page<Product> findByDetailTypeOrderByPriceAsc(Long detailType, Pageable pageable);

    //search by TypeProduct_Id high đến low
    @Query("select p from Product p where p.typeProduct.id = ?1 ORDER BY p.price DESC")
    Page<Product> findByTypeProduct_IdOrderByPriceDesc(Long id, Pageable pageable);

    //search by TypeProduct_Id cao low đến high
    @Query("select p from Product p where p.typeProduct.id = ?1 ORDER BY p.price ASC")
    Page<Product> findByTypeProduct_IdOrderByPriceAsc(Long id, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> findByName(String name, Pageable pageable);

    @Query("select p from Product p where p.typeProduct.id = ?1or p.typeProduct.id = ?2")
    Page<Product> findByTypeProduct_IdAndTypeProduct_Id(Long id, @Nullable Long id1, Pageable pageable);

}
