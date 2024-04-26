package com.example.demo.repositories;

import com.example.demo.model.entity.DAOUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<DAOUser, Long> {

    DAOUser findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("select d from DAOUser d where d.email = ?1")
    Optional<DAOUser> findByEmail(String email);

    @Query("select (count(d) > 0) from DAOUser d where d.email = ?1")
    boolean existsByEmailIs(String email);

    @Query("select (count(d) > 0) from DAOUser d where d.phoneNumber = ?1")
    boolean existsByPhoneNumber(String phoneNumber);


}