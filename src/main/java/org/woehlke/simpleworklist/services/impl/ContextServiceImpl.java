package org.woehlke.simpleworklist.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.NewContextFormBean;
import org.woehlke.simpleworklist.repository.ContextRepository;
import org.woehlke.simpleworklist.repository.ProjectRepository;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.ContextService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ContextServiceImpl implements ContextService {

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

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
    public void createNewContext(NewContextFormBean newContext, UserAccount user) {
        Context context = new Context();
        context.setNameEn(newContext.getNameEn());
        context.setNameDe(newContext.getNameDe());
        context.setUserAccount(user);
        contextRepository.saveAndFlush(context);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void updateContext(NewContextFormBean editContext, UserAccount user, long contextId) {
        Context context = contextRepository.findOne(contextId);
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
        Context contextFound = contextRepository.findOne(contextId);
        return (contextFound == null);
    }

    @Override
    public boolean contextHasItems(Context context) {
        int numberOfTasks = taskRepository.findByContext(context).size();
        int numberOfProjects = projectRepository.findByContext(context).size();
        return ((numberOfTasks + numberOfProjects) > 0);
    }
}
