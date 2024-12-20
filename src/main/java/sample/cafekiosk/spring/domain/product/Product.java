package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String productNumber;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;


    private int price;


    @Builder
    private Product(String productNumber, String name, ProductType type, ProductSellingStatus sellingStatus, int price) {
        this.productNumber = productNumber;
        this.name = name;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.price = price;
    }
}
