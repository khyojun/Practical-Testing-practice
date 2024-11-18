package sample.cafekiosk.spring.api.controller.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductServiceRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "상품 이름은 필수입니다.")// notblank는 문자가 필수로 있어야 한다!
    //@NotNull // "" "  " 요건 허용이 됨!
    //@NotEmpty // ""  이것만 아니면 통과! 공백은 허용!
    // String name -> 상품 이름은 20자 제한 이렇게 도메인 정책
    //@Max(20) // 이걸 여기서 해야하는게 맞나? 책임이 맞나? 고민해봐야함!
    // 왜냐? 우리만 사실 도메인 정책에서 사용하는 그런 성격의 정책이라면? 특수한 정책이라면? 구분해야함!
    // 그래서 우리는 하나의 밸리데이션으로 보이더라도 어느 레이어에서 검증할지 한 번에 한 레이어에서 할 필요는 없음!
    // 책임을 어떻게 분리하면 좋을지 생각!
    private String name;
    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;
    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;
    @Positive(message = "상품 가격은 양수여야 합니다.")
    private int price;


    @Builder
    private ProductCreateRequest(String name, ProductType type, ProductSellingStatus sellingStatus, int price) {
        this.name = name;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.price = price;
    }


    public ProductServiceRequest toServiceRequest(){
        return ProductServiceRequest.builder()
                .name(name)
                .type(type)
                .sellingStatus(sellingStatus)
                .price(price)
                .build();
    }


}
