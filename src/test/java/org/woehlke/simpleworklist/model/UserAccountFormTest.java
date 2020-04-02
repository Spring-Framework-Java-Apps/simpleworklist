package org.woehlke.simpleworklist.model;

import org.junit.Assert;
import org.junit.Test;
import org.woehlke.simpleworklist.AbstractTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.user.UserAccountForm;


public class UserAccountFormTest extends AbstractTest {


    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder encoder;

    /**
     * This Test is obsolete now due to changed encoder from MD5 to BCrypt (20.02.2016).
     */
    @Test
    public void testGetUserPasswordEncoded(){
        UserAccountForm u = new UserAccountForm();
        u.setUserEmail("test01@test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        String encodedPassword =  encoder.encode(u.getUserPassword());
        Assert.assertTrue(encodedPassword.compareTo(encoder.encode(u.getUserPassword()))==0);
    }

    @Test
    public void testPasswordsAreTheSame(){
        UserAccountForm u = new UserAccountForm();
        u.setUserEmail("test01@test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        Assert.assertTrue(u.passwordsAreTheSame());
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_23");
        Assert.assertFalse(u.passwordsAreTheSame());
    }
}
