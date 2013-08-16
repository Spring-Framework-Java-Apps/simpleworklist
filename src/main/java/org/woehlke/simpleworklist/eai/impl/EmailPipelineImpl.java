package org.woehlke.simpleworklist.eai.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.eai.EmailPipeline;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.RegistrationProcessStatus;
import org.woehlke.simpleworklist.repository.RegistrationProcessRepository;
import org.woehlke.simpleworklist.services.RegistrationProcessService;


@MessageEndpoint(value="emailPipeline")
public class EmailPipelineImpl implements EmailPipeline {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailPipelineImpl.class);

	@Inject
	private JavaMailSender mailSender;
	
	@Inject
	private RegistrationProcessService registrationProcessService;

	@Value("${worklist.registration.url.host}")
	private String urlHost;

	@Value("${worklist.registration.mail.from}")
	private String mailFrom;
	
	@Override
	public void sendEmailToRegisterNewUser(RegistrationProcess o){
		boolean success = true;
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(o.getEmail());
        msg.setText(
            "Dear new User, " +
            "thank you for registring at Simple Worklist. " +
            "Please validate your email and go to URL: http://"+urlHost+"/confirm/" + o.getToken()+" "+
            "Sincerely Yours, The Team");
        msg.setSubject("Your Registration at Simple Worklist");
        msg.setFrom(mailFrom);
        try{
            this.mailSender.send(msg);
        }
        catch(MailException ex) {
        	logger.warn(ex.getMessage()+" for "+o.toString());  
        	success = false;
        }
        if(success){
        	registrationProcessService.sentEmailToRegisterNewUser(o);
        }
		logger.info("Sent MAIL: "+o.toString());
	}

    @Override
    public void sendEmailForPasswordReset(RegistrationProcess o) {
        boolean success = true;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(o.getEmail());
        msg.setText(
                "Dear User, " +
                        "for Password Reset at SimpleWorklist, " +
                        "Please go to URL: http://"+urlHost+"/passwordResetConfirm/" + o.getToken()+" "+
                        "Sincerely Yours, The Team");
        msg.setSubject("Password Reset at Simple Worklist");
        msg.setFrom(mailFrom);
        try{
            this.mailSender.send(msg);
        }
        catch(MailException ex) {
            logger.warn(ex.getMessage()+" for "+o.toString());
            success = false;
        }
        if(success){
            registrationProcessService.sentEmailForPasswordReset(o);
        }
        logger.info("Sent MAIL: "+o.toString());
    }
}
