package dlarodziny.wolontariusze.ie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    private Long id;
    private String contactName;
    private String phone;
    private String email;
    private LocalDate lastContact;
    private int attitude;
    private String comment;
    private Long patron;

    public Contact(Long patron) {
        this();
        this.patron = patron;
    }

    public VolunteerDetails getVolunteerDetails(VolunteerDetails[] volunteerDetails) {
        return Arrays.stream(volunteerDetails)
            .filter(details -> details.getPatron().equals(this.patron))
            .findFirst()
            .orElse(null);
    }
    
    public String getVolunteerName(VolunteerDetails[] volunteerDetails) {
        return Arrays.stream(volunteerDetails)
            .filter(details -> details.getPatron().equals(this.patron))
            .findFirst()
            .map(vd -> vd.getName() + " " + vd.getSurname())
            .orElse("brak danych");
    }
}
