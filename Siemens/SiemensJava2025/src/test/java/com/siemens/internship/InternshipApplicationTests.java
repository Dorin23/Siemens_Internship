package com.siemens.internship;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InternshipApplicationTests {
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemService itemService;

	//Initializare Spring context
	@Test
	void contextLoads() {
		assertNotNull(itemRepository);
		assertNotNull(itemService);
	}
	//Salvare item valid
    @Test
	void testSaveItem(){
		Item item = new Item();
		item.setName("New Test Item");
		item.setDescription("Test Description");
		item.setStatus("NEW");
		item.setEmail("test@example.com");

		Item saved = itemService.save(item);

		assertNotNull(saved.getId());
		assertEquals(saved.getName(), "New Test Item");
		assertEquals(saved.getEmail(), "test@example.com");
	}

	//Email duplicat
	@Test
	void testSaveItemWithDuplicateEmail() {
		//Presupunem ca emailul este unic în entitate
		Item item1 = new Item(null, "Item 1", "Desc", "NEW", "duplicate@example.com");
		Item item2 = new Item(null, "Item 2", "Desc", "NEW", "duplicate@example.com");

		itemService.save(item1);

		try {
			itemService.save(item2);
			fail("Ar fi trebuit sa arunce o exceptie pentru email duplicat!");
		} catch (Exception e) {
			System.out.println("Excepție: " + e.getMessage());
			assertTrue(e.getMessage().contains("could not execute statement") || e.getMessage().contains("ConstraintViolation"));
		}
	}
	//Gasire toate itemele
	@Test
	void testFindAllItems() {
		itemService.save(new Item(null, "Item A", "A", "NEW", "a@example.com"));
		itemService.save(new Item(null, "Item B", "B", "NEW", "b@example.com"));

		List<Item> allItems = itemService.findAll();

		assertFalse(allItems.isEmpty());
		assertTrue(allItems.size() >= 2);
	}
	//Testam stergerea unui item dupa ID
	@Test
	void testDeleteItemById() {
		Item item = new Item(null, "To Delete", "Desc", "NEW", "delete@example.com");
		Item saved = itemService.save(item);

		itemService.deleteById(saved.getId());

		Optional<Item> found = itemService.findById(saved.getId());
		assertTrue(found.isEmpty());
	}
	//Testam procesarea asincrona a itemelor
	@Test
	void testProcessItemsAsync() {
		itemRepository.deleteAll();
		// Adaugam 3 iteme noi
		itemService.save(new Item(null, "Item1", "Desc", "NEW", "proc1@example.com"));
		itemService.save(new Item(null, "Item2", "Desc", "NEW", "proc2@example.com"));
		itemService.save(new Item(null, "Item3", "Desc", "NEW", "proc3@example.com"));

		// Procesam asincron
		List<Item> processed = itemService.processItemsAsync();

		// Verificam că toate au fost procesate
		assertEquals(3, processed.size());
		for (Item item : processed) {
			assertEquals("PROCESSED", item.getStatus());
		}
	}
	@Test
	void testFindById_nonExistingItem_shouldReturnEmpty() {
		Optional<Item> found = itemService.findById(999999L); // ID care sigur nu exista
		assertTrue(found.isEmpty());
	}
    //Testam ca stergerea unui ID inexistent nu arunca exceptie
	@Test
	void testDeleteNonExistingItem_shouldNotCrash() {
		try {
			itemService.deleteById(999999L); // ID inexistent
		} catch (Exception e) {
			fail("Stergerea unui item inexistent nu ar trebui sa arunce exceptii.");
		}
	}
	//Testam ca un item salvat poate fi gasit după ID
	@Test
	void testFindById_existingItem_shouldReturnItem() {
		Item item = new Item(null, "FindMe", "Desc", "NEW", "findme@example.com");
		Item saved = itemService.save(item);

		Optional<Item> found = itemService.findById(saved.getId());

		assertTrue(found.isPresent());
		assertEquals("FindMe", found.get().getName());
	}
	//Testam performanta metodei processItemsAsync()
	@Test
	void testProcessItemsPerformance() {
		itemRepository.deleteAll();
		int itemCount = 1000;

		//Adaugam 1000 de iteme cu status NEW
		for (int i = 0; i < itemCount; i++) {
			Item item = new Item(null, "Item" + i, "Desc", "NEW", "perf" + i + "@example.com");
			itemService.save(item);
		}

		//Masurăm timpul de execuție
		long start = System.currentTimeMillis();

		List<Item> processed = itemService.processItemsAsync();

		long end = System.currentTimeMillis();
		long duration = end - start;

		//Afișam timpul în consola
		System.out.println("Procesarea " + itemCount + " iteme a durat " + duration + " ms");

		//Verificam ca toate au fost procesate
		assertEquals(itemCount, processed.size());

		//Putem pune un prag - max 12 sec
		assertTrue(duration < 12000, "Procesarea a fost prea lentă!");
	}
	//Verificam actualizarea unui item existent
	@Test
	void testUpdateExistingItem() {
		Item item = new Item(null, "Original", "Old Desc", "NEW", "update@example.com");
		Item saved = itemService.save(item);

		saved.setDescription("Updated Desc");
		saved.setStatus("PROCESSED");
		Item updated = itemService.save(saved);

		assertEquals("Updated Desc", updated.getDescription());
		assertEquals("PROCESSED", updated.getStatus());
	}


}
