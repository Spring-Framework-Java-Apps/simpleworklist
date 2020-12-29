package org.woehlke.simpleworklist.user.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.UUIDGenerationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.application.ApplicationProperties;
import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecovery;
import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecoveryRepository;
import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecoveryStatus;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserPasswordRecoveryServiceImpl implements UserPasswordRecoveryService {

    private final UserPasswordRecoveryRepository userPasswordRecoveryRepository;
    private final ApplicationProperties applicationProperties;
    private final TokenGeneratorService tokenGeneratorService;
    private final JavaMailSender mailSender;

    @Autowired
    public UserPasswordRecoveryServiceImpl(UserPasswordRecoveryRepository userPasswordRecoveryRepository, ApplicationProperties applicationProperties, TokenGeneratorService tokenGeneratorService, JavaMailSender mailSender) {
        this.userPasswordRecoveryRepository = userPasswordRecoveryRepository;
        this.applicationProperties = applicationProperties;
        this.tokenGeneratorService = tokenGeneratorService;
        this.mailSender = mailSender;
    }

    @Override
    public UserPasswordRecovery findByToken(String token) {
        return userPasswordRecoveryRepository.findByToken(token);
    }

    @Override
    public boolean passwordRecoveryIsRetryAndMaximumNumberOfRetries(String email) {
        UserPasswordRecovery earlierOptIn = userPasswordRecoveryRepository.findByEmail(email);
        return earlierOptIn == null?false:earlierOptIn.getNumberOfRetries() >= applicationProperties.getRegistration().getMaxRetries();
    }

    @Override
    public void passwordRecoveryCheckIfResponseIsInTime(String email) {
        UserPasswordRecovery earlierOptIn = userPasswordRecoveryRepository.findByEmail(email);
        if (earlierOptIn != null) {
            Date now = new Date();
            if ((applicationProperties.getRegistration().getTtlEmailVerificationRequest() + earlierOptIn.getRowCreatedAt().getTime()) < now.getTime()) {
                userPasswordRecoveryRepository.delete(earlierOptIn);
            }
        }
    }

    @Async
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoverySendEmailTo(String email) {
        UserPasswordRecovery earlierOptIn = userPasswordRecoveryRepository.findByEmail(email);
        UserPasswordRecovery o = new UserPasswordRecovery();
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        } else {
            o.setUuid(UUID.randomUUID().toString());
        }
        o.setDoubleOptInStatus(UserPasswordRecoveryStatus.PASSWORD_RECOVERY_SAVED_EMAIL);
        o.setEmail(email);
        String token = tokenGeneratorService.getToken();
        o.setToken(token);
        log.info("To be saved: " + o.toString());
        o = userPasswordRecoveryRepository.saveAndFlush(o);
        log.info("Saved: " + o.toString());
        this.sendEmailForPasswordReset(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoverySentEmail(UserPasswordRecovery o) {
        o.setDoubleOptInStatus(UserPasswordRecoveryStatus.PASSWORD_RECOVERY_SENT_EMAIL);
        log.info("about to save: " + o.toString());
        userPasswordRecoveryRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoveryClickedInEmail(UserPasswordRecovery o) {
        o.setDoubleOptInStatus(UserPasswordRecoveryStatus.PASSWORD_RECOVERY_CLICKED_IN_MAIL);
        userPasswordRecoveryRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoveryDone(UserPasswordRecovery o) {
        o.setDoubleOptInStatus(UserPasswordRecoveryStatus.PASSWORD_RECOVERY_STORED_CHANGED);
        o = userPasswordRecoveryRepository.saveAndFlush(o);
        userPasswordRecoveryRepository.delete(o);
    }

    private void sendEmailForPasswordReset(UserPasswordRecovery o) {
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
            log.warn(ex.getMessage() + " for " + o.toString());
            success = false;
        }
        if (success) {
            this.passwordRecoverySentEmail(o);
        }
        log.info("Sent MAIL: " + o.toString());
    }
}
