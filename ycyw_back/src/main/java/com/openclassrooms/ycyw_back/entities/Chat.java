package com.openclassrooms.ycyw_back.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @CreationTimestamp
    @Column(name = "creation_time", updatable = false)
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "update_time", updatable = false)
    private LocalDateTime updateTime;

    // relations
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private User employee;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    private List<Message> messages;
}
