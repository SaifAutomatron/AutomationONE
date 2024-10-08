package Utilities.Common;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import Utilities.Excel.EmailData;
import Utilities.Excel.EnvironmentData;
import lombok.SneakyThrows;

public class SendMail {
		
	@SneakyThrows
	public static void sendOutputMail(String path,String status)
	{
		EnvironmentData.getInstance().fetchEnvironmentData();
		HashMap<String, String> envMap = EnvironmentData.getInstance().environmentDataMap;
		EmailData.getInstance().fetchEmailData();
		HashMap<String, String> emailMap=EmailData.getInstance().getEmailDataMap();
		
		if(emailMap.get("SMTPHost")!=null && emailMap.get("SMTPPort")!=null
		   && emailMap.get("Sender")!=null && emailMap.get("Receiver")!=null
		   && emailMap.get("MessageSubject")!=null && emailMap.get("MessageText")!=null
		   && emailMap.get("SMTPHost")!="" && emailMap.get("SMTPPort")!=""
		   && emailMap.get("Sender")!="" && emailMap.get("Receiver")!=""
		   && emailMap.get("MessageSubject")!="" && emailMap.get("MessageText")!="")
	    {
			
			final String username=emailMap.get("User");
			final String password=emailMap.get("Password");
			String envName="";
			String fileSystem="";
			String capacity="";
			int utilizationSizeBefore=0;
			int runCleanupCount=0;
			
			Properties props=new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "false");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", emailMap.get("SMTPHost"));
			props.put("mail.smtp.port", emailMap.get("SMTPPort"));
			
		Session session=Session.getInstance(props,new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			Message message=new MimeMessage(session);
			message.setFrom(new InternetAddress(emailMap.get("Sender")));
			message.setRecipients(Message.RecipientType.TO,
			 InternetAddress.parse(emailMap.get("Receiver")));
			message.setSubject(emailMap.get("MessageSubject")+" | "+envMap.get("ENVIRONMENT")+" - "+status);
			message.setText(emailMap.get("MessageText"));
			BodyPart messageBodyPart=new MimeBodyPart();
			Multipart multipart =new MimeMultipart();
			messageBodyPart.setText(emailMap.get("MessageText"));
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart=new MimeBodyPart();
			DataSource source=new FileDataSource(path);
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
