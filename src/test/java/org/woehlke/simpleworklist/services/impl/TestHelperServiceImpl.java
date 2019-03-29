package org.woehlke.simpleworklist.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.enumerations.RegistrationProcessType;
import org.woehlke.simpleworklist.repository.*;
import org.woehlke.simpleworklist.services.TestHelperService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TestHelperServiceImpl implements TestHelperService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RegistrationProcessRepository registrationProcessRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllRegistrationProcess() {
        registrationProcessRepository.deleteAll();

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllActionItem() {
        taskRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllCategory() {
        List<Project> roots = projectRepository.findByParentIsNull();
        for(Project root:roots){
            remove(root);
        }
    }

    private void remove(Project root){
        for(Project child:root.getChildren()){
            remove(child);
        }
        projectRepository.delete(root);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteUserAccount() {
        userAccountRepository.deleteAll();
    }

    @Override
    public int getNumberOfAllRegistrations() {
        return registrationProcessRepository.findAll().size();
    }

    @Override
    public RegistrationProcess findByEmailRegistration(String email) {
        return registrationProcessRepository.findByEmailAndRegistrationProcessType(email,RegistrationProcessType.REGISTRATION);
    }

    @Override
    public RegistrationProcess findByEmailPasswordRecovery(String email) {
        return registrationProcessRepository.findByEmailAndRegistrationProcessType(email, RegistrationProcessType.PASSWORD_RECOVERY);
    }
}
