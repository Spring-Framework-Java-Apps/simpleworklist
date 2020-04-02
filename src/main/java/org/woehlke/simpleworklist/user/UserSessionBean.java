package org.woehlke.simpleworklist.user;

import org.woehlke.simpleworklist.task.TaskState;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by tw on 13.03.16.
 */
public class UserSessionBean implements Serializable {

    private static final long serialVersionUID = -6649686058228455825L;

    private Long contextId;

    private String lastSearchterm;

    private TaskState lastTaskState;

    private Long lastProjectId;

    public UserSessionBean(){
        lastSearchterm="";
        lastTaskState=TaskState.INBOX;
        lastProjectId=0L;
        contextId=0L;
    }

    public UserSessionBean(long contextId){
        lastSearchterm="";
        lastTaskState=TaskState.INBOX;
        lastProjectId=0L;
        this.contextId = contextId;
    }

    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    public String getLastSearchterm() {
        return lastSearchterm;
    }

    public void setLastSearchterm(String lastSearchterm) {
        this.lastSearchterm = lastSearchterm;
    }

    public TaskState getLastTaskState() {
        return lastTaskState;
    }

    public void setLastTaskState(TaskState lastTaskState) {
        this.lastTaskState = lastTaskState;
    }

    public Long getLastProjectId() {
        return lastProjectId;
    }

    public void setLastProjectId(Long lastProjectId) {
        this.lastProjectId = lastProjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSessionBean)) return false;
        UserSessionBean that = (UserSessionBean) o;
        return getContextId().equals(that.getContextId()) &&
                getLastSearchterm().equals(that.getLastSearchterm()) &&
                getLastTaskState() == that.getLastTaskState() &&
                getLastProjectId().equals(that.getLastProjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContextId(), getLastSearchterm(), getLastTaskState(), getLastProjectId());
    }

    @Override
    public String toString() {
        return "UserSessionBean{" +
                "contextId=" + contextId +
                ", lastSearchterm='" + lastSearchterm + '\'' +
                ", lastTaskState=" + lastTaskState +
                ", lastProjectId=" + lastProjectId +
                '}';
    }
}
