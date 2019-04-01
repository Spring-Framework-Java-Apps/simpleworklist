package org.woehlke.simpleworklist.control.impl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.TestService;
import org.woehlke.simpleworklist.services.UserAccountAccessService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class TestController {

    private final TestService testService;

    private final UserAccountAccessService userAccountAccessService;

    @Autowired
    public TestController(TestService testService, UserAccountAccessService userAccountAccessService) {
        this.testService = testService;
        this.userAccountAccessService = userAccountAccessService;
    }

    @RequestMapping(value = "/test/helper/project/createTree", method = RequestMethod.GET)
    public String createTestCategoryTree() {
        UserAccount user = userAccountAccessService.retrieveCurrentUser();
        Assert.notNull(user);
        testService.createTestCategoryTreeForUserAccount(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/t/tw/thymeleaf", method = RequestMethod.GET)
    public String zhymeleafTest(Model model) {
        model.addAttribute("title","Hello, Thomas Woehlke");
        model.addAttribute("description","bla blupp blopp honk tonk");
        return "tw";
    }
}
