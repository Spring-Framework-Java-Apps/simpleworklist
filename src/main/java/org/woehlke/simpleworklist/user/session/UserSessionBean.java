package org.woehlke.simpleworklist.user.session;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.woehlke.simpleworklist.domain.task.TaskState;

import java.io.Serializable;

/**
 * Created by tw on 13.03.16.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserSessionBean implements Serializable {

    private static final long serialVersionUID = -6649686058228455825L;

    private Long userAccountid;
    private Long lastContextId;
    private Long lastProjectId;
    private Long lastTaskId;
    private TaskState lastTaskState;
    private String lastSearchterm; //TODO: Make SearchRequest to Entity

    public UserSessionBean(){
        this.lastSearchterm=""; //TODO: Make SearchRequest to Entity
        this.lastTaskState=TaskState.INBOX;
        this.lastProjectId=0L;
        this.lastContextId=0L;
        this.lastTaskId=0L;
        this.userAccountid=0L;
    }

    public UserSessionBean(long userAccountid, long lastContextId){
        this.lastSearchterm=""; //TODO: Make SearchRequest to Entity
        this.lastTaskState=TaskState.INBOX;
        this.lastProjectId=0L;
        this.lastTaskId=0L;
        this.userAccountid=userAccountid;
        this.lastContextId=lastContextId;
    }

    @Deprecated
    public UserSessionBean(long contextId){
        this.lastContextId = contextId;
        lastSearchterm=""; //TODO: Make SearchRequest to Entity
        lastTaskState=TaskState.INBOX;
        lastProjectId=0L;
        lastTaskId=0L;
        userAccountid=0L;
    }

    public void update(long userAccountid, long contextId) {
        this.userAccountid=userAccountid;
        if(this.lastContextId == 0L ){
            this.lastContextId=contextId;
        }
    }
}
