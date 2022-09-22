# simpleworklist

[![Java CI with Maven](https://github.com/Spring-Framework-Java-Apps/simpleworklist/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/Spring-Framework-Java-Apps/simpleworklist/actions)
[![OSSAR](https://github.com/Spring-Framework-Java-Apps/simpleworklist/workflows/OSSAR/badge.svg)](https://github.com/Spring-Framework-Java-Apps/simpleworklist/actions)
[![Travis CI Build Status](https://travis-ci.com/Spring-Framework-Java-Apps/simpleworklist.svg?branch=master)](https://travis-ci.com/Spring-Framework-Java-Apps/simpleworklist)
[![Maven Project Reports](src/site/resources/img/maven-feather.png)](https://bloodmoneyapp.github.io/bloodmoney/)

Your Todo List for Getting Things Done

## Getting Things Done
GTD&reg; and Getting Things Done&reg; are registered trademarks of the David Allen Company. 
SimpleWorklist is not affiliated with or endorsed by the David Allen Company.

## Development
* [CHANGELOG](src/site/markdown/CHANGELOG.md)
* [TODO](src/site/markdown/TODO.md)

## URLs:
* Heroku Deployment: [simpleworklist.herokuapp.com/](https://simpleworklist.herokuapp.com/)
* For Developers: [localhost:8080](http://localhost:8080/)

## Nonfunctional Requirements
* [Nonfunctional Requirements](src/site/markdown/REQUIREMENTS_NONFUNCTIONAL.md)

## Functional Requirements

### Functional Requirements 001 - Start
![Functional Requirements 001 - Start](src/site/plantuml/Simpleworklist__Use_Cases__001__Start.png)
#### F001 Server Starts
#### F002 Home Page rendered
#### F003 Registration
#### F004 Password Recovery
#### F005 Login
#### F006 Page after first successful Login
#### F007 Logout

### Functional Requirements 002 - Show TaskstateTab
![Functional Requirements 002 - Show TaskstateTab](src/site/plantuml/Simpleworklist__Use_Cases__002__Show_TaskstateTab.png)
#### F010 show /taskstate/inbox
#### F011 show /taskstate/today
#### F012 show /taskstate/next
#### F013 show /taskstate/waiting
#### F014 show /taskstate/scheduled
#### F015 show /taskstate/someday
#### F016 show /taskstate/focus
#### F017 show /taskstate/completed
#### F018 show /taskstate/trash

### Functional Requirements 003 - TaskstateTab
![Functional Requirements 003 - TaskstateTab](src/site/plantuml/Simpleworklist__Use_Cases__003__TaskstateTab.png)
#### F040 Taskstate: Add Task to Inbox
#### F041 Taskstate: Add Task to Inbox again
#### F042 Taskstate: Task Edit
#### F043 Taskstate: Task Edit Form -> change Taskstate via DropDown
#### F044 Taskstate: Task Edit Form -> change Project via DropDown
#### F045 Taskstate: Task Edit Form -> transform to Project
#### F046 Taskstate: Task setFocus
#### F047 Taskstate: Task unSetFocus
#### F048 Taskstate: Task complete
#### F049 Taskstate: Task incomplete
#### F050 Taskstate: Task delete
#### F051 Taskstate: Task undelete

### Functional Requirements 004 - Project/Root
![Functional Requirements 004 - Project/Root](src/site/plantuml/Simpleworklist__Use_Cases__004__Project_Root.png)
#### F060 Project/Root: Add Task
#### F061 Project/Root: Add SubProject
#### F062 Project/Root: Task Edit
#### F063 Project/Root: Task Edit Form -> change Taskstate via DropDown
#### F064 Project/Root: Task Edit Form -> change Project via DropDown
#### F065 Project/Root: Task Edit Form -> transform to Project
#### F066 Project/Root: Task setFocus
#### F067 Project/Root: Task unSetFocus
#### F068 Project/Root: Task complete
#### F069 Project/Root: Task incomplete
#### F070 Project/Root: Task delete
#### F071 Project/Root: Task undelete

### Functional Requirements 005 - Project/id
![Functional Requirements 005 - Project/id](src/site/plantuml/Simpleworklist__Use_Cases__005__Project_id.png)
#### F080 Project/id: Add Task
#### F081 Project/id: Add SubProject
#### F082 Project/id: Task Edit
#### F083 Project/id: Task Edit Form -> change Taskstate via DropDown
#### F084 Project/id: Task Edit Form -> change Project via DropDown
#### F085 Project/id: Task Edit Form -> transform to Project
#### F086 Project/id: Task setFocus
#### F087 Project/id: Task unSetFocus
#### F088 Project/id: Task complete
#### F089 Project/id: Task incomplete
#### F090 Project/id: Task delete
#### F091 Project/id: Task undelete

### Functional Requirements 006 - Drag and Drop. Task move to TaskstateTab
![Functional Requirements 006 - Drag and Drop. Task move to TaskstateTab](src/site/plantuml/Simpleworklist__Use_Cases__006__Task_move_to_TaskstateTab.png)
#### F120 Drag and Drop: Task move to /taskstate/inbox
#### F121 Drag and Drop: Task move to /taskstate/today
#### F122 Drag and Drop: Task move to /taskstate/next
#### F123 Drag and Drop: Task move to /taskstate/waiting
#### F124 Drag and Drop: Task move to /taskstate/scheduled
#### F125 Drag and Drop: Task move to /taskstate/someday
#### F126 Drag and Drop: Task move to /taskstate/focus
#### F127 Drag and Drop: Task move to /taskstate/completed
#### F128 Drag and Drop: Task move to /taskstate/trash

### Functional Requirements 007 - Drag and Drop Move to Project
![Functional Requirements 007 - Drag and Drop Move to Project](src/site/plantuml/Simpleworklist__Use_Cases__007__Move_to_Project.png)
#### F129 Drag and Drop: Task move to Project
#### F130 Drag and Drop: Project move to Project

### Functional Requirements 008 - Drag and Drop Move Task to Change Task-Order
![Functional Requirements 008 - Drag and Drop Move Task to Change Task-Order](src/site/plantuml/Simpleworklist__Use_Cases__008__Move_Task_to_Change_Task_Order.png)
#### F131 Drag and Drop: Move Task to Change Task-Order in TaskstateTab
#### F132 Drag and Drop: Move Task to Change Task-Order in Project/Root
#### F133 Drag and Drop: Move Task to Change Task-Order in Project/id

### Functional Requirements 009 - Search
![Functional Requirements 009 - Search](src/site/plantuml/Simpleworklist__Use_Cases__009__Search.png)
#### F200 Search Request

### Domain Class Modell
#### Domain Class Modell - Persistent
![Domain_Class_Modell_Persistent](src/site/plantuml/Simpleworklist__Domain_Class_Modell_Persistent.png)
#### Domain Class Modell - Transient ( TODO )
![Domain_Class_Modell_Persistent](src/site/plantuml/Simpleworklist__Domain_Class_Modell_Transient.png)

## Software Design
### Service Classes Controller ( TODO )
![Service_Classes_Controller](src/site/plantuml/Simpleworklist__Service_Classes_Controller.png)
### Service Classes DB ( TODO )
![Service_Classes_DB](src/site/plantuml/Simpleworklist__Service_Classes_DB.png)
### Service Classes Meso ( TODO )
![Service_Classes_Meso](src/site/plantuml/Simpleworklist__Service_Classes_Meso.png)
