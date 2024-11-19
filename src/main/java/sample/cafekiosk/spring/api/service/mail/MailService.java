package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {

        boolean result= mailSendClient.sendEmail(fromEmail, toEmail, subject, content); // 요것만 stubbing 다른건 동일하게 작동 이럴때 spy 활용

        if(result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                    .fromMail(fromEmail)
                    .toEmail(toEmail)
                    .subject(subject)
                    .content(content)
                    .build());

            mailSendClient.a();
            mailSendClient.b();


            return true;
        }
        return false;
    }
}
