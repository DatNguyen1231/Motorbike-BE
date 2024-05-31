package com.example.demo.service.serviceImpl;

import com.example.demo.constants.ConstantsShoppingCart;
import com.example.demo.model.Dto.Messenger;
import com.example.demo.model.Dto.RevenueStatisticsDTO;
import com.example.demo.model.Dto.ShoppingCartDto;
import com.example.demo.model.Dto.ShoppingCartDtoReturn;
import com.example.demo.model.entity.Product;
import com.example.demo.model.entity.ShoppingCart;
import com.example.demo.model.entity.ShoppingCartDetail;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ShoppingCartDetailRepository;
import com.example.demo.repositories.ShoppingCartRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.Util.GetInfoUser.getUsername;

@Service
public class ShoppingCartImpl implements ShoppingCartService {
    private final Messenger messenger;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;

    public ShoppingCartImpl(
            Messenger messenger,
            ShoppingCartRepository shoppingCartRepository,
            UserRepository userRepository, ProductRepository productRepository, ShoppingCartDetailRepository shoppingCartDetailRepository

    ) {
        this.messenger = messenger;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
    }

    boolean checkUserAndStatus(int status) {
        return shoppingCartRepository.existsByUser_UsernameAndStatus(getUsername(), status);
    }

    Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    ShoppingCart getShoppingCartByUser(int status) {
        return shoppingCartRepository.findByUsernameAndStatus(getUsername(), status);
    }

    ShoppingCart getShoppingCartById(Long id) {
        return shoppingCartRepository.findByIdCart(id);
    }

    @Override
    public ResponseEntity<?> paymentCart() {

        if (checkUserAndStatus(0)) {
            ShoppingCart shoppingCartNew = getShoppingCartByUser(0);

            List<StringBuilder> mess = new ArrayList<>();
            for (ShoppingCartDetail cartDetail : shoppingCartNew.getShoppingCartDetails()) {
                // Kiểm tra xem số lượng trong kho có đủ để thêm vào giỏ hàng không
                long availableQuantity = cartDetail.getProduct().getQuantity();
                long requestedQuantity = cartDetail.getQuantityCart();

                if (availableQuantity < requestedQuantity) {
                    StringBuilder mess1 = new StringBuilder();
                    mess1.append("Sản phẩm ");
                    mess1.append(cartDetail.getProduct().getName());
                    mess1.append(" đã hết hàng.");
                    mess.add(mess1);
                    return new ResponseEntity<>(mess, HttpStatus.BAD_REQUEST);
                }
            }
            // 1:completly payment
            shoppingCartNew.setStatus(1);


            //Trừ số lượng ở kho
            for (ShoppingCartDetail a : shoppingCartNew.getShoppingCartDetails()) {
                Product product = getProduct(a.getProduct().getId());
                assert product != null;
                product.setQuantity(product.getQuantity() - a.getQuantityCart());

                productRepository.save(product);
                messenger.setMessenger("Duyệt đơn hàng thành công");
            }
            shoppingCartRepository.save(shoppingCartNew);

            messenger.setMessenger(ConstantsShoppingCart.ADD_SUCCESS);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        } else {
            messenger.setMessenger(ConstantsShoppingCart.CART_EMPTY);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> orderOrCancel(Long id, int status) {


        ShoppingCart shoppingCartNew = getShoppingCartById(id);


        // 0:Orders at cart user ; 1:user places an order, 2:admin accept the order , 3 admin cancel order
        shoppingCartNew.setStatus(status);
        messenger.setMessenger("Hủy đơn hàng thành công");
        if (status == 3) {
            //cong lai so luong neu don hang bi huy
            for (ShoppingCartDetail a : shoppingCartNew.getShoppingCartDetails()) {
                Product product = getProduct(a.getProduct().getId());
                assert product != null;
                product.setQuantity(product.getQuantity() + a.getQuantityCart());

                productRepository.save(product);
                messenger.setMessenger("Duyệt đơn hàng thành công");
            }
        }
        shoppingCartRepository.save(shoppingCartNew);


        return new ResponseEntity<>(messenger, HttpStatus.OK);

    }


    @Override
    public ResponseEntity<?> updateCart(Long idCart, int quantityCart) {
        String username = getUsername();

        if (!shoppingCartRepository.existsByUser_UsernameAndShoppingCartDetails_Id(username, idCart)) {
            messenger.setMessenger(ConstantsShoppingCart.PRODUCT_NOT_EXIST);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser_UsernameAndShoppingCartDetails_Id(username, idCart);

        if (quantityCart <= 0) {
            return deleteByIdShoppingCartDetail(idCart);
        }

        List<ShoppingCartDetail> shoppingCartDetails = shoppingCart.getShoppingCartDetails();

        for (ShoppingCartDetail cartDetail : shoppingCartDetails) {
            if (Objects.equals(cartDetail.getId(), idCart)) {
                long availableQuantity = cartDetail.getProduct().getQuantity();
                if (availableQuantity < quantityCart) {
                    messenger.setMessenger(ConstantsShoppingCart.QUANTITY_LIMIT_REACHED);
                    return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
                }
                cartDetail.setQuantityCart(quantityCart);
                break;
            }
        }
        shoppingCart.setShoppingCartDetails(shoppingCartDetails);
        shoppingCartRepository.save(shoppingCart);

        messenger.setMessenger(ConstantsShoppingCart.UPDATE_SUCCESS);
        return new ResponseEntity<>(messenger, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addCart(ShoppingCartDto shoppingCartDto) {
        String username = getUsername();

        try {
            if (shoppingCartDto.getQuantityCart() <= 0) {
                messenger.setMessenger(ConstantsShoppingCart.QUANTITY_GREATER_THAN_ZERO);
                return new ResponseEntity<>(messenger, HttpStatus.OK);
            }

            Product product = getProduct(shoppingCartDto.getIdProduct());
            if (product == null) {
                messenger.setMessenger(ConstantsShoppingCart.PRODUCT_NOT_FOUND);
                return new ResponseEntity<>(messenger, HttpStatus.OK);
            }

            long availableQuantity = product.getQuantity();
            long requestedQuantity = shoppingCartDto.getQuantityCart();
            if (availableQuantity < requestedQuantity) {
                messenger.setMessenger(ConstantsShoppingCart.QUANTITY_LIMIT_REACHED);
                return new ResponseEntity<>(messenger, HttpStatus.OK);
            }

            ShoppingCart shoppingCartNew = shoppingCartRepository.findByUser_UsernameAndStatus(username, 0).orElse(null);
            if (shoppingCartNew == null) {
                shoppingCartNew = new ShoppingCart();
                shoppingCartNew.setPaymentDate(new Date());
                shoppingCartNew.setStatus(0);
                shoppingCartNew.setUser(userRepository.findByUsername(username));
            }

            List<ShoppingCartDetail> shoppingCartDetails = shoppingCartNew.getShoppingCartDetails();
            boolean productExists = false;
            for (ShoppingCartDetail cartDetail : shoppingCartDetails) {
                if (Objects.equals(cartDetail.getProduct().getId(), shoppingCartDto.getIdProduct())) {
                    long updatedQuantity = cartDetail.getQuantityCart() + shoppingCartDto.getQuantityCart();
                    if (updatedQuantity > availableQuantity) {
                        messenger.setMessenger(ConstantsShoppingCart.QUANTITY_LIMIT_REACHED);
                        return new ResponseEntity<>(messenger, HttpStatus.OK);
                    }
                    cartDetail.setQuantityCart(updatedQuantity);
                    productExists = true;
                    break;
                }
            }

            if (!productExists) {
                ShoppingCartDetail shoppingCartDetail = new ShoppingCartDetail();
                shoppingCartDetail.setQuantityCart(shoppingCartDto.getQuantityCart());
                shoppingCartDetail.setDateCreated(new Date());
                shoppingCartDetail.setProduct(product);
                shoppingCartDetails.add(shoppingCartDetail);
            }

            shoppingCartNew.setShoppingCartDetails(shoppingCartDetails);
            shoppingCartRepository.save(shoppingCartNew);
            messenger.setMessenger(ConstantsShoppingCart.ADD_SUCCESS);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        } catch (Exception e) {
            messenger.setMessenger(ConstantsShoppingCart.ADD_ERROR);
            return new ResponseEntity<>(messenger, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> getAllCard(int status) {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.getAllCartByStatus(status);

        List<ShoppingCartDtoReturn> shoppingCartDtoReturns = shoppingCarts.stream()
                .map(ShoppingCartDtoReturn::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(shoppingCartDtoReturns, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> getAllCardByUser(int status) {
        List<ShoppingCart> shoppingCarts;
        if(status==4){
            shoppingCarts=shoppingCartRepository.findByUser_Username(getUsername());
        }
        else {
            shoppingCarts = shoppingCartRepository.getListByUser_UsernameAndStatus(getUsername(),status);
        }


        List<ShoppingCartDtoReturn> shoppingCartDtoReturns = shoppingCarts.stream()
                .map(ShoppingCartDtoReturn::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(shoppingCartDtoReturns, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> getRevenue(Date firstDay, Date lastDay, int status) {
        List<ShoppingCart> shoppingCarts;
        if (status == 4) {
            shoppingCarts = shoppingCartRepository.findByPaymentDate(firstDay, lastDay);
        } else {
            shoppingCarts = shoppingCartRepository.findByPaymentDateBetween(firstDay, lastDay, status);
        }
//        List<ShoppingCartDtoReturn> shoppingCartDtoReturns = shoppingCarts.stream()
//                .map(ShoppingCartDtoReturn::new)
//                .collect(Collectors.toList());
        RevenueStatisticsDTO t = new RevenueStatisticsDTO(shoppingCarts);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteByIdShoppingCartDetail(Long id) {
        String username = getUsername();

        if (shoppingCartRepository.existsByUser_UsernameAndShoppingCartDetails_Id(username, id)) {
            shoppingCartDetailRepository.deleteById(id);
            messenger.setMessenger(ConstantsShoppingCart.DELETE_SUCCESS);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        messenger.setMessenger(ConstantsShoppingCart.PRODUCT_NOT_EXIST);
        return new ResponseEntity<>(messenger, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> getCartByUser(int status) {
        String username = getUsername();

        try {
            if (status == 0) {
                if (checkUserAndStatus(status)) {
                    ShoppingCart shoppingCarts = getShoppingCartByUser(status);
                    ShoppingCartDtoReturn shoppingCartDtoReturns = new ShoppingCartDtoReturn(shoppingCarts);
                    return new ResponseEntity<>(shoppingCartDtoReturns, HttpStatus.OK);
                }
            } else {
                if (checkUserAndStatus(status)) {
                    List<ShoppingCart> shoppingCarts = shoppingCartRepository.findListByUsernameAndStatus(username, status);
                    ShoppingCartDtoReturn shoppingCartDtoReturns = null;
                    for (ShoppingCart a : shoppingCarts) {
                        shoppingCartDtoReturns = new ShoppingCartDtoReturn(a);
                    }
                    return new ResponseEntity<>(shoppingCartDtoReturns, HttpStatus.OK);
                }
            }
            messenger.setMessenger(ConstantsShoppingCart.CART_EMPTY);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        } catch (Exception e) {
            messenger.setMessenger(ConstantsShoppingCart.ADD_ERROR);
            return new ResponseEntity<>(messenger, HttpStatus.BAD_REQUEST);
        }

    }

//    @Override
//    public List<ProductQuantityProjection> test() {
//          return shoppingCartRepository.test();
//    }

//    @Override
//    public  List<ProductQuantityDTO>  test() {
//        return shoppingCartRepository.test();
//    }


}
