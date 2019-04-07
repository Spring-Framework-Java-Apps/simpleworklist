package org.woehlke.simpleworklist.model.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.entities.Context;
import org.woehlke.simpleworklist.entities.entities.Task;
import org.woehlke.simpleworklist.entities.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.entities.repository.TaskRepository;
import org.woehlke.simpleworklist.model.services.TaskMoveService;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TaskMoveServiceImpl implements TaskMoveService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskMoveServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void deleteAllCompleted(Context context, UserAccount thisUser) {
        if(context == null) {
            context = thisUser.getDefaultContext();
        }
        if (thisUser.getId().longValue() == context.getUserAccount().getId().longValue()) {
            List<Task> taskList = taskRepository.findByTaskStateAndContextOrderByOrderIdTaskState(TaskState.COMPLETED, context);
            Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState.TRASHED, context);
            long maxOrderIdTaskState = (task == null) ? 0 : task.getOrderIdTaskState();
            for (Task mytask : taskList) {
                maxOrderIdTaskState++;
                mytask.setOrderIdTaskState(maxOrderIdTaskState);
                mytask.setTaskState(TaskState.TRASHED);
                taskRepository.save(mytask);
            }
        }
    }

    @Override
    public void emptyTrash(UserAccount userAccount, Context context) {
        List<Task> taskList = taskRepository.findByTaskStateAndContext(TaskState.TRASHED,context);
        for(Task task:taskList){
            taskRepository.delete(task);
        }
    }
}
