package org.woehlke.java.simpleworklist.application.framework;

public interface ComparableById<T extends AuditModel> {
    boolean equalsById(T otherObject);
    boolean equalsByUniqueConstraint(T otherObject);
    boolean equalsByUuid(T otherObject);
}
