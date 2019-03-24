package org.woehlke.simpleworklist.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.TestService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;

@Controller
public class TestController {

    @Inject
    private TestService testService;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/test/helper/project/createTree", method = RequestMethod.GET)
    public final String createTestCategoryTree() {
        UserAccount user = userService.retrieveCurrentUser();
        Assert.notNull(user);
        testService.createTestCategoryTreeForUserAccount(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/test/tw/thymeleaf", method = RequestMethod.GET)
    public final String zhymeleafTest(Model model) {
        model.addAttribute("title","Hello, Thomas Woehlke");
        model.addAttribute("description","bla blupp blopp honk tonk");
        return "test/tw";
    }
}
