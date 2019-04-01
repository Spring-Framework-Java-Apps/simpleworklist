package org.woehlke.simpleworklist.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.eai.EmailPipeline;
import org.woehlke.simpleworklist.entities.UserRegistration;
import org.woehlke.simpleworklist.entities.enumerations.UserRegistrationStatus;
import org.woehlke.simpleworklist.repository.UserRegistrationRepository;
import org.woehlke.simpleworklist.services.TokenGeneratorService;
import org.woehlke.simpleworklist.services.UserRegistrationService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserRegistrationServiceImpl implements
        UserRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    private final UserRegistrationRepository userRegistrationRepository;

    private final EmailPipeline emailPipeline;

    private final TokenGeneratorService tokenGeneratorService;

    @Autowired
    public UserRegistrationServiceImpl(ApplicationProperties applicationProperties, UserRegistrationRepository userRegistrationRepository, EmailPipeline emailPipeline, TokenGeneratorService tokenGeneratorService) {
        this.applicationProperties = applicationProperties;
        this.userRegistrationRepository = userRegistrationRepository;
        this.emailPipeline = emailPipeline;
        this.tokenGeneratorService = tokenGeneratorService;
    }

    @Override
    public boolean registrationIsRetryAndMaximumNumberOfRetries(String email) {
        UserRegistration earlierOptIn = userRegistrationRepository.findByEmail(email);
        return earlierOptIn == null?false:(earlierOptIn.getNumberOfRetries() >= applicationProperties.getRegistration().getMaxRetries());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationCheckIfResponseIsInTime(String email) {
        UserRegistration earlierOptIn = userRegistrationRepository.findByEmail(email);
        if (earlierOptIn != null) {
            Date now = new Date();
            if ((applicationProperties.getRegistration().getTtlEmailVerificationRequest() + earlierOptIn.getCreatedTimestamp().getTime()) < now.getTime()) {
                userRegistrationRepository.delete(earlierOptIn);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationSendEmailTo(String email) {
        UserRegistration earlierOptIn = userRegistrationRepository.findByEmail(email);
        UserRegistration o = new UserRegistration();
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setDoubleOptInStatus(UserRegistrationStatus.REGISTRATION_SAVED_EMAIL);
        o.setEmail(email);
        String token = tokenGeneratorService.getToken();
        o.setToken(token);
        LOGGER.info("To be saved: " + o.toString());
        o = userRegistrationRepository.saveAndFlush(o);
        LOGGER.info("Saved: " + o.toString());
        emailPipeline.sendEmailToRegisterNewUser(o);
    }

    @Override
    public UserRegistration findByToken(String confirmId) {
        return userRegistrationRepository.findByToken(confirmId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationSentEmail(UserRegistration o) {
        o.setDoubleOptInStatus(UserRegistrationStatus.REGISTRATION_SENT_MAIL);
        LOGGER.info("about to save: " + o.toString());
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

}
