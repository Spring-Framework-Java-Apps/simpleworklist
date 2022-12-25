package org.woehlke.java.simpleworklist.domain.db.user.signup;

import java.util.Date;
import java.util.UUID;

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
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountRegistration;
import org.woehlke.java.simpleworklist.domain.db.user.token.TokenGeneratorService;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountRegistrationServiceImpl implements UserAccountRegistrationService {

    private final SimpleworklistProperties simpleworklistProperties;
    private final UserAccountRegistrationRepository userAccountRegistrationRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final JavaMailSender mailSender;

    @Autowired
    public UserAccountRegistrationServiceImpl(
        SimpleworklistProperties simpleworklistProperties,
        UserAccountRegistrationRepository userAccountRegistrationRepository,
        TokenGeneratorService tokenGeneratorService,
        JavaMailSender mailSender
    ) {
        this.simpleworklistProperties = simpleworklistProperties;
        this.userAccountRegistrationRepository = userAccountRegistrationRepository;
        this.tokenGeneratorService = tokenGeneratorService;
        this.mailSender = mailSender;
    }

    @Override
    public boolean registrationIsRetryAndMaximumNumberOfRetries(String email) {
        UserAccountRegistration earlierOptIn = userAccountRegistrationRepository.findByEmail(email);
        return earlierOptIn == null?false:(earlierOptIn.getNumberOfRetries()
            >= simpleworklistProperties.getRegistration().getMaxRetries());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationCheckIfResponseIsInTime(String email) {
        UserAccountRegistration earlierOptIn = userAccountRegistrationRepository.findByEmail(email);
        if (earlierOptIn != null) {
            Date now = new Date();
            if ((simpleworklistProperties.getRegistration().getTtlEmailVerificationRequest()
                + earlierOptIn.getRowCreatedAt().getTime()) < now.getTime()) {
                userAccountRegistrationRepository.delete(earlierOptIn);
            }
        }
    }

    @Async
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationSendEmailTo(String email) {
        UserAccountRegistration earlierOptIn = userAccountRegistrationRepository.findByEmail(email);
        UserAccountRegistration o = new UserAccountRegistration();
        o.setUuid(UUID.randomUUID());
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setDoubleOptInStatus(UserAccountRegistrationStatus.REGISTRATION_SAVED_EMAIL);
        o.setEmail(email);
        String token = tokenGeneratorService.getToken();
        o.setToken(token);
        log.info("To be saved: " + o.toString());
        o = userAccountRegistrationRepository.saveAndFlush(o);
        log.info("Saved: " + o.toString());
        this.sendEmailToRegisterNewUser(o);
    }

    @Override
    public UserAccountRegistration findByToken(String confirmId) {
        return userAccountRegistrationRepository.findByToken(confirmId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationSentEmail(UserAccountRegistration o) {
        o.setDoubleOptInStatus(UserAccountRegistrationStatus.REGISTRATION_SENT_MAIL);
        log.info("about to save: " + o.toString());
        userAccountRegistrationRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationClickedInEmail(UserAccountRegistration o) {
        o.setDoubleOptInStatus(UserAccountRegistrationStatus.REGISTRATION_CLICKED_IN_MAIL);
        userAccountRegistrationRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationUserCreated(UserAccountRegistration o) {
        o.setDoubleOptInStatus(UserAccountRegistrationStatus.REGISTRATION_ACCOUNT_CREATED);
        o = userAccountRegistrationRepository.saveAndFlush(o);
        userAccountRegistrationRepository.delete(o);
    }

    private void sendEmailToRegisterNewUser(UserAccountRegistration o) {
        String urlHost = simpleworklistProperties.getRegistration().getUrlHost();
        String mailFrom= simpleworklistProperties.getRegistration().getMailFrom();
        boolean success = true;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(o.getEmail());
        msg.setText(
                "Dear new User,\n\n"
                        + "thank you for registring at Simple Worklist. \n"
                        + "Please validate your email and go to URL: \n"
                        + "\n\nSincerely Yours, Simpleworklist Team"
        );
        msg.setSubject("Your Registration at Simple Worklist");
        msg.setFrom(mailFrom);
        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            log.warn(ex.getMessage() + " for " + o.toString());
            success = false;
        }
        if (success) {
            this.registrationSentEmail(o);
        }
        log.info("Sent MAIL: " + o.toString());
    }


}
