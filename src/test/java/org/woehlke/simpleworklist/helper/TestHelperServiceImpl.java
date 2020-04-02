package org.woehlke.simpleworklist.helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.project.ProjectRepository;
import org.woehlke.simpleworklist.user.register.UserRegistrationRepository;
import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecovery;
import org.woehlke.simpleworklist.user.register.UserRegistration;
import org.woehlke.simpleworklist.helper.TestHelperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.task.TaskRepository;
import org.woehlke.simpleworklist.user.account.UserAccountRepository;
import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecoveryRepository;

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
