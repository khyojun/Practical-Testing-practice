package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
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

    @Spy // 실제 객체를 기반으로 만들어짐!, 일부만 쓰고 일부만 모킹하고 싶을때 사용
    //@Mock
    private MailSendClient mailSendClient; // 더 편하게 작성 extendwith mockitoExtension.class 같이 써야함

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks // Mock으로 선언된 친구를 해당 어노테이션 지정된 클래스에 넣어준다.
    private MailService mailService;


    @DisplayName("메일 전송 테스트")
    @Test
    void test(){
        //given

//        when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(true); @mock 일때

        doReturn(true) // spy 일때 활용하는 방시
                .when(mailSendClient).sendEmail(anyString(), anyString(), anyString(), anyString());

//
//        when(mailSendHistoryRepository.save(any(MailSendHistory.class)))
//                .thenReturn()
        // mock 객체만 만들면 기본값을 던짐 -> 빈 값

        //when
        boolean result = mailService.sendMail("", "", "", "");

        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));


        //then
        assertThat(result).isTrue();

    }


}