package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

class ProductTypeTest {
    // 이런것도 테스트 해야해?
    // 당연..! -> 언제 어떻게 바뀔지는 아무도 모르기 때문에!


    @DisplayName("여러 주제 상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockTypeEx(){
        //given
        ProductType[] productTypes = ProductType.values();
        for (ProductType productType : productTypes) {
            if(productType == HANDMADE){
                //when
                boolean result = ProductType.containsStockType(productType);

                assertThat(result).isFalse();
            }

            if(productType == BAKERY || productType == BOTTLE){
                //when
                boolean result = ProductType.containsStockType(productType);
                //then
                assertThat(result).isTrue();
        }

        }
    }


    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType(){
        //given
        ProductType givenType = HANDMADE;


        //when
        boolean result = ProductType.containsStockType(givenType);


        //then
        assertThat(result).isFalse();
    }



    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType2(){
        //given
        ProductType givenType = BAKERY;


        //when
        boolean result = ProductType.containsStockType(givenType);


        //then
        assertThat(result).isTrue();
    }



    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType3(){
        //given
        ProductType givenType1 = HANDMADE;
        ProductType givenType2 = BOTTLE;
        ProductType givenType3 = BAKERY;



        //when
        boolean result1 = ProductType.containsStockType(givenType1);
        boolean result2 = ProductType.containsStockType(givenType2);
        boolean result3 = ProductType.containsStockType(givenType3);


        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isTrue();
    }


    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @CsvSource({"HANDMADE, false","BOTTLE, true", "BAKERY, true"})
    @ParameterizedTest
    void containsStockType4(ProductType productType, boolean expected){
        //when
        boolean result1 = ProductType.containsStockType(productType);

        //then
        assertThat(result1).isEqualTo(expected);
    }

    private static Stream<Arguments> provieProductTypesForCheckingStockType(){
        return Stream.of(
                Arguments.of(HANDMADE, false),
                Arguments.of(BOTTLE, true),
                Arguments.of(BAKERY, true)
        );
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @MethodSource("provieProductTypesForCheckingStockType")
    @ParameterizedTest
    void containsStockType5(ProductType productType, boolean expected){
        //when
        boolean result1 = ProductType.containsStockType(productType);

        //then
        assertThat(result1).isEqualTo(expected);
    }



}