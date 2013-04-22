package org.woehlke.simpleworklist.control;

import static org.hamcrest.Matchers.*;
import static org.woehlke.simpleworklist.entities.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.repository.CategoryRepository;
import org.woehlke.simpleworklist.repository.DataRepository;
import org.woehlke.simpleworklist.repository.UserAccountRepository;
import org.woehlke.simpleworklist.services.CategoryService;
import org.woehlke.simpleworklist.services.DataService;
import org.woehlke.simpleworklist.services.UserService;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class CategoryControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryControllerTest.class);

	@Inject
	protected WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Inject
	private CategoryRepository categoryNodeRepository;
	
	@Inject
	private DataRepository dataLeafRepository;

	@Inject
	private UserService userService;
	
	@Inject
	private DataService dataService;
	
	@Inject
	private CategoryService categoryService;
	
	private static String emails[]    = {"test01@test.de","test02@test.de","test03@test.de"};
	private static String passwords[] = {"test01pwd","test02pwd","test03pwd"};
	private static String fullnames[] = {"test01 Name","test02 Name","test03 Name"};
	
	private static UserAccount testUser[] = new UserAccount[emails.length];
	
	static {
		for(int i =0; i<testUser.length;i++){
			testUser[i] = new UserAccount();
			testUser[i].setUserEmail(emails[i]);
			testUser[i].setUserPassword(passwords[i]);
			testUser[i].setUserFullname(fullnames[i]);
		}
	}
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(wac).build();
		for(UserAccount u:testUser){
			UserAccount a = userService.findByUserEmail(u.getUserEmail());
			if(a==null){
				u=userService.saveAndFlush(u);
			}
		}
	}
	
    @After
    public void clearContext() {
        SecurityContextHolder.clearContext();
		dataService.deleteAll();
		categoryService.deleteAll();
		userService.deleteAll();
    }
	
    private void makeActiveUser(String username) {     
        UserDetails ud = userService.loadUserByUsername(username);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
    }
	
	@Test
	public void testHome() throws Exception {
		makeActiveUser(emails[0]);
		this.mockMvc.perform(
				get("/")).andDo(print())
				.andExpect(view().name(containsString("redirect:/category/0/page/1")));
	}
	
	@Test
	public void testCategoryZeroRedirect() throws Exception {
		makeActiveUser(emails[0]);
		this.mockMvc.perform(
				get("/category/0/")).andDo(print())
				.andExpect(view().name(containsString("redirect:/category/0/page/1")));		
	}
	
	@Test
	public void testCategoryZero() throws Exception {
		makeActiveUser(emails[0]);
		this.mockMvc.perform(
				get("/category/0/page/1")).andDo(print())
				.andExpect(view().name(containsString("category/show")))
				.andExpect(model().attributeExists("breadcrumb"))
				.andExpect(model().attributeExists("thisCategory"))
				.andExpect(model().attributeExists("dataList"))
				.andExpect(model().attributeExists("beginIndex"))
				.andExpect(model().attributeExists("endIndex"))
				.andExpect(model().attributeExists("currentIndex"));
	}
	
	@Test
	public void testFormNewCategoryNode() throws Exception {
		makeActiveUser(emails[0]);
		this.mockMvc.perform(
				get("/category/addchild/0")).andDo(print())
				.andExpect(model().attributeExists("breadcrumb"))
				.andExpect(model().attributeExists("thisCategory"))
				.andExpect(model().attribute("thisCategory", notNullValue()))
				.andExpect(model().attribute("thisCategory", instanceOf(Category.class)))
				.andExpect(model().attribute("thisCategory", hasProperty("id")))
				.andExpect(model().attribute("thisCategory", is(categoryNullObject())));
	}
	
	@Test
	public void testHelperCategoryCreateTree() throws Exception {
		makeActiveUser(emails[0]);
		this.mockMvc.perform(
				get("/test/helper/category/createTree")).andDo(print())
				.andExpect(view().name(containsString("redirect:/")));
		UserAccount user=userService.retrieveCurrentUser();
		List<Category> rootCategories = categoryNodeRepository.findByParentIsNullAndUserAccount(user);
		Assert.assertTrue(rootCategories.size()>0);
	}
	
	@Test
	public void testShowChildNodePage() throws Exception {
		makeActiveUser(emails[0]);
		logger.info("----------------------------------------------");
		logger.info("testShowChildNodePage");
		logger.info("----------------------------------------------");
		UserAccount user=userService.retrieveCurrentUser();
		logger.info(user.toString());
		logger.info("----------------------------------------------");
		Date nowDate = new Date();
		long now = nowDate.getTime();
		String name01 = "test01"+now;
		String name02 = "test02"+now;
		String name0101 = "test0101"+now;
		String name0102 = "test0102"+now;
		String name0201 = "test0201"+now;
		String name0202 = "test0202"+now;
		String name020201 = "test020201"+now;
		String name020202 = "test020202"+now;
		String name020203 = "test020203"+now;
		Category c01 = Category.newRootCategoryNodeFactory(user);
		c01.setName(name01);
		c01.setDescription("description01 for "+name01);
		c01=categoryNodeRepository.saveAndFlush(c01);
		Category c02 = Category.newRootCategoryNodeFactory(user);
		c02.setName(name02);
		c02.setDescription("description02 for "+name02);
		c02=categoryNodeRepository.saveAndFlush(c02);
		Category c0101 = Category.newCategoryNodeFactory(c01);
		c0101.setName(name0101);
		c0101.setDescription("description0101 for "+name0101);
		c0101=categoryNodeRepository.saveAndFlush(c0101);
		Category c0102 = Category.newCategoryNodeFactory(c01);
		c0102.setName(name0102);
		c0102.setDescription("description0102 for "+name0102);
		c0102=categoryNodeRepository.saveAndFlush(c0102);
		Category c0201 = Category.newCategoryNodeFactory(c02);
		c0201.setName(name0201);
		c0201.setDescription("description0201 for "+name0201);
		c0201=categoryNodeRepository.saveAndFlush(c0201);
		Category c0202 = Category.newCategoryNodeFactory(c02);
		c0202.setName(name0202);
		c0202.setDescription("description0202 for "+name0202);
		c0202=categoryNodeRepository.saveAndFlush(c0202);
		Category c020201 = Category.newCategoryNodeFactory(c0202);
		Category c020202 = Category.newCategoryNodeFactory(c0202);
		Category c020203 = Category.newCategoryNodeFactory(c0202);
		c020201.setName(name020201);
		c020202.setName(name020202);
		c020203.setName(name020203);
		c020201.setDescription("description for "+name020201);
		c020202.setDescription("description for "+name020202);
		c020203.setDescription("description for "+name020203);
		c020201=categoryNodeRepository.saveAndFlush(c020201);
		c020202=categoryNodeRepository.saveAndFlush(c020202);
		c020203=categoryNodeRepository.saveAndFlush(c020203);
		logger.info(c01.toString());
		logger.info(c02.toString());
		logger.info(c0101.toString());
		logger.info(c0102.toString());
		logger.info(c0201.toString());
		logger.info(c0202.toString());
		logger.info("----------------------------------------------");
		this.mockMvc.perform(
				get("/category/"+c01.getId()+"/page/1")).andDo(print())
				.andExpect(view().name(containsString("category/show")))
				.andExpect(model().attribute("thisCategory", c01))
				.andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
		logger.info("----------------------------------------------");
		this.mockMvc.perform(
				get("/category/"+c0202.getId()+"/page/1")).andDo(print())
				.andExpect(view().name(containsString("category/show")))
				.andExpect(model().attribute("thisCategory", c0202))
				.andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
		logger.info("----------------------------------------------");
		this.mockMvc.perform(
				get("/category/"+c020202.getId()+"/page/1")).andDo(print())
				.andExpect(view().name(containsString("category/show")))
				.andExpect(model().attribute("thisCategory", c020202))
				.andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
		logger.info("----------------------------------------------");
	}
	
	@Test
	public void rootCategoriesNonNullPrecondition() throws Exception {
		makeActiveUser(emails[0]);
		UserAccount user=userService.retrieveCurrentUser();
		List<Category> rootCategories = categoryNodeRepository.findByParentIsNullAndUserAccount(user);
		for(Category category : rootCategories){
		this.mockMvc.perform(
				get("/category/"+category.getId()+"/page/1")).andDo(print())
				.andExpect(model().attributeExists("breadcrumb"))
				.andExpect(model().attributeExists("thisCategory"))
				.andExpect(model().attributeExists("dataList"))
				.andExpect(view().name(containsString("category/show")))
				.andExpect(model().attribute("thisCategory", category))
				.andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
		}
	}
	
	@Test
	public void testEditDataLeafForm() throws Exception {
		makeActiveUser(emails[0]);
		List<Data> all = dataLeafRepository.findAll();
		for(Data dl :all){
			this.mockMvc.perform(
					get("/data/detail/"+dl.getId())).andDo(print())
					.andExpect(model().attributeExists("thisCategory"))
					.andExpect(model().attributeExists("breadcrumb"))
					.andExpect(model().attributeExists("command"))
					.andExpect(model().attribute("command", notNullValue()))
					.andExpect(model().attribute("command", instanceOf(Data.class)))
					.andExpect(model().attribute("command", hasProperty("id")))
					.andExpect(model().attribute("command", is(dataNotNullObject())));
		}
	}
	
	@Test
	public void testNewDataLeafForRootCategoryNode() throws Exception {
		makeActiveUser(emails[0]);
		this.mockMvc.perform(
				get("/data/addtocategory/0")).andDo(print())
				.andExpect(model().attributeExists("thisCategory"))
				.andExpect(model().attributeExists("breadcrumb"))
				.andExpect(model().attribute("thisCategory",notNullValue()))
				.andExpect(model().attribute("thisCategory",instanceOf(Category.class)))
				.andExpect(model().attribute("thisCategory", hasProperty("id")))
				.andExpect(model().attribute("thisCategory", is(categoryNullObject())));
	}
	
}
