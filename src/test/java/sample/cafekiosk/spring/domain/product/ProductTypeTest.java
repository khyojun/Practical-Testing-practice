package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {
    // 이런것도 테스트 해야해?
    // 당연..! -> 언제 어떻게 바뀔지는 아무도 모르기 때문에!


    @DisplayName("여러 주제 상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockTypeEx(){
        //given
        ProductType[] productTypes = ProductType.values();
        for (ProductType productType : productTypes) {
            if(productType == ProductType.HANDMADE){
                //when
                boolean result = ProductType.containsStockType(productType);

                assertThat(result).isFalse();
            }

            if(productType == ProductType.BAKERY || productType == ProductType.BOTTLE){
                //when
                boolean result = ProductType.containsStockType(productType);
                //then
                assertThat(result).isTrue();
        }

    }


    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType(){
        //given
        ProductType givenType = ProductType.HANDMADE;


        //when
        boolean result = ProductType.containsStockType(givenType);


        //then
        assertThat(result).isFalse();
    }



    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType2(){
        //given
        ProductType givenType = ProductType.BAKERY;


        //when
        boolean result = ProductType.containsStockType(givenType);


        //then
        assertThat(result).isTrue();
    }

}