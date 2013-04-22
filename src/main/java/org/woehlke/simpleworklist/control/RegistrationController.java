package org.woehlke.simpleworklist.control;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.model.RegisterFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.UserService;

@Controller
public class RegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	
	@Inject
	private RegistrationProcessService registrationProcessService;
	
	@Inject
	private UserService userService;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String signInFormular(Model model){
		RegisterFormBean registerFormBean = new RegisterFormBean();
		model.addAttribute("registerFormBean",registerFormBean);
		return "user/registerForm";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String signInRegister(@Valid RegisterFormBean registerFormBean,
			BindingResult result, Model model){
		if(result.hasErrors()){
			return "user/registerForm";
		} else {
			registrationProcessService.checkIfResponseIsInTime(registerFormBean.getEmail());
			if(userService.isEmailAvailable(registerFormBean.getEmail())){
				if(registrationProcessService.isRetryAndMaximumNumberOfRetries(registerFormBean.getEmail())){
					String objectName="registerFormBean";
					String field="email";
					String defaultMessage="Maximum Number of Retries reached.";
					FieldError e = new FieldError(objectName, field, defaultMessage);
					result.addError(e);
					return "user/registerForm";
				} else {
					registrationProcessService.startSecondOptIn(registerFormBean.getEmail());
					return "user/registerSentMail";
				}
			} else {
				String objectName="registerFormBean";
				String field="email";
				String defaultMessage="Email is already in use.";
				FieldError e = new FieldError(objectName, field, defaultMessage);
				result.addError(e);
				return "user/registerForm";
			}
		}
	}
	
	@RequestMapping(value = "/confirm/{confirmId}", method = RequestMethod.GET)
	public String signInFormular(@PathVariable String confirmId,Model model){
		logger.info("GET /confirm/"+confirmId);
		RegistrationProcess o = registrationProcessService.findByToken(confirmId);
		if(o!=null){
			UserAccountFormBean ua = new UserAccountFormBean();
			ua.setUserEmail(o.getEmail());
			model.addAttribute("userAccount",ua);
			return "user/registerConfirmed";
		} else {
			return "user/registerNotConfirmed";
		}
	}
	
	@RequestMapping(value = "/confirm/{confirmId}", method = RequestMethod.POST)
	public String signInFormularPost(@Valid UserAccountFormBean userAccount, BindingResult result,
			@PathVariable String confirmId, Model model){
		logger.info("POST /confirm/"+confirmId+" : "+userAccount.toString());
		RegistrationProcess o = registrationProcessService.findByToken(confirmId);
		if(o!=null){
			if(!result.hasErrors()){
				userService.createUser(userAccount,o);
			} 
			model.addAttribute("userAccount",userAccount);
			return "redirect:/login";
		} else {
			return "user/registerNotConfirmed";
		}
	}
}
