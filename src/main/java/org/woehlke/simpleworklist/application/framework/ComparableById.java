package org.woehlke.simpleworklist.application.framework;

import org.woehlke.simpleworklist.application.framework.AuditModel;

public interface ComparableById<T extends AuditModel> {
    boolean equalsById(T otherObject);
    boolean equalsByUniqueConstraint(T otherObject);
    boolean equalsByUuid(T otherObject);
}
