package sample.cafekiosk.spring.api.controller.order.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
    // controller dto 에서만 validation 로직을 분리하여 진행하는 방식이 좋아보임!
    // 귀찮긴 함! 너무 그럼! 서비스가 커지면 커질수록 이 작업 또한 부담일 수 있음!
    // 나중에 포스키 주문을 받을수도 사이렌 오더에서 받을수도 있고, 주문 받는 채널이 여러개가 될 수 있음!
    // 나는 같은 서비스를 사용하고 싶은데 만약 컨트롤러 dto 그대로 받아버리면 이거 이렇게 받으면 안될건데? 하고 하는 문제가 일어날 수 있음!
    // 그래서 너무너무 귀찮지만 분리해주는 작업들이 너무 필요하다! -> 숲은 보자~
    @NotEmpty(message = "상품 번호는 필수입니다.")
    private List<String> productNumbers;


    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }


    public OrderCreateServiceRequest toServiceRequest(){
        return OrderCreateServiceRequest.builder()
                .productNumbers(productNumbers)
                .build();
    }
}
