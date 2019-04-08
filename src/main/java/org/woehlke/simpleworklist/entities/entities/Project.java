package org.woehlke.simpleworklist.entities.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.persistence.Index;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.simpleworklist.entities.entities.impl.AuditModel;

@Entity
@Table(
    name="project",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_project",
            columnNames = {"uuid", "parent_id", "user_account_id", "context_id"}
        )
    }, indexes = {
        @Index(name = "ix_project_uuid", columnList = "uuid"),
        @Index(name = "ix_project_row_created_at", columnList = "row_created_at")
    }
)
@Indexed
public class Project extends AuditModel implements Serializable {

    private static final long serialVersionUID = 4566653175832872422L;

    @Id
    @GeneratedValue(generator = "project_generator")
    @SequenceGenerator(
        name = "project_generator",
        sequenceName = "project_sequence",
        initialValue = 1000
    )
    @DocumentId(name="id")
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Project parent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_account_id")
    @IndexedEmbedded(includeEmbeddedObjectId=true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private UserAccount userAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "context_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Context context;

    @SafeHtml(whitelistType=SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min=1,max=255)
    @Column(name="name",nullable = false)
    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String name;

    @SafeHtml(whitelistType=SafeHtml.WhiteListType.SIMPLE_TEXT)
    @NotBlank
    @Length(min=0,max=65535)
    @Column(name="description", nullable = true, length = 65535, columnDefinition="text")
    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = { CascadeType.ALL })
    private List<Project> children = new ArrayList<Project>();

    @Transient
    public boolean hasNoChildren() {
        return this.children.size() == 0;
    }

    @Transient
    public boolean isRootProject() {
        return this.parent == null;
    }

    public static Project newProjectFactory(Project parent) {
        Project n = new Project();
        n.setParent(parent);
        n.setUserAccount(parent.getUserAccount());
        n.setContext(parent.getContext());
        return n;
    }

    public static Project newRootProjectFactory(UserAccount userAccount) {
        Project n = new Project();
        n.setParent(null);
        n.setUserAccount(userAccount);
        return n;
    }

    public static Project newRootProjectFactory(UserAccount userAccount,Context context) {
        Project n = new Project();
        n.setParent(null);
        n.setUserAccount(userAccount);
        n.setContext(context);
        return n;
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

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        if (!super.equals(o)) return false;
        Project project = (Project) o;
        return Objects.equals(getId(), project.getId()) &&
                Objects.equals(getParent(), project.getParent()) &&
                getUserAccount().equals(project.getUserAccount()) &&
                getContext().equals(project.getContext()) &&
                getName().equals(project.getName()) &&
                getDescription().equals(project.getDescription()) &&
                getChildren().equals(project.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getParent(), getUserAccount(), getContext(), getName(), getDescription(), getChildren());
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", parent=" + parent +
                ", userAccount=" + userAccount +
                ", context=" + context +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", children=" + children +
                ", uuid='" + uuid + '\'' +
                ", rowCreatedAt=" + rowCreatedAt +
                ", rowUpdatedAt=" + rowUpdatedAt +
                '}';
    }
}
