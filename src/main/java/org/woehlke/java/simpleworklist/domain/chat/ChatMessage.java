package org.woehlke.java.simpleworklist.domain.chat;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
//import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Fert on 16.02.2016.
 */
@Entity
@Table(
    name="user_chat_message",
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
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends AuditModel implements Serializable {

    private static final long serialVersionUID = 4263078228257938175L;

    @Id
    @GeneratedValue(generator = "user_message_generator")
    @SequenceGenerator(
        name = "user_message_generator",
        sequenceName = "user_message_sequence",
        initialValue = 1000
    )
    private Long id;

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

}
