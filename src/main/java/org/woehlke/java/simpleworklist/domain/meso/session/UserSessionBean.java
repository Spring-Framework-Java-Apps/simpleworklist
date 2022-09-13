package org.woehlke.java.simpleworklist.domain.meso.session;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;

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
    private TaskState lastTaskState; //TODO: make it to String?
    private String lastSearchterm; //TODO: Make SearchRequest to Entity

    public UserSessionBean(){
        this.userAccountid=0L;
        this.lastContextId=0L;
        this.lastProjectId=0L;
        this.lastTaskId=0L;
        this.lastTaskState=TaskState.INBOX;
        this.lastSearchterm=""; //TODO: Make SearchRequest to Entity
    }

    public UserSessionBean(long userAccountid, long lastContextId){
        this.userAccountid=userAccountid;
        this.lastContextId=lastContextId;
        this.lastProjectId=0L;
        this.lastTaskId=0L;
        this.lastTaskState=TaskState.INBOX;
        this.lastSearchterm=""; //TODO: Make SearchRequest to Entity
    }

    @Deprecated
    public UserSessionBean(long contextId){
        this.userAccountid=0L;
        this.lastContextId=contextId;
        this.lastProjectId=0L;
        this.lastTaskId=0L;
        this.lastTaskState=TaskState.INBOX;
        this.lastSearchterm=""; //TODO: Make SearchRequest to Entity
    }

    public void update(long userAccountid, long contextId) {
        this.userAccountid=userAccountid;
        if(this.lastContextId == 0L ){
            this.lastContextId=contextId;
        }
    }
}
