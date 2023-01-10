# Requirements

## Functional Requirements
* [Functional Requirements](REQUIREMENTS_FUNCTIONAL.md)

### Functional Requirements 001 - Start
![Functional Requirements 001 - Start](plantuml/Simpleworklist__Use_Cases__001__Start.png)
#### F001 Server Starts
#### F002 Home Page rendered
![Screen F002 Home Page rendered](screenshots/en/screen01.png)
#### F003 Registration
#### F004 Password Recovery
#### F005 Login
![Screen F005 Login](screenshots/en/screen01.png)
#### F006 Page after first successful Login
![Screen F006 Page after first successful Login](screenshots/en/screen02.png)
#### F007 Logout

### Functional Requirements 002 - Show TaskstateTab
![Functional Requirements 002 - Show TaskstateTab](plantuml/Simpleworklist__Use_Cases__002__Show_TaskstateTab.png)
#### F010 show /taskstate/inbox
![Screen F010 show /taskstate/inbox](screenshots/en/screen03.png)
#### F011 show /taskstate/today
![Screen F011 show /taskstate/today](screenshots/en/screen04.png)
#### F012 show /taskstate/next
![Screen F012 show /taskstate/next](screenshots/en/screen05.png)
#### F013 show /taskstate/waiting
![Screen F013 show /taskstate/waiting](screenshots/en/screen06.png)
#### F014 show /taskstate/scheduled
![Screen F014 show /taskstate/scheduled](screenshots/en/screen07.png)
#### F015 show /taskstate/someday
![Screen F015 show /taskstate/someday](screenshots/en/screen08.png)
#### F016 show /taskstate/focus
![Screen F016 show /taskstate/focus](screenshots/en/screen09.png)
#### F017 show /taskstate/completed
![Functional Requirements F017 show /taskstate/completed](screenshots/en/screen10.png)
#### F018 show /taskstate/trash
![Screen F018 show /taskstate/trash](screenshots/en/screen11.png)
#### F019 show /project/{projectid}
![Screen F006 Pe after first successful Loginag](screenshots/en/screen13.png)
#### F020 show /taskstate/all
![Screen F020 show /taskstate/all](screenshots/en/screen14.png)

### Functional Requirements 003 - TaskstateTab
![Functional Requirements 003 - TaskstateTab](plantuml/Simpleworklist__Use_Cases__003__TaskstateTab.png)
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
![Functional Requirements 004 - Project/Root](plantuml/Simpleworklist__Use_Cases__004__Project_Root.png)
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
![Functional Requirements 005 - Project/id](plantuml/Simpleworklist__Use_Cases__005__Project_id.png)
#### F080 Project/id: Add Task
![Screen F080 Project/id: Add Task](screenshots/en/screen22.png)
#### F081 Project/id: Add SubProject
#### F082 Project/id: Task Edit
![Screen F082 Project/id: Task Edit](screenshots/en/screen24.png)
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
![Functional Requirements 006 - Drag and Drop. Task move to TaskstateTab](plantuml/Simpleworklist__Use_Cases__006__Task_move_to_TaskstateTab.png)
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
![Functional Requirements 007 - Drag and Drop Move to Project](plantuml/Simpleworklist__Use_Cases__007__Move_to_Project.png)
#### F129 Drag and Drop: Task move to Project
#### F130 Drag and Drop: Project move to Project

### Functional Requirements 008 - Drag and Drop Move Task to Change Task-Order
![Functional Requirements 008 - Drag and Drop Move Task to Change Task-Order](plantuml/Simpleworklist__Use_Cases__008__Move_Task_to_Change_Task_Order.png)
#### F131 Drag and Drop: Move Task to Change Task-Order in TaskstateTab
#### F132 Drag and Drop: Move Task to Change Task-Order in Project/Root
#### F133 Drag and Drop: Move Task to Change Task-Order in Project/id

### Functional Requirements 009 - Pages
#### F140 Pages Information
![Screen F140 Pages Information](screenshots/en/screen15.png)

### Functional Requirements 010 - Search
![Functional Requirements 010 - Search](plantuml/Simpleworklist__Use_Cases__009__Search.png)
#### F200 Search Request

## Functional Requirements - User
### Functional Requirements 001 - Start
![Functional Requirements 001 - Start](plantuml/Simpleworklist__Use_Cases__001__Start.png)
#### F003 Registration
#### F004 Password Recovery
#### F005 Login
#### F007 Logout

### Functional Requirements 011 - User Profile
![Functional Requirements 010 - User Profile](plantuml/Simpleworklist__Use_Cases__010__User_Profile.png)
#### F200 List of other Users and New Messages received from them
![Screen F200 List of other Users and New Messages received from them](screenshots/en/screen19.png)
#### F201 Chat Messages shared with one other User
![Screen F201 Chat Messages shared with one other User](screenshots/en/screen30.png)
#### F202 Send New Chat Message to one other User
![Screen F202 Send New Chat Message to one other User](screenshots/en/screen28.png)
#### F220 Menu Selfservice: Change Username
#### F221 Menu Selfservice: Change Password
#### F222 Menu Selfservice: Change Contexts
#### F223 Menu Selfservice: Set Default Language
#### F224 Menu Selfservice: Create Test Data

### Functional Requirements 012 - On every Page behind Login
![Functional Requirements 012 - User Profile](plantuml/Simpleworklist__Use_Cases__010__User_Profile.png)
#### F250 Number of new incoming Messages from other Users
