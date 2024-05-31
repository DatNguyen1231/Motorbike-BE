package com.example.demo.repositories;

import com.example.demo.model.entity.TypeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeProductRepository extends JpaRepository<TypeProduct, Long> {

    @Query("select (count(t) > 0) from TypeProduct t where t.nameType = ?1")
    boolean existsByNameType(String nameType);

}
