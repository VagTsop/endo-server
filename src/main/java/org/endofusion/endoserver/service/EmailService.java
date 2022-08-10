package org.endofusion.endoserver.service;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static org.endofusion.endoserver.constant.EmailConstant.*;

@Service
public class EmailService {

    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException, IOException {
        Message message = createEmail(firstName, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(OUTLOOK_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    private Message createEmail(String firstName, String password, String email) throws MessagingException,
            IOException {
        Message message = new MimeMessage(getEmailSession());
        MimeMultipart content = new MimeMultipart("related");
        // ContentID is used by both parts
        String cid = ContentIdGenerator.getContentId();
        MimeBodyPart mainPart = new MimeBodyPart();
        mainPart.setText("<section>\n" +
                " <div><img style=\"width:150px;margin-top:10px\" src=\"cid:"+cid + "\" /></div>\n"+
                " <p>Hello " + firstName + ",</p>\n" +
                " <p>Welcome to [product name, service, subscription, etc.]! Thanks so much for [buying, joining, subscribing, signing-up for, etc.] We're looking forward to [helping you with X, supporting your X efforts, providing you with X], so you can [outcome of using our product, service, subscription, etc.].</p>\n" +
                " <p>[Optional section: Where words are underlined, add hyperlinks]</p>\n" +
                " <p>We offer lots of ways to connect:</p>\n" +
                " <ul class=\"default-list\">\n" +
                " <li>Join us on [date] for [<u>event</u>]</li>\n" +
                " <li>Follow us on social media [<u>Twitter</u>, <u>Facebook</u>, <u>Instagram</u>, <u>LinkedIn</u>]</li>\n" +
                " <li><u>Subscribe to our newsletter</u> for special offers and discounts</li>\n" +
                " <li>Watch our <u>YouTube videos</u> about [topic]</li>\n" +
                " </ul>\n" +
                " <p>We're here to help! If you have any questions, please reply to this email or call our customer service team at 1-800-123-4567. We're available Monday through Friday, from 7 a.m. to 9 p.m. CST.</p>\n" +
                " <p>Sincerely,<br>Chris Smith, customer service agent</p>\n" +
                " </section>","US-ASCII", "html");

        content.addBodyPart(mainPart);
        // Image part
        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.attachFile("src/endofusion.jpg");
        imagePart.setContentID("<" + cid + ">");
        imagePart.setDisposition(MimeBodyPart.INLINE);
        content.addBodyPart(imagePart);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setContent(content);
  //    message.setText("Hello " + firstName + ", \n \n Your new account password is: " + password + "\n \n The Support Team");
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
