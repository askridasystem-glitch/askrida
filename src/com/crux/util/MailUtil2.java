/**
 * Created by IntelliJ IDEA.
 * User: dono
 * Date: Dec 7, 2004
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
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class MailUtil2 {

    //askrida.co.id
    String mailSmtpHost = "mail.askrida.co.id";
    String mailSmtpPort = "25";
    String mailFrom = "sistem@askrida.co.id";
    String mailPass = "!askrid@15";

//    String mailFrom = "it.dept@askrida.com";
//    String mailPass = "!askrid@15";
    public static void main(String[] args) {
        String Cc = "prasetyodepe@gmail.com";
        String Subject = "Email from Java Tester";
        String Text = "haloo..haloo";

        String SmtpHost = "mail.askrida.co.id";
        String SmtpPort = "25";
        String From = "sistem@askrida.co.id";
        String Pass = "!askrid@15";
        String To = "prasetyo@askrida.co.id";

//        String SmtpHost = "webmail.askrida.com";
//        String SmtpPort = "465";
//        String From = "prasetyo.dwi@askrida.com";
//        String Pass = "@skridA00"; 
//        String To = "prasetyo@askrida.co.id";

        //sendEmail(mailFrom, To, Cc, Subject, Text, mailSmtpHost, mailSmtpPort, mailSmtpPort2);
        sendEmailTest(From, To, Cc, Pass, Subject, Text, SmtpHost, SmtpPort);
        //sendEmail2(From, To, Subject, Text);
    }

    public static void sendEmailTest(String from, String to, String cc, String pass, String subject, String text, String host, String port) {

        final String mailFrom = from;
        final String mailPass = pass;
        final String mailTo = to;
        final String mailCc = cc;

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        /*props.put("mail.smtp.socketFactory.port", port2);
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
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
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(mailCc));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    public void sendEmailMultiFileNoReceiver(String urx, String filename,
            String urx2, String filename2,
            String urx3, String filename3,
            String urx4, String filename4,
            String urx5, String filename5,
            String subject, String text) {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {
            //,supri@askrida.co.id,arfian@askrida.co.id
            String toList = "prasetyo@askrida.co.id";
            //String toList = Parameter.readString("SEND_EMAIL");
            String to[] = toList.split(",");

            InternetAddress[] addresses = new InternetAddress[to.length];

            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
//attach file 1
            String file = urx;
            String fileName = filename + ".pdf";

            FileDataSource source = new FileDataSource(file);
            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart2);

//attach file 2
            if (urx2 != null) {
                String file2 = urx2;
                String fileName2 = filename2 + ".pdf";

                source = new FileDataSource(file2);
                BodyPart messageBodyPart3 = new MimeBodyPart();
                messageBodyPart3.setDataHandler(new DataHandler(source));
                messageBodyPart3.setFileName(fileName2);
                multipart.addBodyPart(messageBodyPart3);
            }

//attach file 3
            if (urx3 != null) {
                String file3 = urx3;
                String fileName3 = filename3 + ".pdf";

                source = new FileDataSource(file3);
                BodyPart messageBodyPart4 = new MimeBodyPart();
                messageBodyPart4.setDataHandler(new DataHandler(source));
                messageBodyPart4.setFileName(fileName3);
                multipart.addBodyPart(messageBodyPart4);
            }

//attach file 4
            if (urx4 != null) {
                String file4 = urx4;
                String fileName4 = filename4 + ".pdf";

                source = new FileDataSource(file4);
                BodyPart messageBodyPart5 = new MimeBodyPart();
                messageBodyPart5.setDataHandler(new DataHandler(source));
                messageBodyPart5.setFileName(fileName4);
                multipart.addBodyPart(messageBodyPart5);
            }

//attach file 5
            if (urx5 != null) {
                String file5 = urx5;
                String fileName5 = filename5 + ".pdf";

                source = new FileDataSource(file5);
                BodyPart messageBodyPart6 = new MimeBodyPart();
                messageBodyPart6.setDataHandler(new DataHandler(source));
                messageBodyPart6.setFileName(fileName5);
                multipart.addBodyPart(messageBodyPart6);
            }

            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(String sender, String receiver, String subject, String text) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.askrida.co.id");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {
            String toList = getUser(receiver).getStEmail();
            String to[] = toList.split(",");

            InternetAddress[] addresses = new InternetAddress[to.length];

            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart =
                    new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            //Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(String receiver, String subject, String text) {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {
            String toList = receiver;
            String to[] = toList.split(",");
            InternetAddress[] addresses = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            //Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailCc(String receiver, String receiverCc, String subject, String text) {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {

            String toList = receiver;
            String to[] = toList.split(",");
            InternetAddress[] addresses = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);
            }

            String toListCc = receiverCc;
            String toCc[] = toListCc.split(",");
            InternetAddress[] addressesCc = new InternetAddress[toCc.length];
            for (int i = 0; i < toCc.length; i++) {
                addressesCc[i] = new InternetAddress(toCc[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.addRecipients(Message.RecipientType.CC, addressesCc);
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            //Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailBcc(String receiver, String receiverCc, String receiverBcc, String subject, String text) {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {

            String toList = receiver;
            String to[] = toList.split(",");
            InternetAddress[] addresses = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);

            }

            String toListCc = receiverCc;
            String toCc[] = toListCc.split(",");
            InternetAddress[] addressesCc = new InternetAddress[toCc.length];
            for (int i = 0; i < toCc.length; i++) {
                addressesCc[i] = new InternetAddress(to[i]);
            }

            String toListBcc = receiverBcc;
            String toBcc[] = toListBcc.split(",");
            InternetAddress[] addressesBcc = new InternetAddress[toBcc.length];
            for (int i = 0; i < toBcc.length; i++) {
                addressesBcc[i] = new InternetAddress(toBcc[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.addRecipients(Message.RecipientType.CC, addressesCc);
            message.addRecipients(Message.RecipientType.BCC, addressesBcc);
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            //Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailWithType(String receiver, String subject, String text, String urx, String filename, String type) {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {
            String toList = receiver;
            String to[] = toList.split(",");
            InternetAddress[] addresses = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            String file = urx;
            String fileName = filename + type;

//attach file 1
            FileDataSource source = new FileDataSource(file);
            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart2);

            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailWithTypeCc(String receiver, String receiverCc, String subject, String text, String urx, String filename, String type) {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {
            String toList = receiver;
            String to[] = toList.split(",");
            InternetAddress[] addresses = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);
            }

            String toListCc = receiverCc;
            String toCc[] = toListCc.split(",");
            InternetAddress[] addressesCc = new InternetAddress[toCc.length];
            for (int i = 0; i < toCc.length; i++) {
                addressesCc[i] = new InternetAddress(toCc[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.addRecipients(Message.RecipientType.CC, addressesCc);
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            String file = urx;
            String fileName = filename + type;

//attach file 1
            FileDataSource source = new FileDataSource(file);
            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart2);

            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailWithTypeBcc(String receiver, String receiverCc, String receiverBcc, String subject, String text, String urx, String filename, String type) {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {
            String toList = receiver;
            String to[] = toList.split(",");
            InternetAddress[] addresses = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);

            }

            String toListCc = receiverCc;
            String toCc[] = toListCc.split(",");
            InternetAddress[] addressesCc = new InternetAddress[toCc.length];
            for (int i = 0; i < toCc.length; i++) {
                addressesCc[i] = new InternetAddress(to[i]);
            }

            String toListBcc = receiverBcc;
            String toBcc[] = toListBcc.split(",");
            InternetAddress[] addressesBcc = new InternetAddress[toBcc.length];
            for (int i = 0; i < toBcc.length; i++) {
                addressesBcc[i] = new InternetAddress(toBcc[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.addRecipients(Message.RecipientType.CC, addressesCc);
            message.addRecipients(Message.RecipientType.BCC, addressesBcc);
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            String file = urx;
            String fileName = filename + type;

//attach file 1
            FileDataSource source = new FileDataSource(file);
            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart2);

            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailMultiFile(String urx, String filename,
            String urx2, String filename2,
            String urx3, String filename3,
            String urx4, String filename4,
            String urx5, String filename5,
            String receiver, String Cc, String subject, String text) {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom, mailPass);
                    }
                });

        try {
            //,supri@askrida.co.id,arfian@askrida.co.id
            //String toList = "prasetyo@askrida.co.id,supri@askrida.co.id,arfian@askrida.co.id";
            String toList = receiver;
            String to[] = toList.split(",");

            InternetAddress[] addresses = new InternetAddress[to.length];

            for (int i = 0; i < to.length; i++) {
                addresses[i] = new InternetAddress(to[i]);
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subject);
            if (Cc != null) {
                message.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(Cc));
            }

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(text);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment

//attach file 1
            String file = urx;
            String fileName = filename + ".pdf";

            FileDataSource source = new FileDataSource(file);
            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart2);

//attach file 2
            if (urx2 != null) {
                String file2 = urx2;
                String fileName2 = filename2 + ".pdf";

                source = new FileDataSource(file2);
                BodyPart messageBodyPart3 = new MimeBodyPart();
                messageBodyPart3.setDataHandler(new DataHandler(source));
                messageBodyPart3.setFileName(fileName2);
                multipart.addBodyPart(messageBodyPart3);
            }

//attach file 3
            if (urx3 != null) {
                String file3 = urx3;
                String fileName3 = filename3 + ".pdf";

                source = new FileDataSource(file3);
                BodyPart messageBodyPart4 = new MimeBodyPart();
                messageBodyPart4.setDataHandler(new DataHandler(source));
                messageBodyPart4.setFileName(fileName3);
                multipart.addBodyPart(messageBodyPart4);
            }

//attach file 4
            if (urx4 != null) {
                String file4 = urx4;
                String fileName4 = filename4 + ".pdf";

                source = new FileDataSource(file4);
                BodyPart messageBodyPart5 = new MimeBodyPart();
                messageBodyPart5.setDataHandler(new DataHandler(source));
                messageBodyPart5.setFileName(fileName4);
                multipart.addBodyPart(messageBodyPart5);
            }

//attach file 4
            if (urx5 != null) {
                String file5 = urx5;
                String fileName5 = filename5 + ".pdf";

                source = new FileDataSource(file5);
                BodyPart messageBodyPart6 = new MimeBodyPart();
                messageBodyPart6.setDataHandler(new DataHandler(source));
                messageBodyPart6.setFileName(fileName5);
                multipart.addBodyPart(messageBodyPart6);
            }

            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
