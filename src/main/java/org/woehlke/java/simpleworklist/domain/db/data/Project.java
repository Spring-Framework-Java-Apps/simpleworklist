package org.woehlke.java.simpleworklist.domain.db.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.application.framework.ComparableById;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Boolean.FALSE;

@Entity
@Table(
    name="data_project",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_project",
            columnNames = {"uuid", "parent_id", "data_context_id"}
        )
    }, indexes = {
        @Index(name = "ix_project_uuid", columnList = "uuid"),
        @Index(name = "ix_project_row_created_at", columnList = "row_created_at")
    }
)
@Getter
@Setter
@ToString(callSuper = true, exclude = {"children","parent","description"})
public class Project extends AuditModel implements Serializable, ComparableById<Project>, Comparable<Project> {

    private static final long serialVersionUID = 4566653175832872422L;
    public final static long rootProjectId = 0L;

    @Id
    @GeneratedValue(generator = "data_project_generator")
    @SequenceGenerator(
        name = "data_project_generator",
        sequenceName = "data_project_sequence",
        initialValue = 1000
    )
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = true,
        cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
        }
    )
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Project parent;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false,
        cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
        }
    )
    @JoinColumn(name = "data_context_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Context context;

    @NotBlank
    @Length(min = 1, max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Length(min = 0, max = 65535)
    @Column(name = "description", nullable = true, length = 65535, columnDefinition = "text")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = {CascadeType.ALL})
    private List<Project> children = new ArrayList<>();

    @NotNull
    @Column(name = "collapsed", nullable = false)
    private Boolean collapsed = FALSE;

    @Transient
    public String getUrlRoot() {
      return "redirect:/project/root";
    }

    @Transient
    public String getUrl() {
        if (this.getId() == null || this.getId() == 0L) {
            return getUrlRoot();
        } else {
            return "redirect:/project/" + this.getId();
        }
    }

    @Transient
    public boolean hasNoChildren() {
        return this.children.size() == 0;
    }

    @Transient
    public boolean isRootProject() {
        return this.parent == null;
    }

    @Transient
    public boolean hasUser(UserAccount thisUser) {
        return (this.getContext().hasThisUser(thisUser));
    }

    @Transient
    @Override
    public boolean equalsById(Project otherObject) {
        return (this.getId().longValue() == otherObject.getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsByUniqueConstraint(Project otherObject) {
        boolean okParent;
        if (this.isRootProject()) {
            okParent = (otherObject.isRootProject());
        } else {
            okParent = this.getParent().equalsByUniqueConstraint(otherObject.getParent());
        }
        boolean okContext = this.getContext().equalsByUniqueConstraint(otherObject.getContext());
        boolean okUuid = this.equalsByUuid(otherObject);
        return okParent && okContext && okUuid;
    }

    @Transient
    @Override
    public boolean equalsByUuid(Project otherObject) {
        return super.equalsByMyUuid(otherObject);
    }

    public static Project newProjectFactoryForParentProject(Project parent) {
        Project thisProject = new Project();
        thisProject.setUuid(UUID.randomUUID());
        thisProject.setParent(parent);
        thisProject.setContext(parent.getContext());
        return thisProject;
    }

    public static Project newRootProjectFactory(Context context) {
        Project thisProject = new Project();
        thisProject.setUuid(UUID.randomUUID());
        thisProject.setParent(null);
        thisProject.setContext(context);
        return thisProject;
    }

    public Project addOtherProjectToChildren(Project otherProject) {
        children.add(otherProject);
        otherProject.setParent(this);
        return otherProject;
    }

    public String out(){
        return "Project: "+name+" ("+id+")";
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Project)) return false;
      if (!super.equals(o)) return false;
      Project project = (Project) o;
      return Objects.equals(getParent(), project.getParent()) && Objects.equals(getContext(), project.getContext()) && Objects.equals(getName(), project.getName()) && Objects.equals(getDescription(), project.getDescription());
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), getParent(), getContext(), getName(), getDescription());
    }

    @Override
    public int compareTo(Project o) {
      return this.name.compareTo(o.name);
    }

}

