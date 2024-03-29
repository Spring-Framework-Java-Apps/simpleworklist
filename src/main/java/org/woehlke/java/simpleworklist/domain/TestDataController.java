package org.woehlke.java.simpleworklist.domain;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.meso.testdata.TestDataService;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.security.login.LoginSuccessService;

@Log
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
