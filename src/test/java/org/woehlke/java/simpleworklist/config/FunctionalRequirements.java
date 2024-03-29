package org.woehlke.java.simpleworklist.config;

public enum FunctionalRequirements {

    F001("Server Starts"),
    F002("Home Page rendered"),
    F003("Registration"),
    F004("Password Recovery"),
    F005("Login"),
    F006("Page after first successful Login"),
    F007("Add first new Task to Inbox"),
    F008("Add another new Task to Inbox"),
    F009("Add Task to ProjectRoot"),
    F010("Add SubProject to ProjectRoot"),
    F011("setFocus of a Task"),
    F012("unSetFocus of a Task"),
    F013("show /taskstate/inbox"),
    F014("show /taskstate/today"),
    F015("show /taskstate/next"),
    F016("show /taskstate/waiting"),
    F017("show /taskstate/scheduled"),
    F018("show /taskstate/someday"),
    F019("show /taskstate/focus"),
    F020("show /taskstate/completed"),
    F021("show /taskstate/trash"),
    F022("Task Edit"),
    F023("Task Edit Form -> change Taskstate via DropDown"),
    F024("Task complete"),
    F025("Task incomplete"),
    F026("Task delete"),
    F027("Task undelete");

    private String name;

    public String getName(){
        return name;
    }

    FunctionalRequirements(String name){
        this.name = name;
    }
}
