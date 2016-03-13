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
                "name"
        })
)
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId(name="id")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @ManyToOne(optional = false)
    @JoinColumn(name = "userAccountId")
    @IndexedEmbedded(includeEmbeddedObjectId=true)
    private UserAccount userAccount;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min=1,max=255)
    @Column(nullable = false)
    private String name;

    public Area(){}

    public Area(String name){
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        if (id != null ? !id.equals(area.id) : area.id != null) return false;
        if (!uuid.equals(area.uuid)) return false;
        if (!userAccount.equals(area.userAccount)) return false;
        return name.equals(area.name);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + uuid.hashCode();
        result = 31 * result + userAccount.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", userAccount=" + userAccount +
                ", name='" + name + '\'' +
                '}';
    }
}
