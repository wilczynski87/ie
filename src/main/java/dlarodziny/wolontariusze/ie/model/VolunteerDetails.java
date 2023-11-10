package dlarodziny.wolontariusze.ie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Volunteerdetails { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long patron;
    String name;
    String surname;
    LocalDate dob;
    LocalDate started;
    LocalDate ended;
    LocalDate lastActivity;
    String phone;
    String email;
    String address;

    public String lasting() {
        return Period.between(this.started, LocalDate.now()).toString();
    }

    public Volunteerdetails(Long patron) {
        this.patron = patron;
    }

    public static Volunteerdetails getVolunteerdetails(Volunteerdetails[] Volunteerdetails, Long id) {
        System.out.println(Volunteerdetails);
        return Arrays.stream(Volunteerdetails)
            .filter(details -> details.getPatron().equals(id))
            .findFirst()
            .orElse(null);
    }

    
}
