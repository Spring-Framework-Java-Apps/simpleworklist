package org.woehlke.simpleworklist.services.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.Message;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.RegistrationProcessStatus;
import org.woehlke.simpleworklist.repository.RegistrationProcessRepository;
import org.woehlke.simpleworklist.services.RegistrationProcessService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class RegistrationProcessServiceImpl implements
        RegistrationProcessService {

    @Value("${worklist.registration.max.retries}")
    private int maxRetries;

    @Value("${worklist.registration.ttl.email.verifcation.request}")
    private long ttlEmailVerificationRequest;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationProcessServiceImpl.class);

    @Inject
    private RegistrationProcessRepository registrationProcessRepository;

    @Inject
    private PollableChannel registrationProcessEmailSenderChannel;

    @Inject
    private PollableChannel passwordResetEmailSenderChannel;

    private SecureRandom random = new SecureRandom();

    @Override
    public boolean isRetryAndMaximumNumberOfRetries(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
        return earlierOptIn == null?false:earlierOptIn.getNumberOfRetries() >= maxRetries;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void checkIfResponseIsInTime(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
        if (earlierOptIn != null) {
            Date now = new Date();
            if ((ttlEmailVerificationRequest + earlierOptIn.getCreatedTimestamp().getTime()) < now.getTime()) {
                registrationProcessRepository.delete(earlierOptIn);
            }
        }
    }

    private String getToken() {
        int base = 130;
        int strLength = 30;
        return new BigInteger(base, random).toString(strLength) + UUID.randomUUID().toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registerNewUserSendEmailTo(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
        RegistrationProcess o = new RegistrationProcess();
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_SAVED_EMAIL);
        o.setEmail(email);
        String token = getToken();
        o.setToken(token);
        LOGGER.info("To be saved: " + o.toString());
        o = registrationProcessRepository.saveAndFlush(o);
        LOGGER.info("Saved: " + o.toString());
        Message<RegistrationProcess> message = MessageBuilder.withPayload(o).build();
        registrationProcessEmailSenderChannel.send(message);
    }

    @Override
    public RegistrationProcess findByToken(String confirmId) {
        return registrationProcessRepository.findByToken(confirmId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void usersPasswordChangeSendEmailTo(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
        RegistrationProcess o = new RegistrationProcess();
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_SAVED_EMAIL);
        o.setEmail(email);
        String token = getToken();
        o.setToken(token);
        LOGGER.info("To be saved: " + o.toString());
        o = registrationProcessRepository.saveAndFlush(o);
        LOGGER.info("Saved: " + o.toString());
        Message<RegistrationProcess> message = MessageBuilder.withPayload(o).build();
        passwordResetEmailSenderChannel.send(message);
        o.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_SAVED_EMAIL);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registerNewUserSentEmail(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_SENT_MAIL);
        LOGGER.info("about to save: " + o.toString());
        try {
            registrationProcessRepository.saveAndFlush(o);
        } catch (Exception e) {
            LOGGER.warn(e.toString());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void usersPasswordChangeSentEmail(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_SENT_EMAIL);
        LOGGER.info("about to save: " + o.toString());
        try {
            registrationProcessRepository.saveAndFlush(o);
        } catch (Exception e) {
            LOGGER.warn(e.toString());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void usersPasswordChangeClickedInEmail(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_CLICKED_IN_MAIL);
        registrationProcessRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registerNewUserClickedInEmail(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_CLICKED_IN_MAIL);
        registrationProcessRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registerNewUserCreated(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_ACCOUNT_CREATED);
        o = registrationProcessRepository.saveAndFlush(o);
        registrationProcessRepository.delete(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void usersPasswordChanged(RegistrationProcess registrationProcess) {
        registrationProcess.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_STORED_CHANGED);
        registrationProcess = registrationProcessRepository.saveAndFlush(registrationProcess);
        registrationProcessRepository.delete(registrationProcess);
    }


}
