package com.zw.admin.server.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

	public static void main(String[] args) {
		sendMail("406708628@qq.com", 1, 123456);
	}

	/**
	 * 发送验证码
	 * 
	 * @param to   发往邮箱
	 * @param type 类型1注册 2其他
	 * @return
	 */
	public static boolean sendMail(String to, int type, int code) {

		try {

			Properties props = new Properties();

			props.put("username", "exops@fftai.com");

			props.put("password", "kJRMUTF8wMEQtSbE");

			props.put("mail.transport.protocol", "smtp");

			props.put("mail.smtp.host", "smtp.exmail.qq.com");

			props.put("mail.smtp.auth", "true");

			props.put("mail.smtp.port", "465");

			props.put("mail.smtp.socketFactory.port", "465");

			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			props.put("mail.smtp.socketFactory.fallback", "false");

			Session mailSession = Session.getDefaultInstance(props);

			Message msg = new MimeMessage(mailSession);

			msg.setFrom(new InternetAddress("exops@fftai.com"));

			msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			msg.setSubject("EXOPS contactUs");

//            msg.setContent("<h1>此邮件为官方激活邮件！请点击下面链接完成激活操作！</h1><h3><a href='http://localhost:8080/fuliye-api/api/sys/emailUrl?uid="+id+"'>http://www.wanshi.com/MyDemo/ABCDKJK838845490SERVLET</a></h3>","text/html;charset=UTF-8");
			if (type == 1) {
				msg.setContent("您的验证码为: " + code + "<br/>\r\n" + "Your verification code is: " + code,
						"text/html;charset=UTF-8");
			} else if (type == 2) { // 激活邮件内容
				msg.setContent("<style>#link a{color:red;}</style><h1>Hello!<br/>\r\n"
						+ "Welcome to EXOPS. This message was generated automatically.<br/>\r\n"
						+ "To activate your account we need you to click the link below. This will verify that you are who you say you are.<br/>\r\n"
						+ "<h3 id=\"link\">" + "link" + "?name=" + "token" + "</h3><br/>\r\n"
						+ "Don’t waste too much time though. This link is only valid for <font color=\"red\">30 days.</font><br/>\r\n"
						+ "Thanks.<br/>\r\n"
						+ "If you need any help or have questions, please email to info@fftai.com.<br/>\r\n"
						+ "Best Regards,<br/>\r\n" + "Fourier Intelligence Account Team<br/>\r\n",
						"text/html;charset=UTF-8");
			} else if (type == 3) { // 忘记密码邮件内容
				msg.setContent(
						"<style>#link a{color:red;}</style><h3 id=\"link\">" + "link" + "?name=" + "token" + "</h3>",
						"text/html;charset=UTF-8");
			}

			msg.saveChanges();

			Transport transport = mailSession.getTransport("smtp");

			transport.connect(props.getProperty("mail.smtp.host"), props.getProperty("username"),
					props.getProperty("password"));

			transport.sendMessage(msg, msg.getAllRecipients());

			transport.close();

		} catch (Exception e) {

			e.printStackTrace();

			System.out.println(e);

			return false;

		}

		return true;

	}
}
