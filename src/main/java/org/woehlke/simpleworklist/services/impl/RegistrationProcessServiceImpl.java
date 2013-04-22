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
	
	public void checkIfResponseIsInTime(String email){
		RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
		if(earlierOptIn!=null){
			Date now = new Date();
			if((ttlEmailVerificationRequest+earlierOptIn.getCreatedTimestamp().getTime()) < now.getTime()){
				registrationProcessRepository.delete(earlierOptIn);
			}
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void startSecondOptIn(String email) {
		RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmail(email);
		if(earlierOptIn==null){
			String token = (new BigInteger(130, random).toString(32))+UUID.randomUUID().toString();
			RegistrationProcess o = new RegistrationProcess();
			o.setDoubleOptInStatus(RegistrationProcessStatus.SAVED_EMAIL);
			o.setEmail(email);
			o.setToken(token);
			logger.info("To be saved: "+o.toString());
			o=registrationProcessRepository.saveAndFlush(o);
			logger.info("Saved: "+o.toString());
			Message<RegistrationProcess> message = MessageBuilder.withPayload(o).build();
			emailChannel.send(message);
		} else {
			String token = (new BigInteger(130, random).toString(32))+UUID.randomUUID().toString();
			RegistrationProcess o = earlierOptIn;
			o.setDoubleOptInStatus(RegistrationProcessStatus.SAVED_EMAIL);
			o.setEmail(email);
			o.setToken(token);
			o.setNumberOfRetries(o.getNumberOfRetries()+1);
			logger.info("To be saved: "+o.toString());
			o=registrationProcessRepository.saveAndFlush(o);
			logger.info("Saved: "+o.toString());
			Message<RegistrationProcess> message = MessageBuilder.withPayload(o).build();
			emailChannel.send(message);
		}		
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
}
