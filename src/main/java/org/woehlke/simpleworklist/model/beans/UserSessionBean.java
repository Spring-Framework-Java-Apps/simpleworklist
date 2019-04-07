package org.woehlke.simpleworklist.model.beans;

import java.io.Serializable;

/**
 * Created by tw on 13.03.16.
 */
public class UserSessionBean implements Serializable {

    private static final long serialVersionUID = -6649686058228455825L;

    private Long contextId;

    public UserSessionBean(){
        contextId=0L;
    }

    public UserSessionBean(long contextId){
        this.contextId = contextId;
    }

    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSessionBean that = (UserSessionBean) o;

        return contextId == that.contextId;
    }

    @Override
    public int hashCode() {
        return (int) (contextId ^ (contextId >>> 32));
    }

    @Override
    public String toString() {
        return "UserSessionBean{" +
                "contextId=" + contextId +
                '}';
    }
}
