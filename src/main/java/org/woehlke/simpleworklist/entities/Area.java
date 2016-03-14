package org.woehlke.simpleworklist.entities;

import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by tw on 13.03.16.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "uuid",
                "userAccountId",
                "nameEn",
                "nameDe"
        })
)
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId(name = "id")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @ManyToOne(optional = false)
    @JoinColumn(name = "userAccountId")
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private UserAccount userAccount;

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(nullable = false)
    private String nameDe;

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(nullable = false)
    private String nameEn;

    public Area() {
    }

    public Area(String nameDe,String nameEn) {
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

        Area area = (Area) o;

        if (id != null ? !id.equals(area.id) : area.id != null) return false;
        if (uuid != null ? !uuid.equals(area.uuid) : area.uuid != null) return false;
        if (userAccount != null ? !userAccount.equals(area.userAccount) : area.userAccount != null) return false;
        if (nameDe != null ? !nameDe.equals(area.nameDe) : area.nameDe != null) return false;
        return nameEn != null ? nameEn.equals(area.nameEn) : area.nameEn == null;

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
        return "Area{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", userAccount=" + userAccount +
                ", nameDe='" + nameDe + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }
}
