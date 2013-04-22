package org.woehlke.simpleworklist.services.impl;

import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.woehlke.simpleworklist.entities.TimelineDay;
import org.woehlke.simpleworklist.services.TimelineService;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class TimelineImplTest {
	
	@Autowired
	private TimelineService todayFactory;
	
	@Before
	public void setup(){
		todayFactory.deleteAll();
	}
	
	@After
	public void cleanup(){
		todayFactory.deleteAll();
	}
	
	@Test
	public void getTodayFactoryTest(){
		Date now = new Date();
		TimelineDay timelineDay = todayFactory.getTodayFactory();
		Assert.assertNotNull(timelineDay);
		Assert.assertNotNull(timelineDay.getMonth());
		Assert.assertNotNull(timelineDay.getMonth().getYear());
		Assert.assertEquals(timelineDay.getDayOfMonth(), now.getDay());
		Assert.assertEquals(timelineDay.getMonth().getMonthOfYear(),now.getMonth());
		Assert.assertEquals(timelineDay.getMonth().getYear().getYear(),now.getYear());
		timelineDay = todayFactory.getTodayFactory();
		Assert.assertNotNull(timelineDay);
		Assert.assertNotNull(timelineDay.getMonth());
		Assert.assertNotNull(timelineDay.getMonth().getYear());
		Assert.assertEquals(timelineDay.getDayOfMonth(), now.getDay());
		Assert.assertEquals(timelineDay.getMonth().getMonthOfYear(),now.getMonth());
		Assert.assertEquals(timelineDay.getMonth().getYear().getYear(),now.getYear());
	}
}
