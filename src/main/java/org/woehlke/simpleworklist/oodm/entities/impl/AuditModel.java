package org.woehlke.simpleworklist.oodm.entities.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"rowCreatedAt", "rowUpdatedAt"},
        allowGetters = true
)
public class AuditModel implements Serializable {

    private static final long serialVersionUID = 4399373914714726911L;

    @NotNull
    @Column(name="uuid", nullable = false)
    protected String uuid = UUID.randomUUID().toString();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "row_created_at", nullable = false, updatable = false)
    @CreatedDate
    protected Date rowCreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "row_updated_at", nullable = false)
    @LastModifiedDate
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
