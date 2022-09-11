package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping(path = "/")
public class PagesController {

  @RequestMapping(path = "/information", method = RequestMethod.GET)
  public final String renderPageInformation(
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    log.info("addNewTaskToInboxGet");
    return "pages/information";
  }
}
