package org.woehlke.simpleworklist.entities;


import javax.persistence.Enumerated;

public enum RegistrationProcessType {

    @Enumerated
    REGISTRATION,

    @Enumerated
    PASSWORD_RECOVERY

}
