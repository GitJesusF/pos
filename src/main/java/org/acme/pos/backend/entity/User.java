package org.acme.pos.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
public class User {

  @Id
  @Column(updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column( nullable = false)
  private String email;

  private Boolean active;


  @ManyToOne
  @JoinColumn(name = "role_id", referencedColumnName = "id")
  private Role role;

  public boolean isActive(){
    return active!=null?active:false;
  }
}