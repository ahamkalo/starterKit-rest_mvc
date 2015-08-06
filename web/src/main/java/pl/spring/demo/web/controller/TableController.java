package pl.spring.demo.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@Controller
public class TableController {
	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/table", method = RequestMethod.GET)
	public String bookList(Map<String, Object> params) {
		final List<BookTo> allBooks = bookService.findAllBooks();
		params.put("books", allBooks);
		return "bookTable";
	}

	@RequestMapping(value = "/table", method = RequestMethod.POST)
	public String deleteBook(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
		BookTo book = bookService.findBookById(id);
		redirectAttributes.addFlashAttribute("message",	"Książka o tytule \"" + book.getTitle() + "\" została usunięta.");

    	bookService.deleteBook(id);
		
		return "redirect:/confirmation";
	}
}
