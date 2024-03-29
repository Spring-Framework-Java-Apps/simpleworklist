package org.woehlke.java.simpleworklist.domain.db.data.context;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.project.ProjectRepository;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskRepository;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by tw on 13.03.16.
 */
@Log
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
        if(userAccount.getUuid()==null){
          userAccount.setUuid(UUID.randomUUID());
        }
        if(newContextId == 0){
            newContextId =  userAccount.getDefaultContext().getId();
        }
        return contextRepository.findByIdAndUserAccount(newContextId,userAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Context createNewContext(NewContextForm newContext, UserAccount user) {
        log.info("createNewContext");
        if(user.getUuid()==null){
          user.setUuid(UUID.randomUUID());
        }
        Context context = new Context();
        context.setUuid(UUID.randomUUID());
        context.setNameEn(newContext.getNameEn());
        context.setNameDe(newContext.getNameDe());
        context.setUserAccount(user);
        return contextRepository.saveAndFlush(context);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Context updateContext(Context context) {
      log.info("updateContext");
      if(context.getUuid()==null){
        context.setUuid(UUID.randomUUID());
      }
      return contextRepository.saveAndFlush(context);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public boolean delete(Context context) {
        log.info("delete");
        if(context.getUuid()==null){
          context.setUuid(UUID.randomUUID());
        }
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

    @Override
    public Optional<Context> getContextFor(UserSessionBean userSession) {
        Long id = userSession.getLastContextId();
        return contextRepository.findById(id);
    }
}
