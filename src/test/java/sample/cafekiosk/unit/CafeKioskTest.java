package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.berverage.Americano;
import sample.cafekiosk.unit.berverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CafeKioskTest {


    @Test
    void add_manual_test(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);

        System.out.println(">> 담긴 음료 수 : " + cafeKiosk.getBerverages().size());
        System.out.println(">> 담긴 음료 : " + cafeKiosk.getBerverages().get(0).getName());

    }

    //@DisplayName("음료 1개 추가 테스트")
    @DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
    @Test
    void add(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);

        // 위와 달리 사람이 검증이 아닌 기계가 검증
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBerverages().get(0).getName()).isEqualTo("아메리카노");
    }


    @Test
    void addSeveralBerverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 2);

        // 위와 달리 사람이 검증이 아닌 기계가 검증
        assertThat(cafeKiosk.getBerverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBerverages().get(1)).isEqualTo(americano);
    }


    @Test
    void addZeroBerverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        // 위와 달리 사람이 검증이 아닌 기계가 검증
        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료의 수량은 0보다 작을 수 없습니다.");
    }





    @Test
    void remove(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 1);
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(0);
    }


    @Test
    void clear(){
        CafeKiosk cafeKiosk = new CafeKiosk();

        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano, 1);
        cafeKiosk.add(latte, 1);
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(0);
    }


    @Test
    void calculateTotalPrice(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        int totalPrice = cafeKiosk.calculateTotalPrice();

        assertThat(totalPrice).isEqualTo(8500);
    }



    @Test
    void createOrder(){ // 이 친구는 항상 성공하는 테스트가 아님! 시간 따라 결과가 달라짐!
        CafeKiosk cafeKiosk = new CafeKiosk();

        Americano americano = new Americano();
        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder();

        assertThat(order.getBerverages()).hasSize(1);
        assertThat(order.getBerverages().get(0).getName()).isEqualTo("아메리카노");
    }


    @Test
    void createOrderWithCurrentTime(){
        CafeKiosk cafeKiosk = new CafeKiosk();

        Americano americano = new Americano();
        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2024,11,5,10,0));

        assertThat(order.getBerverages()).hasSize(1);
        assertThat(order.getBerverages().get(0).getName()).isEqualTo("아메리카노");
    }


    @Test
    void createOrderOutsideOpenTime(){
        CafeKiosk cafeKiosk = new CafeKiosk();

        Americano americano = new Americano();
        cafeKiosk.add(americano);


        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2024,11,5,9,59)))
                .isInstanceOf((IllegalArgumentException.class))
                .hasMessage("현재 주문이 가능한 시간이 아닙니다.");
    }


}