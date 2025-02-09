package Utilities.Common;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import Utilities.Excel.EmailData;
import Utilities.Excel.EnvironmentData;
import lombok.SneakyThrows;

public class SendMail {

    @SneakyThrows
    public static void sendOutputMail(String path, String status) {
        EnvironmentData.getInstance().fetchEnvironmentData();
        HashMap<String, String> envMap = EnvironmentData.getInstance().environmentDataMap;
        EmailData.getInstance().fetchEmailData();
        HashMap<String, String> emailMap = EmailData.getInstance().getEmailDataMap();

        if (emailMap.get("SMTPHost") != null && emailMap.get("SMTPPort") != null
                && emailMap.get("Sender") != null && emailMap.get("Receiver") != null
                && emailMap.get("MessageSubject") != null && emailMap.get("MessageText") != null
                && !emailMap.get("SMTPHost").isEmpty() && !emailMap.get("SMTPPort").isEmpty()
                && !emailMap.get("Sender").isEmpty() && !emailMap.get("Receiver").isEmpty()
                && !emailMap.get("MessageSubject").isEmpty() && !emailMap.get("MessageText").isEmpty()) {

            final String username = emailMap.get("User");
            final String password = emailMap.get("Password");

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", emailMap.get("SMTPHost"));
            props.put("mail.smtp.port", emailMap.get("SMTPPort"));

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailMap.get("Sender")));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailMap.get("Receiver")));
                message.setSubject(emailMap.get("MessageSubject") + " | " + envMap.get("ENVIRONMENT") + " - " + status);
                message.setText(emailMap.get("MessageText"));

                BodyPart messageBodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();
                messageBodyPart.setText(emailMap.get("MessageText"));
                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(path);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(new File(path).getName());
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
