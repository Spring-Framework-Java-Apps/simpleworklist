package org.woehlke.simpleworklist.model.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.model.NewContextForm;
import org.woehlke.simpleworklist.model.entities.Context;
import org.woehlke.simpleworklist.model.entities.UserAccount;
import org.woehlke.simpleworklist.model.repository.ContextRepository;
import org.woehlke.simpleworklist.model.repository.ProjectRepository;
import org.woehlke.simpleworklist.model.repository.TaskRepository;
import org.woehlke.simpleworklist.model.services.ContextService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
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
        return contextRepository.findByUserAccount(user);
    }

    @Override
    public Context findByIdAndUserAccount(long newContextId, UserAccount userAccount) {
        return contextRepository.findByIdAndUserAccount(newContextId,userAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void createNewContext(NewContextForm newContext, UserAccount user) {
        Context context = new Context();
        context.setNameEn(newContext.getNameEn());
        context.setNameDe(newContext.getNameDe());
        context.setUserAccount(user);
        contextRepository.saveAndFlush(context);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void updateContext(NewContextForm editContext, UserAccount user, long contextId) {
        Context context = contextRepository.getOne(contextId);
        if(context.getUserAccount().getId().longValue() == user.getId().longValue()){
            context.setNameEn(editContext.getNameEn());
            context.setNameDe(editContext.getNameDe());
            contextRepository.saveAndFlush(context);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public boolean delete(Context context) {
        long contextId = context.getId();
        contextRepository.delete(context);
        return (!contextRepository.existsById(contextId));
    }

    @Override
    public boolean contextHasItems(Context context) {
        int numberOfTasks = taskRepository.findByContext(context).size();
        int numberOfProjects = projectRepository.findByContext(context).size();
        return ((numberOfTasks + numberOfProjects) > 0);
    }
}
