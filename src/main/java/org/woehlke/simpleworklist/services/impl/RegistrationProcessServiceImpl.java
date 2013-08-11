package org.woehlke.simpleworklist.services.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
public class RegistrationProcessServiceImpl implements
		RegistrationProcessService {

	@Autowired
	@Value("${worklist.registration.max.retries}")
	private int maxRetries;
	
	@Autowired
	@Value("${worklist.registration.ttl.email.verifcation.request}")
	private long ttlEmailVerificationRequest;

	private static final Logger logger = LoggerFactory.getLogger(RegistrationProcessServiceImpl.class);
	
	@Inject
	private RegistrationProcessRepository registrationProcessRepository;
	
	@Inject
	private PollableChannel emailChannel;

    @Inject
    private PollableChannel passwordResetEmailSenderChannel;
	
	private SecureRandom random = new SecureRandom();
	
	@Override
	public boolean isRetryAndMaximumNumberOfRetries(String email) {
		RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
		if(earlierOptIn == null){
			return false;
		} else {
			return earlierOptIn.getNumberOfRetries()>=maxRetries;
		}
	}


    @Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void checkIfResponseIsInTime(String email){
		RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
		if(earlierOptIn!=null){
			Date now = new Date();
			if((ttlEmailVerificationRequest+earlierOptIn.getCreatedTimestamp().getTime()) < now.getTime()){
				registrationProcessRepository.delete(earlierOptIn);
			}
		}
	}

    private String getToken(){
        return (new BigInteger(130, random).toString(32))+UUID.randomUUID().toString();
    }

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void startSecondOptIn(String email) {
		RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
        RegistrationProcess o = new RegistrationProcess();
		if(earlierOptIn!=null){
			o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_SAVED_EMAIL);
        o.setEmail(email);
        String token = getToken();
        o.setToken(token);
        logger.info("To be saved: "+o.toString());
        o=registrationProcessRepository.saveAndFlush(o);
        logger.info("Saved: "+o.toString());
        Message<RegistrationProcess> message = MessageBuilder.withPayload(o).build();
        emailChannel.send(message);
	}
	
	@Override
	public RegistrationProcess findByToken(String confirmId) {
		return registrationProcessRepository.findByToken(confirmId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void deleteAll() {
		registrationProcessRepository.deleteAll();
	}

    @Override
    public int getNumberOfAll() {
        return registrationProcessRepository.findAll().size();
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
    public void sendPasswordResetTo(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
        RegistrationProcess o = new RegistrationProcess();
        if(earlierOptIn!=null){
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_SAVED_EMAIL);
        o.setEmail(email);
        String token = getToken();
        o.setToken(token);
        logger.info("To be saved: "+o.toString());
        o=registrationProcessRepository.saveAndFlush(o);
        logger.info("Saved: "+o.toString());
        Message<RegistrationProcess> message = MessageBuilder.withPayload(o).build();
        passwordResetEmailSenderChannel.send(message);
    }

    @Override
    public void usersPasswordChangeClickedInEmail(RegistrationProcess registrationProcess) {
        registrationProcess.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_CLICKED_IN_MAIL);
        registrationProcess=registrationProcessRepository.saveAndFlush(registrationProcess);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
    public void registratorClickedInEmail(RegistrationProcess registrationProcess) {
        registrationProcess.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_CLICKED_IN_MAIL);
        registrationProcess=registrationProcessRepository.saveAndFlush(registrationProcess);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
    public void userCreated(RegistrationProcess registrationProcess) {
        registrationProcess.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_ACCOUNT_CREATED);
        registrationProcess=registrationProcessRepository.saveAndFlush(registrationProcess);
        registrationProcessRepository.delete(registrationProcess);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
    public void usersPasswordChanged(RegistrationProcess registrationProcess) {
        registrationProcess.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_STORED_CHANGED);
        registrationProcess=registrationProcessRepository.saveAndFlush(registrationProcess);
        registrationProcessRepository.delete(registrationProcess);
    }


}
