package org.woehlke.simpleworklist.control;

import java.util.List;

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
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.RegisterFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.UserService;

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Inject
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginFormular(Model model){
		LoginFormBean loginFormBean = new LoginFormBean();
		model.addAttribute("loginFormBean",loginFormBean);
		return "user/loginForm";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPerform(@Valid LoginFormBean loginFormBean,
			BindingResult result, Model model){
		boolean authorized = userService.authorize(loginFormBean);
		if(!result.hasErrors() && authorized){
			return "redirect:/";
		} else {
			String objectName="loginFormBean";
			String field="userEmail";
			String defaultMessage="Email or Password wrong.";
			FieldError e = new FieldError(objectName, field, defaultMessage);
			result.addError(e);
			return "user/loginForm";
		}
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String users(Model model){
		List<UserAccount> users = userService.findAll();
		model.addAttribute("users",users);
		return "user/users";
	}

}
