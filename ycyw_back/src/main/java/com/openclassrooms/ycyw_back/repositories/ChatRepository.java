package com.openclassrooms.ycyw_back.repositories;

import com.openclassrooms.ycyw_back.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}

