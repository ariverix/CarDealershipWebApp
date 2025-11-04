package org.example.apicontract.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "employees", itemRelation = "employee")
public class EmployeeResponse extends RepresentationModel<EmployeeResponse> {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String position;
    private final String email;
    private final LocalDateTime hiredAt;

    public EmployeeResponse(Long id, String firstName, String lastName, String position,
                            String email, LocalDateTime hiredAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.email = email;
        this.hiredAt = hiredAt;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public LocalDateTime getHiredAt() { return hiredAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmployeeResponse that = (EmployeeResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(position, that.position) &&
                Objects.equals(email, that.email) &&
                Objects.equals(hiredAt, that.hiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, position, email, hiredAt);
    }
}