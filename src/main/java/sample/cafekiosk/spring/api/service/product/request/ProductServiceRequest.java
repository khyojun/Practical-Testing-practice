package sample.cafekiosk.spring.api.service.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

public class ProductServiceRequest {
    private String name;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private int price;


    @Builder
    private ProductServiceRequest(String name, ProductType type, ProductSellingStatus sellingStatus, int price) {
        this.name = name;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .name(name)
                .type(type)
                .price(price)
                .sellingStatus(sellingStatus)
                .build();
    }

}
