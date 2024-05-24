package sit.int204.resurrections.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    private Integer customerNumber;
    private String customerName;
    private String contactLastName;
    private String contactFirstName;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String password;
    private String role;

    private Double creditLimit;

    @ManyToOne
    @JoinColumn(name = "salesRepEmployeeNumber")
    private Employee salesEmployee;

    @OneToMany(mappedBy = "customerNumber")
    private List<Order> orders = new ArrayList<>();
}