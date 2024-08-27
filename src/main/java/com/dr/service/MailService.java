package com.dr.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmpRegistrationMail(String to, String subject, String body){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        javaMailSender.send(simpleMailMessage);
    }

    public void sendEmpRegistrationMailWithHtmlLink(String to, String subject, String body, String link) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        // Creating the email body with HTML content including the href link
        String htmlBody = "<html><body>"
                + "<p>" + body + "</p>"
                + "<p>Click the following link to: "
                + "<a href=\"" + link + "\">Reset password</a></p>"
                + "</body></html>";
        mimeMessageHelper.setText(htmlBody, true); // The 'true' flag indicates that the text is HTML
        javaMailSender.send(mimeMessage);
    }
}
