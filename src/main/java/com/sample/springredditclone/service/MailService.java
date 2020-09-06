package com.sample.springredditclone.service;


import com.sample.springredditclone.exception.SpringRedditException;
import com.sample.springredditclone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final  MailContentBuilder mailContentBuilder;
    private final JavaMailSender mailSender;

    @Async
     void sendEmail(NotificationEmail notificationEmail){

        MimeMessagePreparator messagePreparator = mimeMessage ->  {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setFrom("abduljabbar@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));

        };

        try{
            mailSender.send(messagePreparator);
            log.info("Activation Email Sent !!!!");
        }catch (MailException me){
            log.error("Exception ", me);
            throw new SpringRedditException("Exception while sending mail");
        }
    }
}
