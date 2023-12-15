package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.http.request.UserCredential;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  @Query("SELECT u.email, u.password FROM UserEntity u where u.email = :email")
  Optional<UserCredential> findUserEntityByEmail(String email);

}
