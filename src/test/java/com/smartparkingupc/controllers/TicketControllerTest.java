package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.Ticket;
import com.smartparkingupc.services.ITicketService;
import com.smartparkingupc.security.JWTTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TicketControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ITicketService ticketService;

	@MockBean
	private JWTTokenUtil jwtTokenUtil;

	private Ticket ticket1;
	private Ticket ticket2;

	@BeforeEach
	public void setUp() {
		ticket1 = Ticket.builder()
						.userOwnerEmail("user1@example.com")
						.watchmanSelectedUser("watchman1@example.com")
						.vehiclePlate("ABC123")
						.createdAt(LocalDateTime.now())
						.isGettingIn(true)
						.build();

		ticket2 = Ticket.builder()
						.userOwnerEmail("user2@example.com")
						.watchmanSelectedUser("watchman2@example.com")
						.vehiclePlate("XYZ789")
						.createdAt(LocalDateTime.now().minusDays(1))
						.isGettingIn(false)
						.build();
	}

	@Test
	public void testFindAllByUserOwnerEmail_Success() throws Exception {
		List<Ticket> tickets = Arrays.asList(ticket1, ticket2);
		when(ticketService.findUserRelatedTickets("user1@example.com")).thenReturn(tickets);

		mockMvc.perform(get("/ticket/find-all")
										.requestAttr("LoggedInUser", "user1@example.com"))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].userOwnerEmail").value("user1@example.com"))
						.andExpect(jsonPath("$[0].vehiclePlate").value("ABC123"))
						.andExpect(jsonPath("$[1].userOwnerEmail").value("user2@example.com"))
						.andExpect(jsonPath("$[1].vehiclePlate").value("XYZ789"));
	}

	@Test
	public void testFindAllByUserOwnerEmail_NoTickets() throws Exception {
		when(ticketService.findUserRelatedTickets("user1@example.com")).thenReturn(List.of());

		mockMvc.perform(get("/ticket/find-all")
										.requestAttr("LoggedInUser", "user1@example.com"))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$").isEmpty());
	}
}
