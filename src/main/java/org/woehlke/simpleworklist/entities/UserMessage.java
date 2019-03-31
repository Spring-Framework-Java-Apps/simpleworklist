package org.woehlke.simpleworklist.entities;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Fert on 16.02.2016.
 */
@Entity
@Table(
    name="user_message",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_user_message",
            columnNames = {"created_timestamp", "user_account_id_sender", "user_account_id_receiver"}
        )
    },
    indexes = {
        @Index(name="ix_user_message_uuid",columnList = "uuid")
    }
)
public class UserMessage {

    @Id
    @GeneratedValue(generator = "user_message_generator")
    @SequenceGenerator(
        name = "user_message_generator",
        sequenceName = "user_message_sequence",
        initialValue = 1000
    )
    private Long id;

    @Column(name="uuid", nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.SIMPLE_TEXT)
    @NotBlank
    @Length(min=1,max=65535)
    @Column(name="message_text", nullable = false, length = 65535, columnDefinition="text")
    private String messageText;

    @NotNull
    @Column(name="read_by_receiver", columnDefinition = "boolean default false")
    private Boolean readByReceiver;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name="created_timestamp", nullable = false)
    private Date createdTimestamp = new Date();

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public boolean isReadByReceiver() {
        return readByReceiver;
    }

    public void setReadByReceiver(boolean read) {
        this.readByReceiver = read;
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

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserMessage)) return false;

        UserMessage that = (UserMessage) o;

        if (readByReceiver != that.readByReceiver) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (messageText != null ? !messageText.equals(that.messageText) : that.messageText != null) return false;
        if (createdTimestamp != null ? !createdTimestamp.equals(that.createdTimestamp) : that.createdTimestamp != null)
            return false;
        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;
        return receiver != null ? receiver.equals(that.receiver) : that.receiver == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (messageText != null ? messageText.hashCode() : 0);
        result = 31 * result + (readByReceiver ? 1 : 0);
        result = 31 * result + (createdTimestamp != null ? createdTimestamp.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", messageText='" + messageText + '\'' +
                ", readByReceiver=" + readByReceiver +
                ", createdTimestamp=" + createdTimestamp +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
