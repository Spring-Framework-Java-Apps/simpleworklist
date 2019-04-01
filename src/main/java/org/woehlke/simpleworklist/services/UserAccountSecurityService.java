package org.woehlke.simpleworklist.services;

import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAccountSecurityService extends UserDetailsService, UserDetailsPasswordService {
}
