package com.rodrigues.silva.marcos.ms.auth_api.core.model;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;
}
