package org.woehlke.simpleworklist.entities.enumerations;

import java.io.Serializable;

public enum TaskStateView implements Serializable {

    INBOX("",""),
    TODAY("",""),
    NEXT("",""),
    WAITING("",""),
    SCHEDULED("",""),
    SOMEDAY("",""),
    COMPLETED("",""),
    TRASHED("",""),
    FOCUS("",""),
    ALL("","");

    TaskStateView(String urlPart, String messageCode) {
        this.urlPart = urlPart;
        this.messageCode = messageCode;
    }

    public String getUrlPart() {
        return urlPart;
    }

    public String getMessageCode() {
        return messageCode;
    }

    private String urlPart;
    private String messageCode;

    private static final long serialVersionUID = 0L;
}
