package org.woehlke.simpleworklist.entities.enumerations;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tw on 05.04.15.
 */
public enum TaskState {

    NEW,
    WORK,
    DONE;

    public static List<TaskState> list(){
        return Arrays.asList(values());
    }
}
