package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;



class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private StockRepository stockRepository;


    @AfterEach
    void tearDown() {
        //productRepository.deleteAll(); // 각각의 테스트 자원을 정리
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder(){
        //given 테스트 준비 단계는 모두 given
        LocalDateTime registeredTime = LocalDateTime.now();
        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
        productRepository.saveAll(List.of(product1, product2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        //when

        OrderResponse orderResponse = orderService.createOrder(request, registeredTime);


        //then

        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredTime,4000);

        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("002", 3000)
                );


    }





    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers(){
        //given 테스트 준비 단계는 모두 given
        LocalDateTime registeredTime = LocalDateTime.now();
        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
        productRepository.saveAll(List.of(product1, product2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request, registeredTime);


        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredTime,2000);

        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("001", 1000)
                );


    }

    private Product createProduct(ProductType type, String productNumber, int price){
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .build();
    }



    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock(){
        //given 테스트 준비 단계는 모두 given
        LocalDateTime registeredTime = LocalDateTime.now();


        Product product1 = createProduct(ProductType.BOTTLE, "001", 1000);
        Product product2 = createProduct(ProductType.BAKERY, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);

        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        //when

        OrderResponse orderResponse = orderService.createOrder(request, registeredTime);


        //then

        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredTime,10000);

        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("002", 3000),
                        Tuple.tuple("003", 5000)
                );


        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );


    }



    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock(){
        //given 테스트 준비 단계는 모두 given
        LocalDateTime registeredTime = LocalDateTime.now();


        Product product1 = createProduct(ProductType.BOTTLE, "001", 1000);
        Product product2 = createProduct(ProductType.BAKERY, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);

        stock1.deductQuantity(1); // todo : 이런식으로 하면 안됨!
        // createOrder 가 주 행위임! 재고 차단이라는 다른 행위를 가져다 썼음!
        // 1. 다른 맥락의 친구가 들어옴! 논리적 사고를 한 번 더 진행해야한다는 것임!
        // 2. deductQuantity 가 잘못될 경우! createOrder 가 잘못될 수 있음! 앞의 given 절 하다가 테스트를 실패한 것임! 이건 좀...! 이후에 테스트 왜 깨졌는지 유추하기가 어려워짐! 조금 더 커지면!
        // 그래서 웬만하면 생성자 기반으로 테스트 진행하면 좋음! 순수한 생성자나 빌더로 구성하는게 좋음! 팩토리 메서드도 어떤 의도를 가지고 만든 것임!
        // 팩토리 메서드도 어떤 목적이 있음! ex) 어떤 인자를 받고 싶다거나, 어떤 거 전에 검증을 하거나
        // 그래서 given절은 웬만하면 생성자들을 구성으로 오게 하거나 API 들을 최대한 사용하지 않고 독립적으로 할 수 있도록 하는것이 좋다!

        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        //when //then
        assertThatThrownBy(() -> orderService.createOrder(request, registeredTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
    }



}