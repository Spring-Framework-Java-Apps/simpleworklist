package org.woehlke.simpleworklist.session;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.woehlke.simpleworklist.task.TaskState;

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
        lastSearchterm=""; //TODO: Make SearchRequest to Entity
        lastTaskState=TaskState.INBOX;
        lastProjectId=0L;
        lastContextId=0L;
        lastTaskId=0L;
        userAccountid=0L;
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

}
