package sit.int204.resurrections.dtos;

import lombok.Setter;

//@Getter
@Setter
public class SimpleEmployeeDTO {
    private String firstName;
    private String lastName;
    private String officeCity;

    public String getName() {
        return firstName + " " + lastName;
    }
}
