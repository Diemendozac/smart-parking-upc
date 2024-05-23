package com.smartparkingupc.services.impl;

import com.smartparkingupc.entities.Ticket;
import com.smartparkingupc.repositories.TicketRepository;
import com.smartparkingupc.services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements ITicketService {

  @Autowired private TicketRepository ticketRepository;

  @Override
  public List<Ticket> findUserRelatedTickets(String userOwnerEmail) {
    return ticketRepository.findAllByUserOwnerEmail(userOwnerEmail);
  }

  @Override
  public void saveTicket(Ticket ticket) {
    ticketRepository.save(ticket);
  }
}
