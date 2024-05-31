package com.example.demo.controller;

import com.example.demo.model.Dto.ProductDto;

import com.example.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/product/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    // get by idType
    @GetMapping("/product/get-by-id-type/{page}/{size}/{idType}")
    public ResponseEntity<?> getSomeProduct(@PathVariable int page, @PathVariable int size, @PathVariable Long idType) {
        return productService.getByIdType(page, size, idType);
    }

    @GetMapping("/product/get-type-detail/{page}/{size}/{detailType}")
    public ResponseEntity<?> getTypeDetail(@PathVariable int page, @PathVariable int size, @PathVariable Long detailType) {
        return productService.getTypeDetail(page, size, detailType);
    }

    //Get by details Sort by price
    @GetMapping("/product/get-type-detail/{page}/{size}/{detailType}/{arrange}")
    public ResponseEntity<?> getTypeDetailArrange(@PathVariable int page, @PathVariable int size, @PathVariable Long detailType, @PathVariable String arrange) {
        return productService.getTypeDetailArrange(page, size, detailType, arrange);
    }

    //get by detailType Sort by price
    @GetMapping("/product/get-by-id-type/{page}/{size}/{typeProduct_Id}/{arrange}")
    public ResponseEntity<?> getByTypeProductIdArrange(@PathVariable int page, @PathVariable int size, @PathVariable Long typeProduct_Id, @PathVariable String arrange) {
        return productService.getByTypeProduct_IdArrange(page, size, typeProduct_Id, arrange);
    }

    //product details
    @GetMapping("/product/get-detail/{idProduct}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long idProduct) {
        return productService.getDetail(idProduct);
    }

    @GetMapping("/product/find-by-name-product/{page}/{size}/{nameProduct}")
    public ResponseEntity<?> findByNameProduct(@PathVariable int page, @PathVariable int size, @PathVariable String nameProduct) {
        return productService.findByNameProduct(page, size, nameProduct);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id) {
        return productService.deleteProduct(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/product/delete-multiple-products")
    public ResponseEntity<?> deleteMultipleProducts(@RequestBody List<Long> ids) {
        return productService.deleteMultipleProducts(ids);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/product/put/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody ProductDto productDto) {
        return productService.putProduct(id, productDto);
    }
}
