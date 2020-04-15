package org.woehlke.simpleworklist.context.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.context.NewContextForm;
import org.woehlke.simpleworklist.user.account.UserAccount;
import org.woehlke.simpleworklist.project.services.ProjectRepository;
import org.woehlke.simpleworklist.task.services.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ContextServiceImpl implements ContextService {

    private final ContextRepository contextRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ContextServiceImpl(ContextRepository contextRepository, TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.contextRepository = contextRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Context> getAllForUser(UserAccount user) {
        log.info("getAllForUser");
        return contextRepository.findByUserAccount(user);
    }

    @Override
    public Context findByIdAndUserAccount(long newContextId, UserAccount userAccount) {
        log.info("findByIdAndUserAccount");
        if(newContextId == 0){
            newContextId =  userAccount.getDefaultContext().getId();
        }
        return contextRepository.findByIdAndUserAccount(newContextId,userAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Context createNewContext(NewContextForm newContext, UserAccount user) {
        log.info("createNewContext");
        Context context = new Context();
        context.setNameEn(newContext.getNameEn());
        context.setNameDe(newContext.getNameDe());
        context.setUserAccount(user);
        return contextRepository.saveAndFlush(context);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Context updateContext(Context context) {
        log.info("updateContext");
        return contextRepository.saveAndFlush(context);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public boolean delete(Context context) {
        log.info("delete");
        long contextId = context.getId();
        contextRepository.delete(context);
        return (!contextRepository.existsById(contextId));
    }

    @Override
    public boolean contextHasItems(Context context) {
        log.info("contextHasItems");
        long numberOfTasks = taskRepository.findByContext(context).size();
        int numberOfProjects = projectRepository.findByContext(context).size();
        return ((numberOfTasks + numberOfProjects) > 0);
    }
}
