package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_type in ('SELLING', 'HOLD');
     *
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);


    List<Product> findAllByProductNumberIn(List<String> productNumbers);


    @Query(value = "select p.product_number from Product p order by id desc limit 1", nativeQuery = true)
    String findLatestProduct(); // 뭐 repository 구현 내용에 관계없이 작성을 해야한다. 라는게 중요했음 테스트코드 관련해서 어떤 형태든 상관없음
}
