package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.Ticket;
import com.smartparkingupc.services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

  @Autowired
  private ITicketService ticketService;
  @GetMapping("/find-all")
  public List<Ticket> findAllByUserOwnerEmail(@RequestAttribute("LoggedInUser") String email) {
    return ticketService.findUserRelatedTickets(email);

  }

}
