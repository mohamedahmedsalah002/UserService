package com.example.VirtualBank.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
@Entity
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(nullable = false,updatable = false,unique = true,columnDefinition = "BINARY(16)")
  //Universally Unique Identifier
    private UUID userId;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Email
    @Column(unique = true,nullable = false)
    private String email;
    private String password;

    //whe did anotatioontaion before function PrePersist to apply it before we do insert in database
    @PrePersist
    private void generateUserId(){
        if (userId==null){
            userId=UUID.randomUUID();
        }
    }



}
