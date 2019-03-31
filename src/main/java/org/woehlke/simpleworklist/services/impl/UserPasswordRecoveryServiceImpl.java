package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.entities.UserPasswordRecovery;
import org.woehlke.simpleworklist.entities.enumerations.UserPasswordRecoveryStatus;
import org.woehlke.simpleworklist.repository.UserPasswordRecoveryRepository;
import org.woehlke.simpleworklist.services.TokenGeneratorService;
import org.woehlke.simpleworklist.services.UserPasswordRecoveryService;

import java.util.Date;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserPasswordRecoveryServiceImpl implements UserPasswordRecoveryService {

    @Autowired
    private UserPasswordRecoveryRepository userPasswordRecoveryRepository;

    @Autowired
    protected ApplicationProperties applicationProperties;

    //@Value("${org.woehlke.simpleworklist.registration.maxRetries}")
    //private int maxRetries;

    //@Value("${org.woehlke.simpleworklist.registration.ttl.email.verifcation.request}")
    //private long ttlEmailVerificationRequest;

    @Autowired
    private PollableChannel passwordResetEmailSenderChannel;

    @Autowired
    private TokenGeneratorService tokenGeneratorService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPasswordRecoveryServiceImpl.class);

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
            if ((applicationProperties.getRegistration().getTtlEmailVerificationRequest() + earlierOptIn.getCreatedTimestamp().getTime()) < now.getTime()) {
                userPasswordRecoveryRepository.delete(earlierOptIn);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoverySendEmailTo(String email) {
        UserPasswordRecovery earlierOptIn = userPasswordRecoveryRepository.findByEmail(email);
        UserPasswordRecovery o = new UserPasswordRecovery();
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setDoubleOptInStatus(UserPasswordRecoveryStatus.PASSWORD_RECOVERY_SAVED_EMAIL);
        o.setEmail(email);
        String token = tokenGeneratorService.getToken();
        o.setToken(token);
        LOGGER.info("To be saved: " + o.toString());
        o = userPasswordRecoveryRepository.saveAndFlush(o);
        LOGGER.info("Saved: " + o.toString());
        Message<UserPasswordRecovery> message = MessageBuilder.withPayload(o).build();
        passwordResetEmailSenderChannel.send(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoverySentEmail(UserPasswordRecovery o) {
        o.setDoubleOptInStatus(UserPasswordRecoveryStatus.PASSWORD_RECOVERY_SENT_EMAIL);
        LOGGER.info("about to save: " + o.toString());
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
}
