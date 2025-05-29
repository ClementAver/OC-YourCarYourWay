package com.openclassrooms.ycyw_back.repositories;

import com.openclassrooms.ycyw_back.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByChatId(Integer id);
}
