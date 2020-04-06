package org.woehlke.simpleworklist.config;

import lombok.Getter;
import org.woehlke.simpleworklist.context.NewContextForm;
import org.woehlke.simpleworklist.language.Language;
import org.woehlke.simpleworklist.user.account.UserAccount;

import java.util.Date;

@Getter
public class TestDataUser {

    private final String[] emails = { "test01@test.de", "test02@test.de", "test03@test.de" };
    private final String[] passwords = { "test01pwd", "test02pwd", "test03pwd"};
    private final String[] fullnames = { "test01 Name", "test02 Name", "test03 Name"};

    private final String username_email = "undefined@test.de";
    private final String password = "ASDFG";
    private final String full_name = "UNDEFINED_NAME";

    private final UserAccount[] testUser;
    private final NewContextForm[] newContext;

    public TestDataUser(){
        Date lastLoginTimestamp = new Date();
        testUser = new UserAccount[emails.length];
        newContext = new NewContextForm[emails.length];
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
            testUser[i].setDefaultLanguage(Language.EN);
            testUser[i].setLastLoginTimestamp(lastLoginTimestamp);
            newContext[i] = new NewContextForm("testDe_"+i,"testEn_"+i);
        }
    }
}
