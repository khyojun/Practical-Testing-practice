package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;


/**
 * readOnly = true 관련 얘기
 * 읽기 전용 트랜잭션 진행
 * crud 중 cud 작업은 동작을 안함! onlyRead만 할 수 있는 형태
 * 조회만 가능
 * JPA : CUD 스냅샷 저장, 변경감지 X (성능 향상)
 * CQRS - Command랑 Query를 분리하자! 이거임 줄여 말하면 -> read 라는 행위가 빈도수가 거진 2:8 정도로 높음
 * 이 친구의 시작으로 readOnly를 true로 하는것부터 이것의 시작이라고 생각함!
 * query용 서비스 command용 서비스를 분리할 수도 있음!
 * command - cud : 이 작업을 위한 친구를 분리할 수 있음!
 * 가장 좋은 포인트! db에 대한 엔드포인트를 분리할 수 있음 aws aurora ~~ read용 db, write용 db를 같이 분리를 하게 할 수 있음
 * writer db에 대한 엔드포인트 slave db에 대한 엔드포인트를 나누고 readOnly true가 걸리면 읽기 전용 db로 보내고 아니면 writer 전용 db로 보내기
 * aurora db 클러스터 모드를 쓰면 readOnly만 보고 분리해주는 친구도 있음!
 * spring에 readOnly값을 보고 엔드포인트 다르게 줄 수도 있음!
 * 하고 싶은 말은! transactional 에 잘 걸기로 했는데! readOnly true 에 대한 것을 query용 행위, command용 행위에 잘 나눠서 달았으면 좋겠음!ㄸ
 * method 마다 달게 되면 누락이 될 수 있는 실수를 할 수 있다는 문제가 있음!
 * 추천하는 방법은 service 클래스 상단에 readOnly 를 걸어버리고 cud 작업이 있따면 method 단위에서 transactional 걸어버리는 것을 추천!
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNumberFactory productNumberFactory;


    // 동시성 이슈 발생 가능성 있음!
    // 방법은 여러가지 unique index 이용해서 재시도 하는 방식
    // 동시접속이 한 번에 너무 ㅁ낳이 나오면! UUID 같은 것으로 해결하는 방식
    @Transactional
    public ProductResponse createProduct(ProductServiceRequest request) {
        // nextProductNumber
        String nextProductNumber = productNumberFactory.createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }





    @Transactional(readOnly = true)
    public List<ProductResponse> getSellingProducts(){
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .toList();
    }

}
