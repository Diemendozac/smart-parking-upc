package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.http.request.UserCredential;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);
  Optional<UserCredential> findUserEntityByEmail(String email);



}
