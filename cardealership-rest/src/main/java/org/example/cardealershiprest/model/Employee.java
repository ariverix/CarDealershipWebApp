package org.example.cardealershiprest.model;

import java.time.LocalDateTime;

public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private String email;
    private LocalDateTime hiredAt;

    public Employee(Long id, String firstName, String lastName, String position, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.email = email;
        this.hiredAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getHiredAt() { return hiredAt; }
}