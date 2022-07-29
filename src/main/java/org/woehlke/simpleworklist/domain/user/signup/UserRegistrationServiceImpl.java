package org.woehlke.simpleworklist.domain.user.signup;

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
import org.woehlke.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.simpleworklist.domain.user.token.TokenGeneratorService;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final SimpleworklistProperties simpleworklistProperties;
    private final UserRegistrationRepository userRegistrationRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final JavaMailSender mailSender;

    @Autowired
    public UserRegistrationServiceImpl(SimpleworklistProperties simpleworklistProperties, UserRegistrationRepository userRegistrationRepository, TokenGeneratorService tokenGeneratorService, JavaMailSender mailSender) {
        this.simpleworklistProperties = simpleworklistProperties;
        this.userRegistrationRepository = userRegistrationRepository;
        this.tokenGeneratorService = tokenGeneratorService;
        this.mailSender = mailSender;
    }

    @Override
    public boolean registrationIsRetryAndMaximumNumberOfRetries(String email) {
        UserRegistration earlierOptIn = userRegistrationRepository.findByEmail(email);
        return earlierOptIn == null?false:(earlierOptIn.getNumberOfRetries() >= simpleworklistProperties.getRegistration().getMaxRetries());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationCheckIfResponseIsInTime(String email) {
        UserRegistration earlierOptIn = userRegistrationRepository.findByEmail(email);
        if (earlierOptIn != null) {
            Date now = new Date();
            if ((simpleworklistProperties.getRegistration().getTtlEmailVerificationRequest() + earlierOptIn.getRowCreatedAt().getTime()) < now.getTime()) {
                userRegistrationRepository.delete(earlierOptIn);
            }
        }
    }

    @Async
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationSendEmailTo(String email) {
        UserRegistration earlierOptIn = userRegistrationRepository.findByEmail(email);
        UserRegistration o = new UserRegistration();
        o.setUuid(UUID.randomUUID());
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setDoubleOptInStatus(UserRegistrationStatus.REGISTRATION_SAVED_EMAIL);
        o.setEmail(email);
        String token = tokenGeneratorService.getToken();
        o.setToken(token);
        log.info("To be saved: " + o.toString());
        o = userRegistrationRepository.saveAndFlush(o);
        log.info("Saved: " + o.toString());
        this.sendEmailToRegisterNewUser(o);
    }

    @Override
    public UserRegistration findByToken(String confirmId) {
        return userRegistrationRepository.findByToken(confirmId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationSentEmail(UserRegistration o) {
        o.setDoubleOptInStatus(UserRegistrationStatus.REGISTRATION_SENT_MAIL);
        log.info("about to save: " + o.toString());
        userRegistrationRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationClickedInEmail(UserRegistration o) {
        o.setDoubleOptInStatus(UserRegistrationStatus.REGISTRATION_CLICKED_IN_MAIL);
        userRegistrationRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationUserCreated(UserRegistration o) {
        o.setDoubleOptInStatus(UserRegistrationStatus.REGISTRATION_ACCOUNT_CREATED);
        o = userRegistrationRepository.saveAndFlush(o);
        userRegistrationRepository.delete(o);
    }

    private void sendEmailToRegisterNewUser(UserRegistration o) {
        String urlHost = simpleworklistProperties.getRegistration().getUrlHost();
        String mailFrom= simpleworklistProperties.getRegistration().getMailFrom();
        boolean success = true;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(o.getEmail());
        msg.setText(
                "Dear new User, "
                        + "thank you for registring at Simple Worklist. \n"
                        + "Please validate your email and go to URL: \nhttp://" + urlHost + "/user/register/confirm/" + o.getToken()
                        + "\n\nSincerely Yours, The Team");
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
