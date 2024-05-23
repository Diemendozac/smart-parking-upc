package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {

  List<Ticket> findAllByUserOwnerEmail(String userOwnerEmail);
}
