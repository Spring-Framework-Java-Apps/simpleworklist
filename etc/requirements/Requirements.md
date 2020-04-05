# Functional Requirements

## F001 Server Starts

## F002 Home Page rendered

## F003 Registration

## F004 Password Recovery

## F005 Login

## F006 Page after first successful Login

## F007 Add first new Task from Inbox

## F008 Add first new Project

## F009 Add another new Task from Inbox
INBOX -> add new task -> Form -> save -> INBOX ( -> OK )
-> add new task -> Form -> save -> Project (-> NOK )

BUG: after saving to db app should show Inbox, but shows ProjectView for RootProject

URLpaths:
http://localhost:5000/taskstate/inbox
http://localhost:5000/task/addtoproject/0
http://localhost:5000/project/0/?listTaskTime=MIN5&listTaskTime=MIN10&listTaskTime=MIN15&listTaskTime=MIN30&listTaskTime=MIN45&listTaskTime=HOUR1&listTaskTime=HOUR2&listTaskTime=HOUR3&listTaskTime=HOUR4&listTaskTime=HOUR6&listTaskTime=HOUR8&listTaskTime=MORE&listTaskTime=NONE&numberOfNewIncomingMessages=0&listTaskEnergy=LOW&listTaskEnergy=MEDIUM&listTaskEnergy=HIGH&listTaskEnergy=NONE&context=Work&refreshMessages=false





http://localhost:5000/taskstate/inbox
today
next
waiting
scheduled
someday
completed
trash
focus



