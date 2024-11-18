package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime now) {
        List<String> productNumbers = request.getProductNumbers();
        //Product
        List<Product> products = findProductsBy(productNumbers);


        Order order = Order.create(products, now);
        Order savedOrder = orderRepository.save(order);

        //Order
        return OrderResponse.of(savedOrder);

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
