package org.woehlke.simpleworklist.model;

import org.junit.Assert;
import org.junit.Test;
import org.woehlke.simpleworklist.AbstractTest;

import javax.inject.Inject;


public class UserAccountFormBeanTest extends AbstractTest {


    @Inject
    private org.springframework.security.crypto.password.PasswordEncoder encoder;

    /**
     * This Test is obsolete now due to changed encoder from MD5 to BCrypt (20.02.2016).
     */
    @Test
    public void testGetUserPasswordEncoded(){
        UserAccountFormBean u = new UserAccountFormBean();
        u.setUserEmail("test01@test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        String encodedPassword =  encoder.encode(u.getUserPassword());
        Assert.assertTrue(encodedPassword.compareTo(encoder.encode(u.getUserPassword()))==0);
    }

    @Test
    public void testPasswordsAreTheSame(){
        UserAccountFormBean u = new UserAccountFormBean();
        u.setUserEmail("test01@test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        Assert.assertTrue(u.passwordsAreTheSame());
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_23");
        Assert.assertFalse(u.passwordsAreTheSame());
    }
}
