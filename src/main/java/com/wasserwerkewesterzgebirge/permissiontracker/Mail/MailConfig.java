package com.wasserwerkewesterzgebirge.permissiontracker.Mail;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Configuration class for the mail properties.
 */
@Configuration
public class MailConfig {
    /**
     * The mail properties.
     */
    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.smtp.starttls.enable}")
    private String starttls_enable;

    @Value("${mail.transport.protocol}")
    private String protocol;

    /**
     * The mail content.
     */
    @Bean
    @Qualifier("messageContent")
    public Message mailInsertProperties() {
        return new MimeMessage(Session.getInstance(mailProperties()));
    }

    /**
     * The mail properties.
     */
    @Bean
    @Qualifier("mailProperties")
    public Properties mailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.starttls.enable", starttls_enable);
        return properties;
    }
}
