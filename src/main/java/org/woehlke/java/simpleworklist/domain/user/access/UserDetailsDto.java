package org.woehlke.java.simpleworklist.domain.user.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;


@Getter
@EqualsAndHashCode
@ToString(exclude = {"password"})
public class UserDetailsDto implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public UserDetailsDto(UserAccount account) {
        this.username = account.getUserEmail();
        this.password = account.getUserPassword();
        /*
        this.accountNonExpired = account.getAccountNonExpired();
        this.accountNonLocked = account.getAccountNonLocked();
        this.credentialsNonExpired = account.getCredentialsNonExpired();
        this.enabled = account.getEnabled();
         */
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}
