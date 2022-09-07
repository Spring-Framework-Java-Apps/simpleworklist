package org.woehlke.java.simpleworklist.domain.db.search;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.application.framework.ComparableById;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */

@Entity
@Table(
    name="search_result",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_search_result",
            columnNames = {"search_request_id"}
        )
    },
    indexes = {
        @Index(name = "ix_search_result_uuid", columnList = "uuid"),
        @Index(name = "ix_search_result_row_created_at", columnList = "row_created_at")
    }
)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult extends AuditModel implements Serializable, ComparableById<SearchResult> {

    private static final long serialVersionUID = 1682809351146047764L;

    @Id
    @GeneratedValue(generator = "search_result_generator")
    @SequenceGenerator(
        name = "search_result_generator",
        sequenceName = "search_result_sequence",
        initialValue = 1000
    )
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false,
        cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
        }
    )
    @JoinColumn(name = "search_request_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private SearchRequest searchRequest;

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
        }
    )
    @JoinTable(name = "search_result2task")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<Task> resultListTasks;

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
        }
    )
    @JoinTable(name = "search_result2project")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<Project> resultListProject;

    @Transient
    @Override
    public boolean equalsById(SearchResult otherObject) {
        return (this.getId().longValue() == otherObject.getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsByUniqueConstraint(SearchResult otherObject) {
        boolean okUuid = this.equalsByUuid(otherObject);
        boolean contextId = searchRequest.getContext().equalsByUniqueConstraint(otherObject.getSearchRequest().getContext());
        return okUuid && contextId;
    }

    @Transient
    @Override
    public boolean equalsByUuid(SearchResult otherObject) {
        return super.equalsByMyUuid(otherObject);
    }
}
