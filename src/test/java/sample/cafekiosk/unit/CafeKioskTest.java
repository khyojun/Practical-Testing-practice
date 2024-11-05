package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.berverage.Americano;
import sample.cafekiosk.unit.berverage.Latte;

import static org.assertj.core.api.Assertions.assertThat;



class CafeKioskTest {


    @Test
    void add_manual_test(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">> 담긴 음료 수 : " + cafeKiosk.getBerverages().size());
        System.out.println(">> 담긴 음료 : " + cafeKiosk.getBerverages().get(0).getName());

    }


    @Test
    void add(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        // 위와 달리 사람이 검증이 아닌 기계가 검증
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBerverages().get(0).getName()).isEqualTo("아메리카노");
    }


    @Test
    void remove(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(0);
    }


    @Test
    void clear(){
        CafeKiosk cafeKiosk = new CafeKiosk();

        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(0);
    }


}