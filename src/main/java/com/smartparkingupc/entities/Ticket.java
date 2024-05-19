package com.smartparkingupc.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document( collection = "tickets")
public class Ticket{

  private String userOwnerEmail;
  private String watchmanSelectedUser;
  private String vehiclePlate;
  private LocalDateTime createdAt;
  private Boolean isGettingIn;

}

