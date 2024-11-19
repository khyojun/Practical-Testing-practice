package sample.cafekiosk.spring.api.service.order;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailServicel;


    // 여기에 트랜잭션 거는게 좋을 지 생각해 볼 필요 있음!-> 메일 전송은 오래 걸림!
    // 트랜잭션을 물게 되면 커넥션을 물게되는거기때문에 오랜 시간 물 수 있음!
    // 사실 repository 단에서 트랜잭션이 이거 한 번 걸릴 거기 때문에 전체 과정에서 트랜잭션 거는거는 옳지 않을 수 있음!
    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
        // 해당 일자에 결제완료된 주문들을 가져와서
        List<Order> orders = orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );

        // 총 매출 합계를 계산하고
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();


        // 메일 전송
        boolean result = mailServicel.sendMail("no-reply@cafekiosk.com", email,
                String.format("[매출통계] %s",orderDate),
                String.format("총 매출 합계는 %s원입니다.", totalAmount));

        if(!result)
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패하였습니다.");
        return true;
    }


}
