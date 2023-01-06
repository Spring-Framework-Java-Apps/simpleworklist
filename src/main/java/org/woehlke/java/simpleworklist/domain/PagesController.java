package org.woehlke.java.simpleworklist.domain;

import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import jakarta.validation.constraints.NotNull;
import java.util.Locale;

@Log
@Controller
@PreAuthorize("permitAll()")
@RequestMapping(path = "/pages")
public class PagesController {

  @PreAuthorize("permitAll()")
  @RequestMapping(path = "/information", method = RequestMethod.GET)
  public final String renderPageInformation(
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    log.info("renderPageInformation");
    return "pages/information";
  }
}
