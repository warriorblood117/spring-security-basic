package com.app.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.persistence.entities.RolesEntity;

@Repository
public interface RoleRepository extends CrudRepository<RolesEntity,Long>{

    List<RolesEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
    
}
