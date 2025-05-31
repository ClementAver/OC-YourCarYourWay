package com.openclassrooms.ycyw_back.repositories;

import com.openclassrooms.ycyw_back.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c WHERE c.customer.id = :id")
    List<Chat> findByCustomerId(@Param("id") int id);

    @Query("SELECT c FROM Chat c WHERE c.employee.id = :id")
    List<Chat> findByEmployeeId(@Param("id") int id);
}