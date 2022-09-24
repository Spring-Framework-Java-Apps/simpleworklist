package org.woehlke.java.simpleworklist.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.context.ContextService;
import org.woehlke.java.simpleworklist.domain.db.data.context.NewContextForm;
import org.woehlke.java.simpleworklist.domain.meso.language.Language;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountService;

import java.util.Date;
import java.util.UUID;


@Slf4j
@Getter
@Service
public class UserAccountTestDataServiceImpl implements UserAccountTestDataService {

    private final String[] emails = { "test01@test.de", "test02@test.de", "test03@test.de" };
    private final String[] passwords = { "test01pwd", "test02pwd", "test03pwd"};
    private final String[] fullnames = { "test01 Name", "test02 Name", "test03 Name"};

    private final String username_email = "undefined@test.de";
    private final String password = "ASDFG";
    private final String full_name = "UNDEFINED_NAME";

    private final UserAccount[] testUser;
    private final NewContextForm[] newContext;

    private final UserAccountService userAccountService;
    private final ContextService contextService;
    private final SimpleworklistProperties simpleworklistProperties;

    @Autowired
    public UserAccountTestDataServiceImpl(
        UserAccountService userAccountService,
        ContextService contextService,
        SimpleworklistProperties simpleworklistProperties
    ) {
        this.userAccountService = userAccountService;
        this.contextService = contextService;
        this.simpleworklistProperties = simpleworklistProperties;
        Date lastLoginTimestamp = new Date();
        testUser = new UserAccount[emails.length];
        newContext = new NewContextForm[emails.length];
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUuid(UUID.randomUUID());
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
            testUser[i].setDefaultLanguage(Language.EN);
            testUser[i].setLastLoginTimestamp(lastLoginTimestamp);
            newContext[i] = new NewContextForm("testDe_"+i,"testEn_"+i);
        }
    }

    public void setUp() {
        for (int i = 0; i < testUser.length; i++) {
            UserAccount a = userAccountService.findByUserEmail(testUser[i].getUserEmail());
            if (a == null) {
                UserAccount persisted = userAccountService.saveAndFlush(testUser[i]);
                testUser[i] = persisted;
                NewContextForm newContextPrivate = new NewContextForm("privat"+i,"private"+i);
                NewContextForm newContextWork = new NewContextForm("arbeit"+i,"work"+i);
                Context persistedContextPrivate = contextService.createNewContext(newContextPrivate, testUser[i]);
                Context persistedContextWork = contextService.createNewContext(newContextWork, testUser[i]);
                testUser[i].setDefaultContext(persistedContextPrivate);
                persisted = userAccountService.saveAndFlush(testUser[i]);
            }
       }
    }

    @Override
    public UserAccount getFirstUserAccount() {
        return testUser[0];
    }
}
