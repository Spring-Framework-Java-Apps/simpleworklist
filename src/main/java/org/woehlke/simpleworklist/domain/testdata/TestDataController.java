package org.woehlke.simpleworklist.domain.testdata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.application.security.login.UserAccountLoginSuccessService;

@Slf4j
@Controller
@RequestMapping(path = "/testdata")
public class TestDataController {

    private final TestDataService testDataService;
    private final UserAccountLoginSuccessService userAccountLoginSuccessService;

    @Autowired
    public TestDataController(
        TestDataService testDataService,
        UserAccountLoginSuccessService userAccountLoginSuccessService
    ) {
        this.testDataService = testDataService;
        this.userAccountLoginSuccessService = userAccountLoginSuccessService;
    }

    @RequestMapping(path = "/createTree", method = RequestMethod.GET)
    public String createTestCategoryTree() {
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        testDataService.createTestData(user);
        return "redirect:/home";
    }

}
