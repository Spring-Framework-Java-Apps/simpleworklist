package org.woehlke.simpleworklist.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.id.UUIDGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
    value = {"rowCreatedAt", "rowUpdatedAt"},
    allowGetters = true
)
public class AuditModel implements Serializable {

    private static final long serialVersionUID = 4399373914714726911L;

    @Column(name="uuid", nullable = false)
    protected String uuid;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "row_created_at", nullable = false, updatable = false)
    protected Date rowCreatedAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "row_updated_at", nullable = false)
    protected Date rowUpdatedAt;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getRowCreatedAt() {
        return rowCreatedAt;
    }

    public void setRowCreatedAt(Date rowCreatedAt) {
        this.rowCreatedAt = rowCreatedAt;
    }

    public Date getRowUpdatedAt() {
        return rowUpdatedAt;
    }

    public void setRowUpdatedAt(Date rowUpdatedAt) {
        this.rowUpdatedAt = rowUpdatedAt;
    }

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
                ", rowCreatedAt=" + rowCreatedAt +
                ", rowUpdatedAt=" + rowUpdatedAt +
                '}';
    }

    protected boolean equalsByMyUuid(AuditModel otherObject){
       return(this.getUuid().compareTo(otherObject.getUuid())==0);
    }
}
