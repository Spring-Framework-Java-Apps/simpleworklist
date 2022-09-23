package org.woehlke.java.simpleworklist.domain.db.user;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountForm;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserAccountPasswordEncodedIntegrationTest {

    @Autowired
    private PasswordEncoder encoder;

    @Test
    public void testEncoderIsWired(){
        assertNotNull(encoder);
    }

    /**
     * This Test is obsolete now due to changed encoder from MD5 to BCrypt (20.02.2016).
     */
    @Test
    public void testGetUserPasswordEncoded(){
        UserAccountForm u = new UserAccountForm();
        u.setUserEmail("test01//@Test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        String encodedPassword =  encoder.encode(u.getUserPassword());
        assertFalse(encodedPassword.compareTo(encoder.encode(u.getUserPassword()))==0);
    }

    @Test
    public void testPasswordsAreTheSame(){
        UserAccountForm u = new UserAccountForm();
        u.setUserEmail("test01//@Test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        assertTrue(u.passwordsAreTheSame());
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_23");
        assertFalse(u.passwordsAreTheSame());
    }
}
