package org.woehlke.simpleworklist.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "uuid",
                "parentId",
                "userAccountId" }
        )
)
@Indexed
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId(name="id")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @ManyToOne(optional = true)
    @JoinColumn(name = "parentId")
    private Project parent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userAccountId")
    @IndexedEmbedded(includeEmbeddedObjectId=true)
    private UserAccount userAccount;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min=1,max=255)
    @Column(nullable = false)
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String name;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.SIMPLE_TEXT)
    @NotBlank
    @Length(min=0,max=65535)
    @Column(nullable = true, length = 65535, columnDefinition="text")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = { CascadeType.ALL })
    private List<Project> children = new ArrayList<Project>();

    @Transient
    public boolean isRootNode() {
        return parent == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getParent() {
        return parent;
    }

    public void setParent(Project parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Project> getChildren() {
        return children;
    }

    public void setChildren(List<Project> children) {
        this.children = children;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result
                + ((userAccount == null) ? 0 : userAccount.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Project other = (Project) obj;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (userAccount == null) {
            if (other.userAccount != null)
                return false;
        } else if (!userAccount.equals(other.userAccount))
            return false;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CategoryNode [id=" + id + ", uuid=" + uuid + ", parent="
                + parent + ", name=" + name + ", description=" + description
                + "]";
    }

    public static Project newProjectFactory(Project parent) {
        Project n = new Project();
        n.setParent(parent);
        n.setUserAccount(parent.getUserAccount());
        return n;
    }

    public static Project newRootProjectFactory(UserAccount userAccount) {
        Project n = new Project();
        n.setParent(null);
        n.setUserAccount(userAccount);
        return n;
    }

    @Transient
    public boolean hasNoChildren() {
        return this.children.size() == 0;
    }

    @Transient
    public boolean isRootCategory() {
        return this.parent == null;
    }
}
