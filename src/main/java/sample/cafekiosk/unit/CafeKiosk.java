package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.berverage.Berverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {


    public static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);
    public static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);


    private final List<Berverage> berverages = new ArrayList<>();


    public void add(Berverage berverage) {
        berverages.add(berverage);
    }

    public void add(Berverage berverage, int count) {

        if (count <= 0) {
            throw new IllegalArgumentException("음료의 수량은 0보다 작을 수 없습니다.");
        }

        for (int i = 0; i < count; i++)
            berverages.add(berverage);
    }


    public void remove(Berverage berverage) {
        berverages.remove(berverage);
    }

    public void clear() {
        berverages.clear();

    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Berverage berverage : berverages) {
            totalPrice += berverage.getPrice();
        }
        return totalPrice;
    }


    public Order createOrder() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime currentTime = now.toLocalTime();

        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("현재 주문이 가능한 시간이 아닙니다.");
        }

        return new Order(LocalDateTime.now(), berverages);
    }


    public Order createOrder(LocalDateTime now) {
        LocalTime currentTime = now.toLocalTime();

        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("현재 주문이 가능한 시간이 아닙니다.");
        }

        return new Order(LocalDateTime.now(), berverages);
    }

}
