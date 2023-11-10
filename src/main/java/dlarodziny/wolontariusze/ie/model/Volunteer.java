package dlarodziny.wolontariusze.ie.model;

import java.util.Arrays;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    private boolean active;

    public Volunteerdetails getVolunteerDetails(Volunteerdetails[] volunteerDetails) {
        return Arrays.stream(volunteerDetails)
            .filter(details -> details.getPatron().equals(this.id))
            .findFirst()
            .orElse(null);
    }

    public String readRole() {
        return this.role.equals("ROLE_ADMIN") ? Role.ADMIN.getRoleDesc() : Role.USER.getRoleDesc();
    }

}
