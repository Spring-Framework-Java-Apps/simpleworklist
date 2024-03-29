package org.woehlke.java.simpleworklist.domain.db.user;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.woehlke.java.simpleworklist.domain.meso.language.Language;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.application.framework.ComparableById;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.persistence.Index;


@Entity
@Table(
    name="user_account",
    uniqueConstraints = {
        @UniqueConstraint(name="ux_user_account", columnNames = {"user_email"})
    },
    indexes = {
        @Index(name="ix_user_account_uuid", columnList = "uuid"),
        @Index(name="ix_user_account_row_created_at", columnList = "row_created_at"),
        @Index(name="ix_user_account_user_fullname", columnList = "user_fullname"),
        @Index(name="ix_user_account_last_login_timestamp", columnList = "last_login_timestamp")
    }
)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"userPassword","defaultLanguage","defaultContext","lastLoginTimestamp"})
public class UserAccount extends AuditModel implements Serializable, ComparableById<UserAccount>, Comparable<UserAccount> {

    @Serial
    private static final long serialVersionUID = 7860692526488291439L;

    @Id
    @GeneratedValue(generator = "user_account_generator")
    @SequenceGenerator(
        name = "user_account_generator",
        sequenceName = "user_account_sequence",
        initialValue = 1000
    )
    private Long id;

    @Email
    @NotBlank
    @Column(name="user_email", nullable = false)
    private String userEmail;

    @NotBlank
    @Column(name="user_password", nullable = false)
    private String userPassword;

    @NotBlank
    @Column(name="user_fullname", nullable = false)
    private String userFullname;

    @Column(name="default_language", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language defaultLanguage;

    //TODO: why nullable=true and optional = true?
    @OneToOne(
            fetch = FetchType.LAZY,
            optional = true,
            cascade = {
                CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "default_data_context_id", nullable=true)
    private Context defaultContext;

    //@NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name="last_login_timestamp", nullable = false)
    private LocalDateTime lastLoginTimestamp;

    //@NotNull
    @Column(name="account_non_expired", nullable = false)
    private Boolean accountNonExpired = true;

    //@NotNull
    @Column(name="account_non_locked", nullable = false)
    private Boolean accountNonLocked = true;

    //@NotNull
    @Column(name="account_credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired = true;

    //@NotNull
    @Column(name="account_enabled", nullable = false)
    private Boolean enabled = true;

    @Transient
    @Override
    public boolean equalsById(UserAccount otherObject) {
        return (this.getId().longValue() == otherObject.getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsByUniqueConstraint(UserAccount otherObject) {
        return (this.getUserEmail().compareTo(otherObject.getUserEmail())==0);
    }

    @Transient
    @Override
    public boolean equalsByUuid(UserAccount otherObject) {
        return super.equalsByMyUuid(otherObject);
    }

    public static UserAccount createUserAccount(
      final String userEmail,
      final String userFullname,
      final String userPassword,
      Context[] contexts
    ){
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(zone);
        UserAccount u = new UserAccount();
        u.setUserEmail(userEmail);
        u.setUserFullname(userFullname);
        u.setUserPassword(userPassword);
        u.setUuid(UUID.randomUUID());
        u.setLastLoginTimestamp(now);
        u.setAccountNonExpired(true);
        u.setAccountNonLocked(true);
        u.setCredentialsNonExpired(true);
        u.setEnabled(true);
        u.setDefaultLanguage(Language.EN);
        u.setDefaultContext(contexts[0]);
        for(Context context:contexts){
            context.setUserAccount(u);
        }
        return u;
    }

    @Override
    public int compareTo(UserAccount o) {
      return this.userEmail.compareTo(o.userEmail);
    }
}
