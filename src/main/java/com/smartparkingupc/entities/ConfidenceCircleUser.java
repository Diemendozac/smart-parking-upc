package com.smartparkingupc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConfidenceCircleUser {

  @Id
  private Long id;
  private String email;
  private String name;
  @Column(name = "phone_number")
  private String phoneNumber;

}
