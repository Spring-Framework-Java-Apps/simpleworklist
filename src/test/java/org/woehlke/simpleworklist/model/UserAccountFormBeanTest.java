package org.woehlke.simpleworklist.model;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;


public class UserAccountFormBeanTest {

    @Test
    public void testGetUserPasswordEncoded(){
        UserAccountFormBean u = new UserAccountFormBean();
        u.setUserEmail("test01@test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        PasswordEncoder encoder = new Md5PasswordEncoder();
        String encodedPassword =  encoder.encodePassword(u.getUserPassword(), null);
        Assert.assertTrue(encodedPassword.compareTo(u.getUserPasswordEncoded())==0);
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
