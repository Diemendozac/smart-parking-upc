package com.smartparkingupc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "UserRole")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserRole implements Serializable{

	private static final long serialVersionUID = 5926468583005150707L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = true, updatable = true)
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name = "role_id", insertable = true, updatable = true)
	private Role role;

}
