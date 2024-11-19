package sample.cafekiosk.spring.domain.history.mail;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class MailSendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromMail;
    private String toEmail;
    private String subject;
    private String content;


    @Builder
    private MailSendHistory(String fromMail, String toEmail, String subject, String content) {
        this.fromMail = fromMail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.content = content;
    }
}
