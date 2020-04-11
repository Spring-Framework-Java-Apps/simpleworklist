package org.woehlke.simpleworklist.application.common;

public interface ComparableById<T extends AuditModel> {
    boolean equalsById(T otherObject);
    boolean equalsByUniqueConstraint(T otherObject);
    boolean equalsByUuid(T otherObject);
}