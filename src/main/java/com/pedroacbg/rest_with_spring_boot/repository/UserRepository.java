package com.pedroacbg.rest_with_spring_boot.repository;

import com.pedroacbg.rest_with_spring_boot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username =: username")
    User findByUsername(@Param("username") String username);

}
