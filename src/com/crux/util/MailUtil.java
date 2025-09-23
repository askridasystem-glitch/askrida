/**
 * Created by IntelliJ IDEA.
 * User: DONI
 * Date: 26 november 2015
 * Time: 3:01:33 PM
 * To change this template use Options | File Templates.
 */
package com.crux.util;

import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.util.Properties;
import java.util.Date;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailUtil {

   private final static transient LogManager logger = LogManager.getInstance(MailUtil.class);

   private String property = "mail.smtp.host";
   //private String property = "mail.askrida.co.id";
   private String host ;//="telkomsel.co.id";
   private String from ;//= "sidon2@excite.com" ;
   private String []to ;//="sidon2@excite.com";
   private String subject ;//= "The subject of mail!";
   private String message ;//= "Message Goes Here!";
   private String port ;
   private String passwordfrom;


   public MailUtil(String host, String port, String from, String password, String[] to, String subject, String message)
   {
      this.host = host;
      this.from = from;
      this.to   = to;
      this.subject = subject;
      this.message = message;
      this.port = port;
      this.passwordfrom = password;
   }

   public void sendMail() throws MessagingException {

       try {
            Properties prop = new Properties();
            prop.put("mail.smtp.host", host);
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.port", port);
            prop.put("mail.smtp.starttls.enable", "true");

            SmtpAuthenticator authentication = new SmtpAuthenticator();

            Session ses = Session.getDefaultInstance(prop, authentication);

            MimeMessage msg = new MimeMessage(ses);
            msg.setFrom(new InternetAddress(from));

            InternetAddress[] adresses = new InternetAddress[to.length];
            for (int i=0;i<to.length;i++)
            {
               InternetAddress address = new InternetAddress(to[i]);
               adresses[i] = address;
            }

            if (to!=null)
            {
               msg.addRecipients(Message.RecipientType.TO, adresses);
            }
            msg.setSubject(subject);
            msg.setText(message);
            msg.setSentDate(new Date());

            Transport.send(msg);
            
       }catch (MessagingException mex) {
           logger.logWarning("Gagal kirim email : "+ mex.getMessage());
           mex.printStackTrace();
        }
   }

   private class SmtpAuthenticator extends Authenticator {

        public String username = from;
        public String password = passwordfrom;

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

   public static void main(String [] args) throws Exception {

       MailUtil mail = new MailUtil("mail.askrida.co.id","25","doni@askrida.co.id", "@skrid@2", new String[]{"ahmad.rhodoni@gmail.com","dhoni7@rocketmail.com","doni@askrida.co.id"}, "test kirim email dari sistem", "tes kirim email dari sistem \n yuhuuu \n ahahaha");

       mail.sendMail();

   }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

   public void sendEmail(String from, String to, String subject, String text) {

        final String mailFrom = "staffti@askrida.co.id";
        final String mailPass = "staffti";
        final String mailTo = getUser(to).getStEmail();
        final String userTo = getUser(to).getStUserName().toUpperCase();

        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.askrida.co.id");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        /*
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");*/

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailTo));
            message.setSubject(subject);
            message.setText(text
                    + "\n \n \n from " + userTo);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

   
}
