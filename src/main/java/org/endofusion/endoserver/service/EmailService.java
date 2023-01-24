package org.endofusion.endoserver.service;

import com.sun.mail.smtp.SMTPTransport;
import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.domain.token.ConfirmationToken;
import org.springframework.stereotype.Service;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static org.endofusion.endoserver.constant.EmailConstant.*;
import static org.endofusion.endoserver.constant.EmailType.*;

@Service
public class EmailService {

    public void sendEmail(User user, String siteUrl, ConfirmationToken confirmationToken, int emailIdentifier, String randomPassword) throws MessagingException {
        Message message = createEmail(user, siteUrl, confirmationToken, emailIdentifier, randomPassword);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(OUTLOOK_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    private Message createEmail(User user,String siteUrl,ConfirmationToken confirmationToken, int emailIdentifier, String randomPassword) throws MessagingException {
        String verifyURL = "";
        Message message = new MimeMessage(getEmailSession());
        MimeMultipart content = new MimeMultipart("related");
        BodyPart messageBodyPart = new MimeBodyPart();
        if(emailIdentifier == VERIFICATION_EMAIL || emailIdentifier == VERIFICATION_EMAIL_WITH_PASSWORD || emailIdentifier == SEND_RESET_PASSWORD_EMAIL) {
            verifyURL = siteUrl + "/verify/" + confirmationToken.getToken();
        }
        if(emailIdentifier == VERIFICATION_EMAIL) {
            messageBodyPart.setContent("<div><img style=\"width:150px;margin-top:10px\" src=\"cid:image\" /></div>\n" +
                    "Dear " + user.getFirstName() + ",<br>\n" +
                    "Please click the link below to verify your registration:<br>\n" +
                    "<h3><a href= " + verifyURL + " target=\"_self\">Activate Your Account</a></h3>\n" +
                    "Thank you,<br>\n" +
                    "Endofusion", "text/html");
        }
        if(emailIdentifier == VERIFICATION_EMAIL_WITH_PASSWORD) {
            messageBodyPart.setContent("<div><img style=\"width:150px;margin-top:10px\" src=\"cid:image\" /></div>\n" +
                    "Dear " + user.getFirstName() + ",<br>\n" +
                    "Your password is " + randomPassword + "<br>\n" +
                    "Please click first the link below to verify your registration:<br>\n" +
                    "<h3><a href= " + verifyURL + " target=\"_self\">Activate Your Account</a></h3>\n" +
                    "Thank you,<br>\n" +
                    "Endofusion", "text/html");
        }
        if(emailIdentifier == SEND_RESET_PASSWORD_EMAIL) {
            messageBodyPart.setContent("<div><img style=\"width:150px;margin-top:10px\" src=\"cid:image\" /></div>\n" +
                    "Dear " + user.getFirstName() + ",<br>\n" +
                    "Please click the link below to change your password:<br>\n" +
                    "<h3><a href= " + verifyURL + " target=\"_self\">New Password</a></h3>\n" +
                    "Thank you,<br>\n" +
                    "Endofusion",  "text/html");
        }
        if(emailIdentifier == REGISTER_EMAIL) {
            messageBodyPart.setContent("<div><img style=\"width:150px;margin-top:10px\" src=\"cid:image\" /></div>\n" +
                " <p>Hello " + user.getFirstName() + ",</p>\n" +
                " <p>Welcome to Endofusion! Thanks so much for joining our platform. We're looking forward to helping you with our products, so you can ...</p>\n" +
                " <p>We offer lots of ways to connect:</p>\n" +
                " <ul class=\"default-list\">\n" +
                " <li>Follow us on social media [<u>Twitter</u>, <u>Facebook</u>, <u>Instagram</u>, <u>LinkedIn</u>]</li>\n" +
                " <li><u>Subscribe to our newsletter</u> for special offers and discounts</li>\n" +
                " <li>Watch our <u>YouTube videos</u> about [topic]</li>\n" +
                " </ul>\n" +
                " <p>We're here to help! If you have any questions, please reply to this email or call our customer service team at 1-800-123-4567. We're available Monday through Friday, from 7 a.m. to 9 p.m. CST.</p>\n" +
                " <p>Sincerely,<br>Chris Smith, customer service agent</p>\n" +
                " </section>","text/html");
        }
        content.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource
                ("usr/src/endofusion.jpg");
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID","<image>");
        messageBodyPart.setFileName("Endofusion");
        content.addBodyPart(messageBodyPart);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(user.getEmail(), false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setContent(content);
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, OUTLOOK_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }
}
