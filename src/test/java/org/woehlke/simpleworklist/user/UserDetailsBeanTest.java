package org.woehlke.simpleworklist.user;



import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.woehlke.simpleworklist.user.account.UserAccount;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class UserDetailsBeanTest {

    @Test
    public void testGetAuthorities(){
        UserAccount account = new UserAccount();
        UserDetailsBean b = new UserDetailsBean(account);
        Collection<? extends GrantedAuthority> c = b.getAuthorities();
        assertTrue(c.size()==1);
        for(GrantedAuthority authority:c){
            assertTrue(authority.getAuthority().compareTo("ROLE_USER")==0);
        }
    }

    @Test
    public void testDefaultBooleans(){
        UserAccount account = new UserAccount();
        UserDetailsBean b = new UserDetailsBean(account);
        assertTrue(b.isAccountNonExpired());
        assertTrue(b.isAccountNonLocked());
        assertTrue(b.isCredentialsNonExpired());
        assertTrue(b.isEnabled());
    }

    @Test
    public void testHashCodeAndEquals(){
        UserAccount account1 = new UserAccount();
        UserAccount account2 = new UserAccount();
        UserAccount account3 = new UserAccount();
        UserAccount account4 = new UserAccount();
        UserAccount account5 = new UserAccount();
        UserAccount account6 = new UserAccount();
        UserAccount account7 = new UserAccount();
        UserAccount account8 = new UserAccount();
        UserAccount account9 = new UserAccount();

        account1.setUserEmail("test01@test.de");
        account1.setUserPassword("pwd01");

        account2.setUserEmail("test01@test.de");
        account2.setUserPassword("pwd01");

        account3.setUserEmail("test03@test.de");
        account3.setUserPassword("pwd03");

        account4.setUserEmail("test01@test.de");
        account4.setUserPassword("pwd03");

        account5.setUserEmail(null);
        account5.setUserPassword(null);

        account6.setUserEmail(null);
        account6.setUserPassword("PWD_NOT_NULL_01");

        account7.setUserEmail("test03@test.de");
        account7.setUserPassword(null);

        account8.setUserEmail(null);
        account8.setUserPassword("PWD_NOT_NULL_02");

        account9.setUserEmail("test04@test.de");
        account9.setUserPassword(null);

        UserDetailsBean b1 = new UserDetailsBean(account1);
        UserDetailsBean b2 = new UserDetailsBean(account2);
        UserDetailsBean b3 = new UserDetailsBean(account3);
        UserDetailsBean b4 = new UserDetailsBean(account4);
        UserDetailsBean b5 = new UserDetailsBean(account5);
        UserDetailsBean b6 = new UserDetailsBean(account6);
        UserDetailsBean b7 = new UserDetailsBean(account7);
        UserDetailsBean b8 = new UserDetailsBean(account8);
        UserDetailsBean b9 = new UserDetailsBean(account9);

        assertEquals(b1.hashCode(),b2.hashCode());
        assertTrue(b1.hashCode()!=b3.hashCode());
        assertTrue(b1.hashCode()!=b4.hashCode());
        assertTrue(b1.hashCode()!=b5.hashCode());
        assertTrue(b1.hashCode()!=b6.hashCode());
        assertTrue(b1.hashCode()!=b7.hashCode());
        assertTrue(b1.hashCode()!=b8.hashCode());
        assertTrue(b1.hashCode()!=b9.hashCode());

        assertTrue(b1.equals(b1));
        assertTrue(b1.equals(b2));

        assertFalse(b1.equals(null));
        assertFalse(b1.equals(account1));
        assertFalse(b1.equals(b3));
        assertFalse(b1.equals(b4));
        assertFalse(b1.equals(b5));
        assertFalse(b1.equals(b6));
        assertFalse(b1.equals(b7));
        assertFalse(b1.equals(b8));
        assertFalse(b1.equals(b9));

        assertFalse(b3.equals(b1));
        assertFalse(b4.equals(b1));
        assertFalse(b5.equals(b1));
        assertFalse(b6.equals(b1));
        assertFalse(b7.equals(b1));
        assertFalse(b8.equals(b1));
        assertFalse(b9.equals(b1));
    }
}
