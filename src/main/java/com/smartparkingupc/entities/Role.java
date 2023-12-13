package com.smartparkingupc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Table(name = "Role")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Role implements Serializable{

	private static final long serialVersionUID = 5926468583005150707L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "name", nullable = false)
	private String name;

}
