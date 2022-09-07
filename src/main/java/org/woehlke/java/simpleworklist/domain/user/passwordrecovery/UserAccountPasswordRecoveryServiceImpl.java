package org.woehlke.java.simpleworklist.domain.user.passwordrecovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.java.simpleworklist.domain.user.token.TokenGeneratorService;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountPasswordRecoveryServiceImpl implements UserAccountPasswordRecoveryService {

    private final UserAccountPasswordRecoveryRepository userAccountPasswordRecoveryRepository;
    private final SimpleworklistProperties simpleworklistProperties;
    private final TokenGeneratorService tokenGeneratorService;
    private final JavaMailSender mailSender;

    @Autowired
    public UserAccountPasswordRecoveryServiceImpl(UserAccountPasswordRecoveryRepository userAccountPasswordRecoveryRepository, SimpleworklistProperties simpleworklistProperties, TokenGeneratorService tokenGeneratorService, JavaMailSender mailSender) {
        this.userAccountPasswordRecoveryRepository = userAccountPasswordRecoveryRepository;
        this.simpleworklistProperties = simpleworklistProperties;
        this.tokenGeneratorService = tokenGeneratorService;
        this.mailSender = mailSender;
    }

    @Override
    public UserAccountPasswordRecovery findByToken(String token) {
        return userAccountPasswordRecoveryRepository.findByToken(token);
    }

    @Override
    public boolean passwordRecoveryIsRetryAndMaximumNumberOfRetries(String email) {
        UserAccountPasswordRecovery earlierOptIn = userAccountPasswordRecoveryRepository.findByEmail(email);
        return earlierOptIn == null?false:earlierOptIn.getNumberOfRetries() >= simpleworklistProperties.getRegistration().getMaxRetries();
    }

    @Override
    public void passwordRecoveryCheckIfResponseIsInTime(String email) {
        UserAccountPasswordRecovery earlierOptIn = userAccountPasswordRecoveryRepository.findByEmail(email);
        if (earlierOptIn != null) {
            Date now = new Date();
            if ((simpleworklistProperties.getRegistration().getTtlEmailVerificationRequest() + earlierOptIn.getRowCreatedAt().getTime()) < now.getTime()) {
                userAccountPasswordRecoveryRepository.delete(earlierOptIn);
            }
        }
    }

    @Async
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoverySendEmailTo(String email) {
        UserAccountPasswordRecovery earlierOptIn = userAccountPasswordRecoveryRepository.findByEmail(email);
        UserAccountPasswordRecovery o = new UserAccountPasswordRecovery();
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        } else {
            o.setUuid(UUID.randomUUID());
        }
        o.setDoubleOptInStatus(UserAccountPasswordRecoveryStatus.PASSWORD_RECOVERY_SAVED_EMAIL);
        o.setEmail(email);
        String token = tokenGeneratorService.getToken();
        o.setToken(token);
        log.info("To be saved: " + o.toString());
        o = userAccountPasswordRecoveryRepository.saveAndFlush(o);
        log.info("Saved: " + o.toString());
        this.sendEmailForPasswordReset(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoverySentEmail(UserAccountPasswordRecovery o) {
        o.setDoubleOptInStatus(UserAccountPasswordRecoveryStatus.PASSWORD_RECOVERY_SENT_EMAIL);
        log.info("about to save: " + o.toString());
        userAccountPasswordRecoveryRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoveryClickedInEmail(UserAccountPasswordRecovery o) {
        o.setDoubleOptInStatus(UserAccountPasswordRecoveryStatus.PASSWORD_RECOVERY_CLICKED_IN_MAIL);
        userAccountPasswordRecoveryRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoveryDone(UserAccountPasswordRecovery o) {
        o.setDoubleOptInStatus(UserAccountPasswordRecoveryStatus.PASSWORD_RECOVERY_STORED_CHANGED);
        o = userAccountPasswordRecoveryRepository.saveAndFlush(o);
        userAccountPasswordRecoveryRepository.delete(o);
    }

    private void sendEmailForPasswordReset(UserAccountPasswordRecovery o) {
        String urlHost = simpleworklistProperties.getRegistration().getUrlHost();
        String mailFrom= simpleworklistProperties.getRegistration().getMailFrom();
        boolean success = true;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(o.getEmail());
        msg.setText(
                "Dear User, "
                        + "for Password Reset at SimpleWorklist, "
                        + "Please go to URL: \nhttp://" + urlHost + "/user/resetPassword/confirm/" + o.getToken()
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
