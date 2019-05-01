package org.woehlke.simpleworklist.model.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.UserPasswordRecovery;
import org.woehlke.simpleworklist.oodm.entities.UserRegistration;
import org.woehlke.simpleworklist.oodm.repository.*;
import org.woehlke.simpleworklist.model.services.TestHelperService;

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
    private UserRegistrationRepository userRegistrationRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserPasswordRecoveryRepository userPasswordRecoveryRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllRegistrationProcess() {
        userRegistrationRepository.deleteAll();

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
        return userRegistrationRepository.findAll().size();
    }

    @Override
    public UserRegistration findByEmailRegistration(String email) {
        return userRegistrationRepository.findByEmail(email);
    }

    @Override
    public UserPasswordRecovery findByEmailPasswordRecovery(String email) {
        return userPasswordRecoveryRepository.findByEmail(email);
    }
}
