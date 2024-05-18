package com.app.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.persistence.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    
    Optional<UserEntity> findUserEntityByUsername(String username);
}
