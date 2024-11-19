package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class OrderRepositoryTest extends IntegrationTestSupport {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;


    @DisplayName("시작 날짜와 종료 날짜 전에 들어온 주문의 갯수가 정확한지 확인한다.")
    @Test
    void findOrdersBy(){
        //given
        LocalDateTime now = LocalDateTime.of(2024, 11, 19, 0, 0);
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));
        List<Product> products = List.of(product1, product2, product3);

        Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2024,11,18,23,59,59));
        Order order2 = createPaymentCompletedOrder(products, LocalDateTime.of(2024,11,20,0,0,0));
        Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2024,11,19,0,0,0));
        Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2024,11,19,23,59,59));

        LocalDateTime startDateTime = LocalDateTime.of(2024, 11, 19, 0, 0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 11, 20, 0, 0,0);


        //when
        List<Order> orders = orderRepository.findOrdersBy(startDateTime, endDateTime, OrderStatus.PAYMENT_COMPLETED);

        //then
        assertThat(orders).hasSize(2)
                .extracting("id")
                .containsExactlyInAnyOrder(order3.getId(), order4.getId());

    }

    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime now) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus
            ,String name, int price
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