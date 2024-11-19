package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    //@Spy // 실제 객체를 기반으로 만들어짐!, 일부만 쓰고 일부만 모킹하고 싶을때 사용
    @Mock
    private MailSendClient mailSendClient; // 더 편하게 작성 extendwith mockitoExtension.class 같이 써야함

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks // Mock으로 선언된 친구를 해당 어노테이션 지정된 클래스에 넣어준다.
    private MailService mailService;


    @DisplayName("메일 전송 테스트")
    @Test
    void test(){
        //given
        // given 절에 when 이라는 문법? 이 있음! 이거 given인데 문법은 when이네?
        // 고민끝에 나온게 BDDMockito 임!
//        Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(true); //@mock 일때
        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true); // BDDMockito 방식 -> 이게 좀 더 자연스러운 맥락의 느낌이 듬!
        // 앞으로는 안 헷갈리게 bddMockito 로 작성해도 좋을 거 같음!
//        doReturn(true) // spy 일때 활용하는 방시
//                .when(mailSendClient).sendEmail(anyString(), anyString(), anyString(), anyString());



        //when
        boolean result = mailService.sendMail("", "", "", "");

        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));


        //then
        assertThat(result).isTrue();

    }


}