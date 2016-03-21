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
                "userAccountId",
                "areaId"
        })
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "areaId")
    private Area area;

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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (id != null ? !id.equals(project.id) : project.id != null) return false;
        if (uuid != null ? !uuid.equals(project.uuid) : project.uuid != null) return false;
        if (parent != null ? !parent.equals(project.parent) : project.parent != null) return false;
        if (userAccount != null ? !userAccount.equals(project.userAccount) : project.userAccount != null) return false;
        if (area != null ? !area.equals(project.area) : project.area != null) return false;
        if (name != null ? !name.equals(project.name) : project.name != null) return false;
        if (description != null ? !description.equals(project.description) : project.description != null) return false;
        return children != null ? children.equals(project.children) : project.children == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (userAccount != null ? userAccount.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", parent=" + parent +
                ", userAccount=" + userAccount +
                ", area=" + area +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static Project newProjectFactory(Project parent) {
        Project n = new Project();
        n.setParent(parent);
        n.setUserAccount(parent.getUserAccount());
        n.setArea(parent.getArea());
        return n;
    }

    public static Project newRootProjectFactory(UserAccount userAccount) {
        Project n = new Project();
        n.setParent(null);
        n.setUserAccount(userAccount);
        return n;
    }

    public static Project newRootProjectFactory(UserAccount userAccount,Area area) {
        Project n = new Project();
        n.setParent(null);
        n.setUserAccount(userAccount);
        n.setArea(area);
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
