package org.woehlke.simpleworklist.control.impl;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.services.UserAccountLoginSuccessService;

@Controller
@RequestMapping(value = "/test")
public class TestController {

    private final TestService testService;

    private final UserAccountLoginSuccessService userAccountLoginSuccessService;

    @Autowired
    public TestController(TestService testService, UserAccountLoginSuccessService userAccountLoginSuccessService) {
        this.testService = testService;
        this.userAccountLoginSuccessService = userAccountLoginSuccessService;
    }

    @RequestMapping(value = "/helper/project/createTree", method = RequestMethod.GET)
    public String createTestCategoryTree() {
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        Assert.notNull(user);
        testService.createTestCategoryTreeForUserAccount(user);
        return "redirect:/";
    }

}
