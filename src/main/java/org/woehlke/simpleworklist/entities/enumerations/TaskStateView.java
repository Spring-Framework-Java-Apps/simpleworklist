package org.woehlke.simpleworklist.entities.enumerations;

public enum TaskStateView {

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
}
