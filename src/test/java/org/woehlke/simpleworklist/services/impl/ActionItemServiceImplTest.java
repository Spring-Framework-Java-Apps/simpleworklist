package org.woehlke.simpleworklist.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.services.ActionItemService;
import org.woehlke.simpleworklist.services.TestHelperService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class ActionItemServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionItemServiceImplTest.class);

    @Inject
    private ActionItemService actionItemService;

    @Inject
    private TestHelperService testHelperService;

    private void deleteAll(){
        testHelperService.deleteAllRegistrationProcess();
        testHelperService.deleteAllActionItem();
        testHelperService.deleteAllCategory();
        testHelperService.deleteUserAccount();
        testHelperService.deleteTimelineDay();
        testHelperService.deleteTimelineMonth();
        testHelperService.deleteTimelineYear();
    }

    @Test
    public void storeRootData() {
        ActionItem rootActionItem01 = new ActionItem();
        rootActionItem01.setCategory(null);
        rootActionItem01.setText("TEXT01");
        rootActionItem01.setTitle("TITLE01");
        ActionItem rootActionItem02 = new ActionItem();
        rootActionItem02.setCategory(null);
        rootActionItem02.setText("TEXT02");
        rootActionItem02.setTitle("TITLE02");
        Assert.assertNotNull(actionItemService.saveAndFlush(rootActionItem01).getId());
        Assert.assertNotNull(actionItemService.saveAndFlush(rootActionItem02).getId());
        Pageable pageRequest = new PageRequest(0, 10);
        List<ActionItem> list = actionItemService.findByRootCategory(pageRequest).getContent();
        boolean rootData01found = false;
        boolean rootData02found = false;
        for (ActionItem leaf : list) {
            if (leaf.getTitle().contentEquals("TITLE01")) {
                rootData01found = true;
            }
            if (leaf.getTitle().contentEquals("TITLE02")) {
                rootData02found = true;
            }
        }
        Assert.assertTrue(rootData01found);
        Assert.assertTrue(rootData02found);
        deleteAll();
    }



}
