package org.woehlke.simpleworklist.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.woehlke.simpleworklist.entities.Data;
import org.woehlke.simpleworklist.services.DataService;
import org.woehlke.simpleworklist.services.TestHelperService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class DataServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(DataServiceImplTest.class);

    @Inject
    private DataService dataService;

    @Inject
    private TestHelperService testHelperService;

    @Test
    public void storeRootData() {
        testHelperService.deleteAll();
        Data rootData01 = new Data();
        rootData01.setCategory(null);
        rootData01.setText("TEXT01");
        rootData01.setTitle("TITLE01");
        Data rootData02 = new Data();
        rootData02.setCategory(null);
        rootData02.setText("TEXT02");
        rootData02.setTitle("TITLE02");
        Assert.assertNotNull(dataService.saveAndFlush(rootData01).getId());
        Assert.assertNotNull(dataService.saveAndFlush(rootData02).getId());
        Pageable pageRequest = new PageRequest(0, 10);
        List<Data> list = dataService.findByRootCategory(pageRequest).getContent();
        boolean rootData01found = false;
        boolean rootData02found = false;
        for (Data leaf : list) {
            if (leaf.getTitle().contentEquals("TITLE01")) {
                rootData01found = true;
            }
            if (leaf.getTitle().contentEquals("TITLE02")) {
                rootData02found = true;
            }
        }
        Assert.assertTrue(rootData01found);
        Assert.assertTrue(rootData02found);
        testHelperService.deleteAll();
    }

}
