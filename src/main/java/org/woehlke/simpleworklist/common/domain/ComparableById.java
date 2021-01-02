package org.woehlke.simpleworklist.common.domain;

public interface ComparableById<T extends AuditModel> {
    boolean equalsById(T otherObject);
    boolean equalsByUniqueConstraint(T otherObject);
    boolean equalsByUuid(T otherObject);
}
