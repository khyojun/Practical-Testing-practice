package sample.cafekiosk.unit.berverage;

public class Americano implements Berverage{
    @Override
    public String getName() {
        return "아메리카노";
    }

    @Override
    public int getPrice() {
        return 4000;
    }
}
