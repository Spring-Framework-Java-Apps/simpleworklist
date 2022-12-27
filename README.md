# simpleworklist

[![Java CI with Maven](https://github.com/Spring-Framework-Java-Apps/simpleworklist/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/Spring-Framework-Java-Apps/simpleworklist/actions)
[![OSSAR](https://github.com/Spring-Framework-Java-Apps/simpleworklist/workflows/OSSAR/badge.svg)](https://github.com/Spring-Framework-Java-Apps/simpleworklist/actions)
[![Travis CI Build Status](https://travis-ci.com/Spring-Framework-Java-Apps/simpleworklist.svg?branch=master)](https://app.travis-ci.com/github/Spring-Framework-Java-Apps/simpleworklist)
[![Maven Project Reports](src/site/resources/img/maven-feather.png)](https://java.woehlke.org/simpleworklist/)

Your Todo List for Getting Things Done

## Getting Things Done
* GTD&reg; and Getting Things Done&reg; are registered trademarks of the David Allen Company. 
* SimpleWorklist is not affiliated with or endorsed by the David Allen Company.

## Development
* [CHANGELOG](src/site/markdown/CHANGELOG.md)
* [TODO](src/site/markdown/TODO.md)

## URLs:
* Heroku Deployment: [simpleworklist.herokuapp.com/](https://simpleworklist.herokuapp.com/)
* For Developers: [localhost:8080](http://localhost:8080/)

## Nonfunctional Requirements
* [Nonfunctional Requirements](src/site/markdown/REQUIREMENTS_NONFUNCTIONAL.md)

## Functional Requirements
* [Functional Requirements](src/site/markdown/REQUIREMENTS_FUNCTIONAL.md)

### Functional Requirements 001 - Start
![Functional Requirements 001 - Start](src/site/resources/plantuml/Simpleworklist__Use_Cases__001__Start.png)
#### F001 Server Starts
#### F002 Home Page rendered
#### F003 Registration
#### F004 Password Recovery
#### F005 Login
![Functional Requirements F005 Login](src/site/resources/screenshots/screen01.png)
#### F006 Page after first successful Login
![Functional Requirements F006 Page after first successful Login](src/site/resources/screenshots/screen02.png)
#### F007 Logout

### Functional Requirements 002 - Show TaskstateTab
![Functional Requirements 002 - Show TaskstateTab](src/site/resources/plantuml/Simpleworklist__Use_Cases__002__Show_TaskstateTab.png)
#### F010 show /taskstate/inbox
![Functional Requirements F010 show /taskstate/inbox](src/site/resources/screenshots/screen03.png)
#### F011 show /taskstate/today
![Functional Requirements F011 show /taskstate/today](src/site/resources/screenshots/screen04.png)
#### F012 show /taskstate/next
![Functional Requirements F012 show /taskstate/next](src/site/resources/screenshots/screen05.png)
#### F013 show /taskstate/waiting
![Functional Requirements F013 show /taskstate/waiting](src/site/resources/screenshots/screen06.png)
#### F014 show /taskstate/scheduled
![Functional Requirements F014 show /taskstate/scheduled](src/site/resources/screenshots/screen07.png)
#### F015 show /taskstate/someday
![Functional Requirements F015 show /taskstate/someday](src/site/resources/screenshots/screen08.png)
#### F016 show /taskstate/focus
![Functional Requirements F016 show /taskstate/focus](src/site/resources/screenshots/screen09.png)
#### F017 show /taskstate/completed
![Functional Requirements F017 show /taskstate/completed](src/site/resources/screenshots/screen10.png)
#### F018 show /taskstate/trash
![Functional Requirements F018 show /taskstate/trash](src/site/resources/screenshots/screen11.png)
#### F019 show /project/{projectid}
![Functional Requirements F006 Pe after first successful Loginag](src/site/resources/screenshots/screen13.png)
#### F020 show /taskstate/all
![Functional Requirements F020 show /taskstate/all](src/site/resources/screenshots/screen14.png)

### Functional Requirements 003 - TaskstateTab
![Functional Requirements 003 - TaskstateTab](src/site/resources/plantuml/Simpleworklist__Use_Cases__003__TaskstateTab.png)
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
![Functional Requirements 004 - Project/Root](src/site/resources/plantuml/Simpleworklist__Use_Cases__004__Project_Root.png)
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
![Functional Requirements 005 - Project/id](src/site/resources/plantuml/Simpleworklist__Use_Cases__005__Project_id.png)
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
![Functional Requirements 006 - Drag and Drop. Task move to TaskstateTab](src/site/resources/plantuml/Simpleworklist__Use_Cases__006__Task_move_to_TaskstateTab.png)
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
![Functional Requirements 007 - Drag and Drop Move to Project](src/site/resources/plantuml/Simpleworklist__Use_Cases__007__Move_to_Project.png)
#### F129 Drag and Drop: Task move to Project
#### F130 Drag and Drop: Project move to Project

### Functional Requirements 008 - Drag and Drop Move Task to Change Task-Order
![Functional Requirements 008 - Drag and Drop Move Task to Change Task-Order](src/site/resources/plantuml/Simpleworklist__Use_Cases__008__Move_Task_to_Change_Task_Order.png)
#### F131 Drag and Drop: Move Task to Change Task-Order in TaskstateTab
#### F132 Drag and Drop: Move Task to Change Task-Order in Project/Root
#### F133 Drag and Drop: Move Task to Change Task-Order in Project/id

### Functional Requirements 009 - Pages
#### F140 Pages Information
![Functional Requirements F140 Pages Information](src/site/resources/screenshots/screen15.png)

### Functional Requirements 010 - Search
![Functional Requirements 010 - Search](src/site/resources/plantuml/Simpleworklist__Use_Cases__009__Search.png)
#### F200 Search Request

## Functional Requirements - User
### Functional Requirements 001 - Start
![Functional Requirements 001 - Start](src/site/resources/plantuml/Simpleworklist__Use_Cases__001__Start.png)
#### F003 Registration
#### F004 Password Recovery
#### F005 Login
#### F007 Logout

### Functional Requirements 011 - User Profile
![Functional Requirements 010 - User Profile](src/site/resources/plantuml/Simpleworklist__Use_Cases__010__User_Profile.png)
#### F200 List of other Users and New Messages received from them
![Functional Requirements F006 Page after first successful Login](src/site/resources/screenshots/screen19.png)
#### F201 Chat Messages shared with one other User
![Functional Requirements F006 Page after first successful Login](src/site/resources/screenshots/screen30.png)
#### F202 Send New Chat Message to one other User
![Functional Requirements F006 Page after first successful Login](src/site/resources/screenshots/screen28.png)
#### F220 Menu Selfservice: Change Username
#### F221 Menu Selfservice: Change Password
#### F222 Menu Selfservice: Change Contexts
#### F223 Menu Selfservice: Set Default Language
#### F224 Menu Selfservice: Create Test Data

### Functional Requirements 012 - On every Page behind Login
![Functional Requirements 012 - User Profile](src/site/resources/plantuml/Simpleworklist__Use_Cases__010__User_Profile.png)
#### F250 Number of new incoming Messages from other Users


### Domain Class Modell
#### Domain Class Modell - Persistent
![Domain_Class_Modell_Persistent](src/site/resources/plantuml/Simpleworklist__Domain_Class_Modell_Persistent.png)
#### Domain Class Modell - Transient
![Domain_Class_Modell_Persistent](src/site/resources/plantuml/Simpleworklist__Domain_Class_Modell_Transient.png)

## Software Design
### Service Classes Controller
![Service_Classes_Controller](src/site/resources/plantuml/Simpleworklist__Service_Classes_Controller.png)
### Service Classes DB data
![Service_Classes_DB](src/site/resources/plantuml/Simpleworklist__Service_Classes_DB_data.png)
### Service Classes DB user
![Service_Classes_DB](src/site/resources/plantuml/Simpleworklist__Service_Classes_DB_user.png)
### Service Classes Meso
![Service_Classes_Meso](src/site/resources/plantuml/Simpleworklist__Service_Classes_Meso.png)
