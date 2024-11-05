package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.berverage.Americano;

import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {


    @Test
    void add(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">> 담긴 음료 수 : " + cafeKiosk.getBerverages().size());
        System.out.println(">> 담긴 음료 : " + cafeKiosk.getBerverages().get(0).getName());

    }

}