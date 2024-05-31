package com.example.demo.repositories;

import com.example.demo.model.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);

    @Query("select (count(r) > 0) from Role r where r.role = ?1")
    boolean existsByRole(String role);
}
