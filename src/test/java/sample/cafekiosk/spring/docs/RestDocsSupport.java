package sample.cafekiosk.spring.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
//@SpringBootTest // 이렇게 할 때 문서를 작성할때? 굳이 스프링 서버를 띄워야하나?
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController()) // 이러면 스프링 의존성을 없애고 진행할 수 있음. 여기에 어느 컨트롤러 할 건지 넣어줘야함
                .apply(documentationConfiguration(provider))
                .build();
    }

    protected abstract Object initController();

}
