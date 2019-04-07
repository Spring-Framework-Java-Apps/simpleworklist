package org.woehlke.simpleworklist.entities;

import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.persistence.Index;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tw on 13.03.16.
 */
@Entity
@Table(
    name="context",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_context",
            columnNames = {"uuid", "user_account_id", "name_de", "name_en" }
        )
    },
    indexes={
        @Index(name="ix_context_uuid", columnList = "uuid")
    }
)
public class Context implements Serializable {

    private static final long serialVersionUID = -5035732370606951871L;

    @Id
    @GeneratedValue(generator = "context_generator")
    @SequenceGenerator(
            name = "context_generator",
            sequenceName = "context_sequence",
            initialValue = 1000
    )
    @DocumentId(name = "id")
    private Long id;

    @NotNull
    @Column(name="uuid", nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_account_id")
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private UserAccount userAccount;

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(name = "name_de", nullable = false)
    private String nameDe;

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(name = "name_en", nullable = false)
    private String nameEn;

    public Context() {
    }

    public Context(String nameDe, String nameEn) {
        this.nameDe = nameDe;
        this.nameEn = nameEn;
    }

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

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String name) {
        this.nameDe = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Context context = (Context) o;

        if (id != null ? !id.equals(context.id) : context.id != null) return false;
        if (uuid != null ? !uuid.equals(context.uuid) : context.uuid != null) return false;
        if (userAccount != null ? !userAccount.equals(context.userAccount) : context.userAccount != null) return false;
        if (nameDe != null ? !nameDe.equals(context.nameDe) : context.nameDe != null) return false;
        return nameEn != null ? nameEn.equals(context.nameEn) : context.nameEn == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (userAccount != null ? userAccount.hashCode() : 0);
        result = 31 * result + (nameDe != null ? nameDe.hashCode() : 0);
        result = 31 * result + (nameEn != null ? nameEn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Context{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", userAccount=" + userAccount +
                ", nameDe='" + nameDe + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }
}
