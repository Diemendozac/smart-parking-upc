package com.smartparkingupc.services;

import com.smartparkingupc.entities.Ticket;

import java.util.List;

public interface ITicketService {

  List<Ticket> findUserRelatedTickets(String userOwnerEmail);
  void saveTicket(Ticket ticket);
}
