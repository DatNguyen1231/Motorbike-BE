package com.example.demo.model.Dto;

import com.example.demo.model.entity.ShoppingCart;
import com.example.demo.model.entity.ShoppingCartDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class RevenueStatisticsDTO<t> {
    private int totalOrder = 0;
    private int successfulOrder = 0;
    private int cancelledOrder = 0;
    private double sumRevenue = 0d;
    private List<RevenueStatisticsDetailDTO> detailRevenueProduct;

    public RevenueStatisticsDTO(List<ShoppingCart> shoppingCart) {
        Map<Long, RevenueStatisticsDetailDTO> productMap = new HashMap<>();
        for (ShoppingCart s : shoppingCart) {
            this.totalOrder += s.getStatus() == 2 || s.getStatus() == 3 ? 1 : 0;
            this.successfulOrder += s.getStatus() == 2 ? 1 : 0;
            this.cancelledOrder += s.getStatus() == 3 ? 1 : 0;
            this.sumRevenue += getTotalRevenue(s);
            //  this.detailRevenueProduct=test(s);
            updateProductMap(s, productMap);
        }
        // Gán danh sách chi tiết thống kê từ Map vào detailRevenueProduct
        this.detailRevenueProduct = new ArrayList<>(productMap.values());
        sortDetailRevenueProduct();
    }

    double getTotalRevenue(ShoppingCart shoppingCart) {
        if(shoppingCart.getStatus()==3||shoppingCart.getStatus()==2)
        return shoppingCart.getShoppingCartDetails().stream()
                .mapToDouble(s -> s.getQuantityCart() *
                        (s.getProduct().getPrice() -
                                (s.getProduct().getPrice() / 100 * s.getProduct().getDiscount())))
                .sum();
        else return 0;
    }

    void updateProductMap(ShoppingCart shoppingCart, Map<Long, RevenueStatisticsDetailDTO> productMap) {
        for (ShoppingCartDetail detail : shoppingCart.getShoppingCartDetails()) {
            RevenueStatisticsDetailDTO existingDetail = productMap.get(detail.getProduct().getId());
            if(shoppingCart.getStatus()==2||shoppingCart.getStatus()==3){
                if (existingDetail != null) {
                    // Nếu sản phẩm đã tồn tại trong Map, tăng số lượng và tổng tiền
                    existingDetail.setQuantityOrder(existingDetail.getQuantityOrder() + detail.getQuantityCart());
                    existingDetail.setTotalMoney(existingDetail.getTotalMoney() + existingDetail.getTotalMoney(detail));
                } else {
                    // Nếu sản phẩm chưa tồn tại trong Map, thêm vào
                    RevenueStatisticsDetailDTO newDetail = new RevenueStatisticsDetailDTO(detail);
                    productMap.put(detail.getProduct().getId(), newDetail);
                }
            }

        }
    }
    public void sortDetailRevenueProduct() {
        // Sử dụng Comparator để so sánh các sản phẩm dựa trên số lượng bán ra
        Comparator<RevenueStatisticsDetailDTO> comparator = new Comparator<RevenueStatisticsDetailDTO>() {
            @Override
            public int compare(RevenueStatisticsDetailDTO o1, RevenueStatisticsDetailDTO o2) {
                // So sánh giảm dần theo số lượng bán ra
                return Long.compare(o1.getQuantityOrder(), o2.getQuantityOrder());
            }
        };

        // Sắp xếp danh sách sản phẩm theo thứ tự giảm dần của số lượng bán ra
        Collections.sort(detailRevenueProduct, comparator.reversed());
    }
}
