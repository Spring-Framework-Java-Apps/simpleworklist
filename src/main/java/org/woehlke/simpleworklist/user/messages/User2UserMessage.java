package org.woehlke.simpleworklist.user.messages;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.simpleworklist.oodm.entities.impl.AuditModel;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Fert on 16.02.2016.
 */
@Entity
@Table(
    name="user_message",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_user_message",
            columnNames = {"row_created_at", "user_account_id_sender", "user_account_id_receiver"}
        )
    },
    indexes = {
        @Index(name = "ix_user_message_uuid", columnList = "uuid"),
        @Index(name = "ix_user_message_row_created_at", columnList = "row_created_at")
    }
)
public class User2UserMessage extends AuditModel implements Serializable {

    private static final long serialVersionUID = 4263078228257938175L;

    @Id
    @GeneratedValue(generator = "user_message_generator")
    @SequenceGenerator(
        name = "user_message_generator",
        sequenceName = "user_message_sequence",
        initialValue = 1000
    )
    private Long id;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.SIMPLE_TEXT)
    @NotBlank
    @Length(min=1,max=65535)
    @Column(name="message_text", nullable = false, length = 65535, columnDefinition="text")
    private String messageText;

    @NotNull
    @Column(name="read_by_receiver", columnDefinition = "boolean default false")
    private Boolean readByReceiver;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_account_id_sender", nullable = false)
    private UserAccount sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_account_id_receiver", nullable = false)
    private UserAccount receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Boolean isReadByReceiver() {
        return readByReceiver;
    }

    public void setReadByReceiver(Boolean readByReceiver) {
        this.readByReceiver = readByReceiver;
    }

    public UserAccount getSender() {
        return sender;
    }

    public void setSender(UserAccount sender) {
        this.sender = sender;
    }

    public UserAccount getReceiver() {
        return receiver;
    }

    public void setReceiver(UserAccount receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User2UserMessage)) return false;
        if (!super.equals(o)) return false;
        User2UserMessage that = (User2UserMessage) o;
        return Objects.equals(getId(), that.getId()) &&
                getMessageText().equals(that.getMessageText()) &&
                isReadByReceiver().equals(that.isReadByReceiver()) &&
                getSender().equals(that.getSender()) &&
                getReceiver().equals(that.getReceiver());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getMessageText(), isReadByReceiver(), getSender(), getReceiver());
    }

    @Override
    public String toString() {
        return "User2UserMessage{" +
                "id=" + id +
                ", messageText='" + messageText + '\'' +
                ", readByReceiver=" + readByReceiver +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", uuid='" + uuid + '\'' +
                ", rowCreatedAt=" + rowCreatedAt +
                ", rowUpdatedAt=" + rowUpdatedAt +
                '}';
    }
}
