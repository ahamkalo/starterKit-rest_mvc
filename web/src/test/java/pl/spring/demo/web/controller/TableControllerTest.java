package pl.spring.demo.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class TableControllerTest {
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

		mockMvc = MockMvcBuilders.standaloneSetup(new TableController()).setViewResolvers(viewResolver).build();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testGetTablePage() throws Exception {
		// given when
		final BookTo bookTo1 = new BookTo(1L, "title1", "Author1");
		final BookTo bookTo2 = new BookTo(2L, "title2", "Author2");

		Mockito.when(bookService.findAllBooks()).thenReturn(Arrays.asList(bookTo1, bookTo2));
		ResultActions resultActions = mockMvc.perform(get("/table"));
		// then
		Mockito.verify(bookService).findAllBooks();

		resultActions.andExpect(view().name("bookTable"))
				.andExpect(model().attribute("books", new ArgumentMatcher<Object>() {
					@SuppressWarnings("unchecked")
					@Override
					public boolean matches(Object argument) {
						return "title1".equals(((List<BookTo>) argument).get(0).getTitle())
								&& "Author2".equals(((List<BookTo>) argument).get(1).getAuthors());
					}
				}));

	}
	@Test
	public void testDeleteButtonClick() throws Exception {
		// given when
		String message = "Książka o tytule \"title1\" została usunięta.";
		final BookTo bookTo = new BookTo(1L, "title1", "Author1");
		Mockito.when(bookService.findBookById(Mockito.anyLong())).thenReturn(bookTo);
		
		ResultActions resultActions = mockMvc.perform(post("/table?id=1"));
		// then
		
		Mockito.verify(bookService).findBookById(1L);
		Mockito.verify(bookService).deleteBook(Mockito.anyLong());
		resultActions.andExpect(MockMvcResultMatchers.redirectedUrl("/confirmation")).andExpect(MockMvcResultMatchers.flash().attribute("message", message));
	}

}