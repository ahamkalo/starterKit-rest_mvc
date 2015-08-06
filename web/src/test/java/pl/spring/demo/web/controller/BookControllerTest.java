package pl.spring.demo.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class BookControllerTest {

	@Autowired
	private BookService bookService;
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		Mockito.reset(bookService);

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/templates/");
		viewResolver.setSuffix(".html");

		mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).setViewResolvers(viewResolver).build();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testBooksPage() throws Exception {
		// given when
		final BookTo bookTo1 = new BookTo(1L, "title1", "Author1");
		final BookTo bookTo2 = new BookTo(2L, "title2", "Author2");

		Mockito.when(bookService.findAllBooks()).thenReturn(Arrays.asList(bookTo1, bookTo2));
		ResultActions resultActions = mockMvc.perform(get("/books"));
		// then
		Mockito.verify(bookService).findAllBooks();

		resultActions.andExpect(view().name("bookList"))
				.andExpect(model().attribute("books", new ArgumentMatcher<Object>() {
					@SuppressWarnings("unchecked")
					@Override
					public boolean matches(Object argument) {
						return "title1".equals(((List<BookTo>) argument).get(0).getTitle())
								&& "Author2".equals(((List<BookTo>) argument).get(1).getAuthors());
					}
				}));

	}

}
