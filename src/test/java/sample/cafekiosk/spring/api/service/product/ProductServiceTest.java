package sample.cafekiosk.spring.api.service.product;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    // 처음 규모가 작아졌을때 service 랑 repository 테스트랑 머가 다른지 잘 모를 때가 있음!
    // 하지만 점차 발전해가면 갈수록 달라져가니 분리해서 작성하는 것이 좋을 듯
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProduct() {
        //given
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
       productRepository.save(product1);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();


        //when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());


        //then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("002", ProductType.HANDMADE, ProductSellingStatus.SELLING, 5000, "카푸치노");

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, 4000, "아메리카노"),
                        Tuple.tuple("002", ProductType.HANDMADE, ProductSellingStatus.SELLING, 5000, "카푸치노")
                );


    }


    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
    @Test
    void createProductWhenProductsIsEmpty() {
        //given

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();


        //when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());


        //then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, 5000, "카푸치노");

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, 5000, "카푸치노")
                );
    }


    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus
            , String name, int price
    ) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}