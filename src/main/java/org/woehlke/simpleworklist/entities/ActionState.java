package org.woehlke.simpleworklist.entities;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tw on 05.04.15.
 */
public enum ActionState {

    NEW,
    WORK,
    DONE;

    public static List<ActionState> list(){
        return Arrays.asList(values());
    }
}
