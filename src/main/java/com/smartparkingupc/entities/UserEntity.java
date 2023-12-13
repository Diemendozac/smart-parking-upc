package com.smartparkingupc.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;
  private String password;
  private String name;
  @Column(name = "phone_number")
  private String phoneNumber;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name="confidence_circle",
          joinColumns=@JoinColumn(name="user_id"),
          inverseJoinColumns=@JoinColumn(name="confidence_circle_id")
  )
  private List<ConfidenceCircleUser> confidenceCircle;

  @ManyToMany
  @JoinTable(name="confidence_circle",
          joinColumns=@JoinColumn(name="confidence_circle_id"),
          inverseJoinColumns=@JoinColumn(name="user_id")
  )
  private List<ConfidenceCircleUser> confidenceCircleOf;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
