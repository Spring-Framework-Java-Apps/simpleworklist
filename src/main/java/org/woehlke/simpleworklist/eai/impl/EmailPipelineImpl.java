package org.woehlke.simpleworklist.eai.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.eai.EmailPipeline;
import org.woehlke.simpleworklist.entities.UserPasswordRecovery;
import org.woehlke.simpleworklist.entities.UserRegistration;
import org.woehlke.simpleworklist.services.UserPasswordRecoveryService;
import org.woehlke.simpleworklist.services.UserRegistrationService;


@Service
public class EmailPipelineImpl implements EmailPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailPipelineImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserPasswordRecoveryService userPasswordRecoveryService;

    @Autowired
    protected ApplicationProperties applicationProperties;

    @Async
    @Override
    public void sendEmailToRegisterNewUser(UserRegistration o) {
        String urlHost = applicationProperties.getRegistration().getUrlHost();
        String mailFrom= applicationProperties.getRegistration().getMailFrom();
        boolean success = true;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(o.getEmail());
        msg.setText(
                "Dear new User, "
                        + "thank you for registring at Simple Worklist. \n"
                        + "Please validate your email and go to URL: \nhttp://" + urlHost + "/confirm/" + o.getToken()
                        + "\n\nSincerely Yours, The Team");
        msg.setSubject("Your Registration at Simple Worklist");
        msg.setFrom(mailFrom);
        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            LOGGER.warn(ex.getMessage() + " for " + o.toString());
            success = false;
        }
        if (success) {
            userRegistrationService.registrationSentEmail(o);
        }
        LOGGER.info("Sent MAIL: " + o.toString());
    }

    @Async
    @Override
    public void sendEmailForPasswordReset(UserPasswordRecovery o) {
        String urlHost = applicationProperties.getRegistration().getUrlHost();
        String mailFrom= applicationProperties.getRegistration().getMailFrom();
        boolean success = true;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(o.getEmail());
        msg.setText(
                "Dear User, "
                       + "for Password Reset at SimpleWorklist, "
                       + "Please go to URL: \nhttp://" + urlHost + "/passwordResetConfirm/" + o.getToken()
                       + "\n\nSincerely Yours, The Team");
        msg.setSubject("Password Reset at Simple Worklist");
        msg.setFrom(mailFrom);
        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            LOGGER.warn(ex.getMessage() + " for " + o.toString());
            success = false;
        }
        if (success) {
            userPasswordRecoveryService.passwordRecoverySentEmail(o);
        }
        LOGGER.info("Sent MAIL: " + o.toString());
    }
}
