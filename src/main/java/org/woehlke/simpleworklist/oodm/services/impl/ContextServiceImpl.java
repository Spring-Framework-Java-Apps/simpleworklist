package org.woehlke.simpleworklist.oodm.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.model.beans.NewContextForm;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.oodm.repository.ContextRepository;
import org.woehlke.simpleworklist.oodm.repository.ProjectRepository;
import org.woehlke.simpleworklist.oodm.repository.TaskRepository;
import org.woehlke.simpleworklist.oodm.services.ContextService;

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
        if(newContextId == 0){
            newContextId =  userAccount.getDefaultContext().getId();
        }
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
    public void updateContext(Context context) {
        contextRepository.saveAndFlush(context);
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
        long numberOfTasks = taskRepository.findByContext(context).size();
        int numberOfProjects = projectRepository.findByContext(context).size();
        return ((numberOfTasks + numberOfProjects) > 0);
    }
}
