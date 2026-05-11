package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.service.ItemService;

@SpringBootTest
class ShareItTests {

	@Mock
	private ItemService itemService;

	@Test
	void contextLoads() {
	}

}
