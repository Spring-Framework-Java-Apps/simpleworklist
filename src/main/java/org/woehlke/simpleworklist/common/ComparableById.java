package org.woehlke.simpleworklist.common;

import org.woehlke.simpleworklist.common.AuditModel;

public interface ComparableById<T extends AuditModel> {
    boolean equalsById(T otherObject);
    boolean equalsByUniqueConstraint(T otherObject);
    boolean equalsByUuid(T otherObject);
}
