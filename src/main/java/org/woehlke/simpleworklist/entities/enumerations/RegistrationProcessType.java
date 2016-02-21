package org.woehlke.simpleworklist.entities.enumerations;


import javax.persistence.Enumerated;

public enum RegistrationProcessType {

    @Enumerated
    REGISTRATION,

    @Enumerated
    PASSWORD_RECOVERY

}
