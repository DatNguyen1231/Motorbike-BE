package com.example.demo.service;

import com.example.demo.model.Dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<?> addProduct(ProductDto dto);

    ResponseEntity<?> getByIdType(int page, int size, Long idType);

    ResponseEntity<?> getTypeDetail(int page, int size, Long detailType);

    ResponseEntity<?> getDetail(Long idProduct);

    ResponseEntity<?> getTypeDetailArrange(int page, int size, Long detailType, String arrange);

    ResponseEntity<?> getByTypeProduct_IdArrange(int page, int size, Long typeProduct_Id, String arrange);

    ResponseEntity<?> deleteProduct(long id);

    ResponseEntity<?> deleteMultipleProducts(List<Long> ids);

    ResponseEntity<?> putProduct(long id, ProductDto productDto);

    ResponseEntity<?> findByNameProduct(int page, int size, String nameProduct);

}
