package org.woehlke.simpleworklist.user;

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

    private Long contextId;
    private TaskState lastTaskState;
    private Long lastProjectId;
    private String lastSearchterm;

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

}
