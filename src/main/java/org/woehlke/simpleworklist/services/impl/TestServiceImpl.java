package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.enumerations.FocusType;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.repository.ProjectRepository;
import org.woehlke.simpleworklist.services.TestService;

import javax.inject.Inject;
import java.util.Date;


@Service
public class TestServiceImpl implements TestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private TaskRepository taskRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void createTestCategoryTreeForUserAccount(UserAccount userAccount) {
        Assert.notNull(userAccount);
        LOGGER.info("----------------------------------------------");
        LOGGER.info("createTestCategoryTreeForUserAccount");
        LOGGER.info("----------------------------------------------");
        Date nowDate = new Date();
        long now = nowDate.getTime();
        String name01 = "test01_" + now;
        String name02 = "test02_" + now;
        String name0101 = "test0101_" + now;
        String name0102 = "test0102_" + now;
        String name0201 = "test0201_" + now;
        String name0202 = "test0202_" + now;
        String name020201 = "test020201_" + now;
        String name020202 = "test020202_" + now;
        String name020203 = "test020203_" + now;
        String name02020301 = "test02020301_" + now;
        String name02020302 = "test02020302_" + now;
        String name02020303 = "test02020303_" + now;
        Project c01 = Project.newRootProjectFactory(userAccount);
        Project c02 = Project.newRootProjectFactory(userAccount);
        c01.setName(name01);
        c02.setName(name02);
        c01.setDescription("description01 for " + name01);
        c02.setDescription("description02 for " + name02);
        c01 = projectRepository.saveAndFlush(c01);
        c02 = projectRepository.saveAndFlush(c02);
        Project c0101 = Project.newProjectFactory(c01);
        Project c0102 = Project.newProjectFactory(c01);
        Project c0201 = Project.newProjectFactory(c02);
        Project c0202 = Project.newProjectFactory(c02);
        c0101.setName(name0101);
        c0102.setName(name0102);
        c0201.setName(name0201);
        c0202.setName(name0202);
        c01.getChildren().add(c0101);
        c01.getChildren().add(c0102);
        c02.getChildren().add(c0201);
        c02.getChildren().add(c0202);
        c0101.setDescription("description0101 for " + name0101);
        c0102.setDescription("description0102 for " + name0102);
        c0201.setDescription("description0201 for " + name0201);
        c0202.setDescription("description0202 for " + name0202);
        c0101 = projectRepository.saveAndFlush(c0101);
        c0102 = projectRepository.saveAndFlush(c0102);
        c0201 = projectRepository.saveAndFlush(c0201);
        c0202 = projectRepository.saveAndFlush(c0202);
        Project c020201 = Project.newProjectFactory(c0202);
        Project c020202 = Project.newProjectFactory(c0202);
        Project c020203 = Project.newProjectFactory(c0202);
        c020201.setName(name020201);
        c020202.setName(name020202);
        c020203.setName(name020203);
        c0202.getChildren().add(c020201);
        c0202.getChildren().add(c020202);
        c0202.getChildren().add(c020203);
        c020201.setDescription("description for " + name020201);
        c020202.setDescription("description for " + name020202);
        c020203.setDescription("description for " + name020203);
        c020201 = projectRepository.saveAndFlush(c020201);
        c020202 = projectRepository.saveAndFlush(c020202);
        c020203 = projectRepository.saveAndFlush(c020203);
        Project c02020301 = Project.newProjectFactory(c020203);
        Project c02020302 = Project.newProjectFactory(c020203);
        Project c02020303 = Project.newProjectFactory(c020203);
        c02020301.setName(name02020301);
        c02020302.setName(name02020302);
        c02020303.setName(name02020303);
        c020203.getChildren().add(c02020301);
        c020203.getChildren().add(c02020302);
        c020203.getChildren().add(c02020303);
        c02020301.setDescription("description for " + name02020301);
        c02020302.setDescription("description for " + name02020302);
        c02020303.setDescription("description for " + name02020303);
        c02020301 = projectRepository.saveAndFlush(c02020301);
        c02020302 = projectRepository.saveAndFlush(c02020302);
        c02020303 = projectRepository.saveAndFlush(c02020303);
        for (int i = 10; i < 111; i++) {
            String title = "t_" + i;
            String text = "d_" + i;
            Task d = new Task();
            d.setText(text);
            d.setTitle(title);
            d.setProject(c02020303);
            d.setStatus(TaskState.NEW);
            d.setCreatedTimestamp(new Date());
            d.setUserAccount(userAccount);
            d.setFocusType(FocusType.INBOX);
            taskRepository.saveAndFlush(d);
        }
        /* without Project for Main INBOX */
        for (int i = 10; i <30; i++) {
            String title = "title_" + i;
            String text = "desc_" + i;
            Task d = new Task();
            d.setText(text);
            d.setTitle(title);
            d.setStatus(TaskState.NEW);
            d.setCreatedTimestamp(new Date());
            d.setUserAccount(userAccount);
            d.setFocusType(FocusType.INBOX);
            taskRepository.saveAndFlush(d);
        }
    }
}
