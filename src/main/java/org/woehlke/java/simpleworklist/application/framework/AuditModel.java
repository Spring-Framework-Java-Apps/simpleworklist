package org.woehlke.java.simpleworklist.application.framework;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
    value = {"rowCreatedAt", "rowUpdatedAt"},
    allowGetters = true
)
@Getter
@Setter
public class AuditModel extends Object implements Serializable {

    static final long serialVersionUID = 4399373914714726911L;

    @Column(name="uuid", nullable = false)
    protected UUID uuid;

    @CreatedDate
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "row_created_at", nullable = false, updatable = false)
    protected LocalDateTime rowCreatedAt;

    @LastModifiedDate
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "row_updated_at", nullable = false)
    protected LocalDateTime rowUpdatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditModel)) return false;
        AuditModel that = (AuditModel) o;
        return getUuid().equals(that.getUuid()) &&
                getRowCreatedAt().equals(that.getRowCreatedAt()) &&
                getRowUpdatedAt().equals(that.getRowUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getRowCreatedAt(), getRowUpdatedAt());
    }

    @Override
    public String toString() {
        return "AuditModel{" +
                "uuid='" + uuid + '\'' +
                '}';
    }

    protected boolean equalsByMyUuid(AuditModel otherObject){
       return(this.getUuid().compareTo(otherObject.getUuid())==0);
    }
}
