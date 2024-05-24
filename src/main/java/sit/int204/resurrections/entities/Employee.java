package sit.int204.resurrections.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @Column(name = "employeeNumber", nullable = false)
    private Integer id;
    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;
    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;
    @Column(name = "extension", nullable = false, length = 10)
    private String extension;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "jobTitle", nullable = false, length = 50)
    private String jobTitle;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reportsTo")
    private Employee employees;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "officeCode")
    private Office office;
}