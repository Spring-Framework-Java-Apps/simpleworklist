package org.woehlke.java.simpleworklist.domain.db.search;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.application.framework.ComparableById;
import org.woehlke.java.simpleworklist.domain.db.data.Context;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(
    name="search_request",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_search_request",
            columnNames = {"data_context_id", "searchterm"}
        )
    },
    indexes = {
        @Index(name = "ix_search_request_uuid", columnList = "uuid"),
        @Index(name = "ix_search_request_row_created_at", columnList = "row_created_at")
    }
)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest extends AuditModel implements Serializable, ComparableById<SearchRequest>,Comparable<SearchRequest> {

    private static final long serialVersionUID = 7860692526488291439L;

    @Id
    @GeneratedValue(generator = "search_request_generator")
    @SequenceGenerator(
        name = "search_request_generator",
        sequenceName = "search_request_sequence",
        initialValue = 1000
    )
    private Long id;

    @NotNull
    @Column(name="searchterm", nullable = false)
    private String searchterm = "";

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

    @Transient
    @Override
    public boolean equalsById(SearchRequest otherObject) {
        return (this.getId().longValue() == otherObject.getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsByUniqueConstraint(SearchRequest otherObject) {
        return (this.getSearchterm().compareTo(otherObject.getSearchterm())==0);
    }

    @Transient
    @Override
    public boolean equalsByUuid(SearchRequest otherObject) {
        return super.equalsByMyUuid(otherObject);
    }

  @Override
  public int compareTo(SearchRequest o) {
    return this.rowCreatedAt.compareTo(o.rowCreatedAt);
  }
}
