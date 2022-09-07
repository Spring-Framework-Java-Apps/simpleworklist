package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.testdata.TestDataService;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.user.login.LoginSuccessService;

@Slf4j
@Controller
@RequestMapping(path = "/testdata")
public class TestDataController {

    private final TestDataService testDataService;
    private final LoginSuccessService loginSuccessService;

    @Autowired
    public TestDataController(
        TestDataService testDataService,
        LoginSuccessService loginSuccessService
    ) {
        this.testDataService = testDataService;
        this.loginSuccessService = loginSuccessService;
    }

    @RequestMapping(path = "/createTree", method = RequestMethod.GET)
    public String createTestCategoryTree() {
        UserAccount user = loginSuccessService.retrieveCurrentUser();
        testDataService.createTestData(user);
        return "redirect:/home";
    }

}
