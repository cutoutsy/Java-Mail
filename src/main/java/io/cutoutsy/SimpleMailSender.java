package io.cutoutsy;

import io.cutoutsy.utils.ConfigParas;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

public class SimpleMailSender {
    //send mail by text
    public boolean sendTextMail(MailSenderInfo mailInfo){
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();

        if (mailInfo.isValidate()){
            authenticator = new MyAuthenticator(mailInfo.getUsername(),mailInfo.getPassword());
        }

        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            Message mailMessage = new MimeMessage(sendMailSession);

            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);

            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());

            String mailcontent = mailInfo.getContent();
            mailMessage.setText(mailcontent);

            Transport.send(mailMessage);
            return true;
        }catch(MessagingException ex){
            ex.printStackTrace();
        }
        return false;
    }

    //send mail by html
    public boolean sendHtmlMail(MailSenderInfo mailInfo){
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();

        if (mailInfo.isValidate()){
            authenticator = new MyAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
        }

        Session sendMailSession = Session.getDefaultInstance(pro,authenticator);

        try {
            Message mailMessage = new MimeMessage(sendMailSession);

            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);

            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());

            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();

            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            mailMessage.setContent(mainPart);

            Transport.send(mailMessage);
            return true;
        }catch (MessagingException ex){
            ex.printStackTrace();
        }
        return false;
    }


    public static void SendMail(String toAddress, String subject, String content){

        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(ConfigParas.smtp_server_host);
        mailInfo.setMailServerPort(ConfigParas.smtp_server_port);
        mailInfo.setValidate(true);

        mailInfo.setUsername(ConfigParas.smtp_username);
        mailInfo.setPassword(ConfigParas.smtp_password);
        mailInfo.setFromAddress(ConfigParas.smtp_from_address);

        mailInfo.setToAddress(toAddress);
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);

        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);
    }

    public static void main(String[] args) {
        String toAddress = "774844724@qq.com";
        String subject = "通知";
        String content = "你有一门课老师了";
        SendMail(toAddress, subject, content);
    }
}
