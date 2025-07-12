package com.pedroacbg.rest_with_spring_boot.repository;

import com.pedroacbg.rest_with_spring_boot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
