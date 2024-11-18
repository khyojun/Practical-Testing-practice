package sample.cafekiosk.spring.api.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;


    /**
     *
     * 재고 감소 -> 동시성 고민
     * optimistic lock / pessimistic lock / ... locking 개념을 사용 순차적으로 처리될 수 있도록
     * 사실 이 키오스크 문제가 조금 복잡하게 있음!
     */
    // 여기 request 가 controller에 의존하는 dto 가 파라미터에 있음!
    // service 가 controller를 알고있음! layered 에서 가장 좋은 그림은 하위 레이어가 상위 레이어를 모르는 형태가 가장 좋음!
    // 그래서 강사님은 서비스용 requestDto 를 만들고 진행하심!
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime now) {
        List<String> productNumbers = request.getProductNumbers();
        //Product
        List<Product> products = findProductsBy(productNumbers);


        deductStockQuantities(products);


        Order order = Order.create(products, now);
        Order savedOrder = orderRepository.save(order);

        //Order
        return OrderResponse.of(savedOrder);

    }

    private void deductStockQuantities(List<Product> products) {
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            // 어? 왜 deduct에서 하는데 굳이 또 해야해?
            // 그런데 이건 약간 관점을 달리해야하는 문제임!
            // 주문 생성 로직을 실행하다가 stock 에 대한 차감을 시도하는 과정
            if(stock.isQuantityLessThan(quantity)){
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            // 이 안쪽에서 체크하는 경우 밖에 서비스가 어떻게 되어있는지 전혀 모름!
            // 수량 차감한다고 했을때 항상 올바르게 수량 차감이 될 수 있도록 이 친구 자체만으로 보장을 해줘야함!
            // 안쪽 테스트 던지는거랑 위에 던지는거랑 다른 상황임! 그래서 메시지도 다름!
            // deductQuantity 를 다른 곳에서도 사용할 수 있기 때문에! 그렇다!
            stock.deductQuantity(quantity);

        }
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .toList();
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
        return stockMap;
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        return productCountingMap;
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers); // 001에 대한 product 한 개만 나옴 in 절이 있기 때문!

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));


        List<Product> duplicatedProducts = productNumbers.stream() // 여기서 순회하면서 2개 중복된거 조회됨!
                .map(productNumber -> productMap.get(productNumber))
                .collect(Collectors.toList());
        return duplicatedProducts;
    }

}
