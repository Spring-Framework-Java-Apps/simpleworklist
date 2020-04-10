package org.woehlke.simpleworklist.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.persistence.Index;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.common.AuditModel;
import org.woehlke.simpleworklist.common.ComparableById;
import org.woehlke.simpleworklist.user.account.UserAccount;

@Entity
@Table(
    name="project",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_project",
            columnNames = {"uuid", "parent_id", "context_id"}
        )
    }, indexes = {
        @Index(name = "ix_project_uuid", columnList = "uuid"),
        @Index(name = "ix_project_row_created_at", columnList = "row_created_at")
    }
)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Project extends AuditModel implements Serializable, ComparableById<Project> {

    private static final long serialVersionUID = 4566653175832872422L;
    public final static long rootProjectId = 0L;

    @Id
    @GeneratedValue(generator = "project_generator")
    @SequenceGenerator(
        name = "project_generator",
        sequenceName = "project_sequence",
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
    @JoinColumn(name = "context_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Context context;

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.RELAXED)
    @Length(min = 0, max = 65535)
    @Column(name = "description", nullable = true, length = 65535, columnDefinition = "text")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = {CascadeType.ALL})
    private List<Project> children = new ArrayList<>();

    @Transient
    public String getUrl() {
        if (this.getId() == null || this.getId() == 0L) {
            return "redirect:/project/root";
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

    public static Project newProjectFactory(Project parent) {
        Project n = new Project();
        n.setParent(parent);
        n.setContext(parent.getContext());
        return n;
    }

    public static Project newRootProjectFactory(UserAccount userAccount) {
        Project n = new Project();
        n.setParent(null);
        return n;
    }

    public static Project newRootProjectFactory(Context context) {
        Project n = new Project();
        n.setParent(null);
        n.setContext(context);
        return n;
    }


    //TODO: use newRootProjectFactory(Context context);
    @Deprecated
    public static Project newRootProjectFactory(UserAccount userAccount, Context context) {
        Project n = new Project();
        n.setParent(null);
        n.setContext(context);
        return n;
    }

}

