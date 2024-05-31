package com.example.demo.service.serviceImpl;

import com.example.demo.constants.ConstantsProduct;
import com.example.demo.model.Dto.*;
import com.example.demo.model.entity.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.TypeProductRepository;
import com.example.demo.service.ProductService;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ProductImpl implements ProductService {

    private static final Logger log = LogManager.getLogger(ProductImpl.class);

    private final Messenger messenger;

    private final ProductRepository productRepository;

    private final TypeProductRepository typeProductRepository;

    public ProductImpl(Messenger messenger, ProductRepository productRepository, TypeProductRepository typeProductRepository) {
        this.messenger = messenger;
        this.productRepository = productRepository;
        this.typeProductRepository = typeProductRepository;
    }

    @Override
    public ResponseEntity<?> addProduct(ProductDto dto) {
        try {
            Product product = new Product();
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setQuantity(dto.getQuantity());
            product.setDetailType(dto.getDetailType());
            product.setImages(dto.getImages());
            product.setDiscount(dto.getDiscount());
            product.setDescribe(dto.getDescribe());
            product.setTypeProduct(typeProductRepository.findById(dto.getIdTypeProduct()).orElse(null));

            if (ObjectUtils.isEmpty(product.getTypeProduct())) {
                log.error("Failed to add product. TypeProduct is null");
                messenger.setMessenger(ConstantsProduct.TYPE_NULL);
                return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
            }
            productRepository.save(product);
            log.info("Added new product: {}", product.getName());
            messenger.setMessenger(ConstantsProduct.ADD_PRODUCT_SUCCESS);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while adding product: {}", e.getMessage());
            messenger.setMessenger(ConstantsProduct.ERROR);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getByIdType(int page, int size, Long idType) {
        try {
            //lay 1 page tu csdl
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productsPage = productRepository.findByTypeProduct_Id(idType, pageable);

            //khoi tao 1 ProductSomePageReturnDTO de Response
            ProductSomePageResponseDTO productSomePageResponseDTO = new ProductSomePageResponseDTO();
            //set du lieu cho ProductSomePageResponseDTO

            //goi ham getProductSomePageResponseDTO de lay du lieu cho setProductSomeReturns
            productSomePageResponseDTO.setProductSomeReponseDtos(getProductSomePageResponseDTO(productsPage));

            productSomePageResponseDTO.setPage(productsPage.getNumber());
            productSomePageResponseDTO.setSize(productsPage.getSize());
            productSomePageResponseDTO.setTotalElements(productsPage.getTotalElements());
            productSomePageResponseDTO.setTotalPages(productsPage.getTotalPages());

            log.info("Retrieved partial product list from page {}: successful", page);
            return new ResponseEntity<>(productSomePageResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error while getting partial product list: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getTypeDetail(int page, int size, Long detailType) {
        try {
            //lay 1 page tu csdl
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productsPage = productRepository.findByDetailType(detailType, pageable);

            //khoi tao 1 ProductSomePageReturnDTO de Response
            ProductSomePageResponseDTO productSomePageResponseDTO = new ProductSomePageResponseDTO();
            //set du lieu cho ProductSomePageResponseDTO

            //goi ham getProductSomePageResponseDTO de lay du lieu cho setProductSomeReturns
            productSomePageResponseDTO.setProductSomeReponseDtos(getProductSomePageResponseDTO(productsPage));

            productSomePageResponseDTO.setPage(productsPage.getNumber());
            productSomePageResponseDTO.setSize(productsPage.getSize());
            productSomePageResponseDTO.setTotalElements(productsPage.getTotalElements());
            productSomePageResponseDTO.setTotalPages(productsPage.getTotalPages());

            log.info("Retrieved partial product list from page {}: successful", page);
            return new ResponseEntity<>(productSomePageResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error while getting partial product list");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static List<ProductSomeReponseDto> getProductSomePageResponseDTO(Page<Product> productsPage) {
        return productsPage.stream().map(ProductSomeReponseDto::new).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> getDetail(Long idProduct) {
        try {
            Product productDB = productRepository.findById(idProduct).orElse(null);
            if (ObjectUtils.isEmpty(productDB)) {
                messenger.setMessenger(ConstantsProduct.PRODUCT_EMTY);
                return new ResponseEntity<>(messenger, HttpStatus.OK);
            }

            ProductDetailDto product = new ProductDetailDto(productDB);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while getting partial product list: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getTypeDetailArrange(int page, int size, Long detailType, String arrange) {
        try {
            // Tạo đối tượng Pageable để xác định trang và kích thước trang
            Pageable pageable = PageRequest.of(page, size);

            // Lấy dữ liệu từ repository với sắp xếp theo giá giảm dần hoặc tăng dần
            Page<Product> productsPage = null;
            if ("DESC".equalsIgnoreCase(arrange)) {
                //lấy
                productsPage = productRepository.findByDetailTypeOrderByPriceDesc(detailType, pageable);
            } else if ("ASC".equalsIgnoreCase(arrange)) {
                productsPage = productRepository.findByDetailTypeOrderByPriceAsc(detailType, pageable);
            } else {
                productsPage = productRepository.findByDetailType(detailType, pageable);
            }

            // Tạo đối tượng DTO để trả về
            ProductSomePageResponseDTO productSomePageResponseDTO = new ProductSomePageResponseDTO();
            // Thiết lập dữ liệu cho DTO
            productSomePageResponseDTO.setProductSomeReponseDtos(getProductSomePageResponseDTO(productsPage));
            productSomePageResponseDTO.setPage(productsPage.getNumber());
            productSomePageResponseDTO.setSize(productsPage.getSize());
            productSomePageResponseDTO.setTotalElements(productsPage.getTotalElements());
            productSomePageResponseDTO.setTotalPages(productsPage.getTotalPages());

            log.info("Retrieved partial product list from page {}: successful", page);
            return ResponseEntity.ok(productSomePageResponseDTO);

        } catch (IllegalArgumentException e) {
            log.error("Invalid arrangement parameter: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error while getting partial product list: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<?> getByTypeProduct_IdArrange(int page, int size, Long typeProduct_Id, String arrange) {
        try {
            // Tạo đối tượng Pageable để xác định trang và kích thước trang
            Pageable pageable = PageRequest.of(page, size);

            // Lấy dữ liệu từ repository với sắp xếp theo giá giảm dần hoặc tăng dần
            Page<Product> productsPage = null;
            if ("DESC".equalsIgnoreCase(arrange)) {
                productsPage = productRepository.findByTypeProduct_IdOrderByPriceDesc(typeProduct_Id, pageable);
            } else if ("ASC".equalsIgnoreCase(arrange)) {
                productsPage = productRepository.findByTypeProduct_IdOrderByPriceAsc(typeProduct_Id, pageable);
            } else {
                productsPage = productRepository.findByTypeProduct_Id(typeProduct_Id, pageable);
            }

            // Tạo đối tượng DTO để trả về
            ProductSomePageResponseDTO productSomePageResponseDTO = new ProductSomePageResponseDTO();
            // Thiết lập dữ liệu cho DTO
            productSomePageResponseDTO.setProductSomeReponseDtos(getProductSomePageResponseDTO(productsPage));
            productSomePageResponseDTO.setPage(productsPage.getNumber());
            productSomePageResponseDTO.setSize(productsPage.getSize());
            productSomePageResponseDTO.setTotalElements(productsPage.getTotalElements());
            productSomePageResponseDTO.setTotalPages(productsPage.getTotalPages());

            log.info("Retrieved partial product list from page {}: successful", page);
            return ResponseEntity.ok(productSomePageResponseDTO);

        } catch (IllegalArgumentException e) {
            log.error("Invalid arrangement parameter: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error while getting partial product list: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<?> deleteProduct(long id) {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);

                log.info("Deleted product successfully: {}", id);
                messenger.setMessenger(ConstantsProduct.DELETE_PRODUCT_SUCCESS + id);

                return new ResponseEntity<>(messenger, HttpStatus.OK);

            }
            log.error("Failed to delete product: Product with id {} not found", id);
            messenger.setMessenger(ConstantsProduct.PRODUCT_EMTY);
            return new ResponseEntity<>(messenger, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error while deleting product: {}", e.getMessage());
            messenger.setMessenger(ConstantsProduct.ERROR);
            return new ResponseEntity<>(messenger, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> deleteMultipleProducts(List<Long> ids) {
        try {
            List<String> deleteMultipleProducts = new ArrayList<>();
            StringBuilder message = new StringBuilder();
            for (Long id : ids) {
                if (productRepository.existsById(id)) {
                    //   productRepository.deleteById(id);
                    log.info("Deleted product successfully: {}", id);
                    deleteMultipleProducts.add(ConstantsProduct.DELETE_PRODUCT_SUCCESS + id);
                } else {
                    log.error("Failed to delete product: Product with id {} not found", id);
                    deleteMultipleProducts.add(ConstantsProduct.DELETE_PRODUCT_FAILSE_WRONG + id);
                }
            }
            return new ResponseEntity<>(deleteMultipleProducts, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ConstantsProduct.ERROR, e.getMessage());
            messenger.setMessenger(ConstantsProduct.ERROR);
            return new ResponseEntity<>(messenger, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> putProduct(long id, ProductDto productDto) {
        try {
            Product product = productRepository.findById(id).orElse(null);
            if (ObjectUtils.isEmpty(product)) {
                log.error("Failed to update product: Product with id {} not found", id);
                messenger.setMessenger(ConstantsProduct.TYPE_NULL);
                return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
            }

            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            product.setDetailType(productDto.getDetailType());
            product.setImages(productDto.getImages());
            product.setDiscount(productDto.getDiscount());
            product.setDescribe(productDto.getDescribe());
            product.setTypeProduct(typeProductRepository.findById(productDto.getIdTypeProduct()).orElse(null));

            if (ObjectUtils.isEmpty(product.getTypeProduct())) {
                log.error("Failed to update product: TypeProduct is null");
                messenger.setMessenger(ConstantsProduct.TYPE_NULL);
                return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
            }
            productRepository.save(product);
            log.info("Updated product successfully: {}", product.getName());
            messenger.setMessenger(ConstantsProduct.PUT_PRODUCT_SUCCESS);
            return new ResponseEntity<>(messenger, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error while updating product: {}", e.getMessage());
            messenger.setMessenger(ConstantsProduct.ERROR);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<?> findByNameProduct(int page, int size, String nameProduct) {
        try {
            //lay 1 page tu csdl
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productsPage = productRepository.findByName(nameProduct, pageable);

            //khoi tao 1 ProductSomePageReturnDTO de Response
            ProductSomePageResponseDTO productSomePageResponseDTO = new ProductSomePageResponseDTO();
            //set du lieu cho ProductSomePageResponseDTO

            //goi ham getProductSomePageResponseDTO de lay du lieu cho setProductSomeReturns
            productSomePageResponseDTO.setProductSomeReponseDtos(getProductSomePageResponseDTO(productsPage));

            productSomePageResponseDTO.setPage(productsPage.getNumber());
            productSomePageResponseDTO.setSize(productsPage.getSize());
            productSomePageResponseDTO.setTotalElements(productsPage.getTotalElements());
            productSomePageResponseDTO.setTotalPages(productsPage.getTotalPages());

            log.info("Retrieved partial product list from page {}: successful", page);
            return new ResponseEntity<>(productSomePageResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error while getting partial product list");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
