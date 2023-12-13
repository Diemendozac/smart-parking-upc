package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

  Role findByName(String name);
}
