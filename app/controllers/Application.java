package controllers;

import java.io.File;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import play.Logger;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import com.typesafe.config.ConfigFactory;

public class Application extends Controller {

    public static Result index() {
    		
		Promise<Boolean> promiseOfMail = Promise.promise(
		  new Function0<Boolean>() {
		    public Boolean apply() {
		    	 try {
		    		File file = new File("C:/test.txt");
					sendMail("sender@gmail.com",
							"Test User",
							new String[] {"receiver@gmail.com"},
							null,
							null,
							"Email title",
							"This is test message with plain text",
							"<html><b>This is html message</b></html>",
							new File[]{file});
					Logger.debug("Email was sent without any problems");
					return true;
				} catch (EmailException e) {
					Logger.error("Error occurred and your email was not sent", e);
					return false;
				}
		    }
		  }
		);

        return ok(index.render());
    }
    
    private static void sendMail(String fromEmail, String fromName, String[] to, String[] cc, String[] bcc, String subject, String txtMessage, String htmlMessage, File[] attachments) throws EmailException {
    	if(htmlMessage != null) {
    		HtmlEmail email = prepareMail(new HtmlEmail(), fromEmail, fromName, to, cc, bcc, subject);
    		email.setHtmlMsg(htmlMessage);
    		email.setTextMsg(txtMessage);
    		
    		// Add attachments
    		if(attachments != null && attachments.length > 0) {
        		for(File fileAttachment : attachments) {
        			EmailAttachment attachment = new EmailAttachment();
        			attachment.setPath(fileAttachment.getAbsolutePath());
            		attachment.setDisposition(EmailAttachment.ATTACHMENT);
            		attachment.setName(fileAttachment.getName());
            		email.attach(attachment);
        		}
    		}
    		
    		email.send();
    	} else if(attachments != null && attachments.length > 0) {
    		// Create the email message
    		MultiPartEmail email = prepareMail(new MultiPartEmail(), fromEmail, fromName, to, cc, bcc, subject);
    		email.setMsg(txtMessage);
    		
    		// Add attachments
    		for(File fileAttachment : attachments) {
    			EmailAttachment attachment = new EmailAttachment();
    			attachment.setPath(fileAttachment.getAbsolutePath());
        		attachment.setDisposition(EmailAttachment.ATTACHMENT);
        		attachment.setName(fileAttachment.getName());
        		email.attach(attachment);
    		}
    		
    		email.send();
    	} else {
    		SimpleEmail email = prepareMail(new SimpleEmail(), fromEmail, fromName, to, cc, bcc, subject);
    		email.setMsg(txtMessage);
    		email.send();
    	}
    	
    }
    
    private static <T extends Email> T prepareMail(T email, String fromEmail, String fromName, String[] to, String[] cc, String[] bcc, String subject) throws EmailException {
    	email.setHostName(getMailConfParam("hostname"));
    	email.setSmtpPort(Integer.valueOf(getMailConfParam("smtpport")));
    	email.setAuthenticator(new DefaultAuthenticator(getMailConfParam("username"), getMailConfParam("password")));
    	email.setSSLOnConnect(Boolean.valueOf(getMailConfParam("sslonconnect")));
    	
    	email.setFrom(fromEmail, fromName != null ? fromName : fromEmail);
    	email.setSubject(subject);
    	
    	if(to != null) {
    		for(String toElement : to) {
    			email.addTo(toElement);
    		}
    	}
    	if(cc != null) {
    		for(String ccElement : cc) {
    			email.addCc(ccElement);
    		}
    	}
    	if(bcc != null) {
    		for(String bccElement : bcc) {
    			email.addBcc(bccElement);
    		}
    	}
    	return email;
    }
    
    private static String getMailConfParam(String name) {
    	return ConfigFactory.load().getString("email." + name);
    }
    
}
