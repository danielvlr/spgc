package br.gov.ce.arce.spgc.client.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailClient {

    private final JavaMailSender mailSender;
    private final String activeProfile;

    public EmailClient(@Value("${spring.profiles.active}") String activeProfile,
                       JavaMailSender mailSender) {
        this.activeProfile = activeProfile;
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String from, String subject, String content){
        if(activeProfile.equals("prod")){
            try {
                var message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                helper.setTo(to);
                helper.setFrom(from);
                helper.setText(content, true);
                helper.setSubject(subject);
                mailSender.send(message);
            }catch (Throwable e){
                throw new RuntimeException("Erro no serviço de email.");
            }
        }
    }
}
