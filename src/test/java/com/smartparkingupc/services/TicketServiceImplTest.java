package com.smartparkingupc.services;

import com.smartparkingupc.entities.Ticket;
import com.smartparkingupc.repositories.TicketRepository;
import com.smartparkingupc.services.impl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

	@Mock
	private TicketRepository ticketRepository;

	@InjectMocks
	private TicketServiceImpl ticketService;

	private Ticket ticket;

	@BeforeEach
	void setUp() {
		ticket = Ticket.builder().userOwnerEmail("test@example.com").build();
	}

	@Test
	void testFindUserRelatedTickets() {
		when(ticketRepository.findAllByUserOwnerEmail("test@example.com")).thenReturn(Collections.singletonList(ticket));

		List<Ticket> tickets = ticketService.findUserRelatedTickets("test@example.com");

		assertNotNull(tickets);
		assertEquals(1, tickets.size());
		verify(ticketRepository, times(1)).findAllByUserOwnerEmail("test@example.com");
	}

	@Test
	void testSaveTicket() {
		ticketService.saveTicket(ticket);

		verify(ticketRepository, times(1)).save(ticket);
	}
}
