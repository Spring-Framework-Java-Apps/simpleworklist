package org.woehlke.simpleworklist.model.beans;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class NewUser2UserMessage implements Serializable {

    private static final long serialVersionUID = 1576610181966480168L;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.SIMPLE_TEXT)
    @NotBlank
    @Length(min=1,max=65535)
    private String messageText;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewUser2UserMessage)) return false;
        NewUser2UserMessage that = (NewUser2UserMessage) o;
        return getMessageText().equals(that.getMessageText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessageText());
    }

    @Override
    public String toString() {
        return "NewUser2UserMessage{" +
                "messageText='" + messageText + '\'' +
                '}';
    }
}
