package org.woehlke.java.simpleworklist.domain.db.user;

import lombok.Getter;
import lombok.Setter;
import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.application.framework.ComparableById;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(
    name="user_account_password",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_user_account_password",
            columnNames = {"user_account_id","user_password"}
        )
    },
    indexes = {
        @Index(name="ix_user_account_password_uuid", columnList = "uuid"),
        @Index(name="ix_user_account_password_row_created_at", columnList = "row_created_at")
    }
)
@Getter
@Setter
public class UserAccountPassword extends AuditModel implements Serializable, ComparableById<UserAccountPassword>, Comparable<UserAccountPassword> {

    private static final long serialVersionUID = 7860692526488291439L;

    @Id
    @GeneratedValue(generator = "user_account_password_generator")
    @SequenceGenerator(
        name = "user_account_password_generator",
        sequenceName = "user_account_password_sequence",
        initialValue = 1000
    )
    private Long id;

    @NotNull
    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false,
        cascade = {
            CascadeType.REFRESH
        }
    )
    @JoinColumn(name = "user_account_id",nullable=false)
    private UserAccount userAccount;

    @NotNull
    @Column(name="user_password", nullable = false)
    private String userPassword;

    @Override
    public boolean equalsById(UserAccountPassword otherObject) {
        return this.getId().compareTo(otherObject.getId())==0;
    }

    @Override
    public boolean equalsByUniqueConstraint(UserAccountPassword otherObject) {
        return equalsById(otherObject) && (this.getUserPassword().compareTo(otherObject.getUserPassword())==0);
    }

    @Override
    public boolean equalsByUuid(UserAccountPassword otherObject) {
        return this.getUuid().compareTo(otherObject.getUuid())==0;
    }

  @Override
  public int compareTo(UserAccountPassword o) {
    return this.rowCreatedAt.compareTo(o.rowCreatedAt);
  }
}
