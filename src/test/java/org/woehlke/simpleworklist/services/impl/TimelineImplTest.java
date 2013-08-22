package org.woehlke.simpleworklist.services.impl;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.TimelineDay;
import org.woehlke.simpleworklist.services.TimelineService;


public class TimelineImplTest extends AbstractTest {

    @Autowired
    private TimelineService todayFactory;

    @Test
    public void getTodayFactoryTest() {
        deleteAll();
        TimelineDay timelineDay = todayFactory.getTodayFactory();
        Assert.assertNotNull(timelineDay);
        Assert.assertNotNull(timelineDay.getMonth());
        Assert.assertNotNull(timelineDay.getMonth().getYear());
        Date now = new Date();
        Assert.assertEquals(timelineDay.getDayOfMonth(), now.getDay());
        Assert.assertEquals(timelineDay.getMonth().getMonthOfYear(), now.getMonth());
        Assert.assertEquals(timelineDay.getMonth().getYear().getYear(), now.getYear());
        timelineDay = todayFactory.getTodayFactory();
        Assert.assertNotNull(timelineDay);
        Assert.assertNotNull(timelineDay.getMonth());
        Assert.assertNotNull(timelineDay.getMonth().getYear());
        Assert.assertEquals(timelineDay.getDayOfMonth(), now.getDay());
        Assert.assertEquals(timelineDay.getMonth().getMonthOfYear(), now.getMonth());
        Assert.assertEquals(timelineDay.getMonth().getYear().getYear(), now.getYear());
        deleteAll();
    }
}
