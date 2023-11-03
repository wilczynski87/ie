package dlarodziny.wolontariusze.ie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dlarodziny.wolontariusze.ie.model.Role;
import dlarodziny.wolontariusze.ie.model.Volunteer;
import dlarodziny.wolontariusze.ie.model.VolunteerDetails;
import dlarodziny.wolontariusze.ie.repositories.VolunteerRepo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class VolunteerDTO {

    private final VolunteerRepo volunteerRepo;

    public VolunteerDTO(VolunteerRepo volunteerRepo) {
        this.volunteerRepo = volunteerRepo;
    }

    public Volunteer mapVolunteer(List<Object> listOfObjects) {
        var fullName = listOfObjects.get(0).toString();
        var email = listOfObjects.get(1).toString();

        var user = new Volunteer();
        user.setUsername(createLogin(fullName));
        user.setPassword(email.toLowerCase());
        user.setRole(Role.USER.getRoleDesc());
        user.setActive(true);
        
        return user;
    }

    public VolunteerDetails mapVolunteerDetails(List<Object> listOfObjects) {
        var fullName = listOfObjects.get(0).toString();
        var email = listOfObjects.get(1).toString();

        var t = volunteerRepo.findByUsername(createLogin(fullName));
        log.info("{}", t);
        
        var userDetails = new VolunteerDetails();
        userDetails.setEmail(email);
        userDetails.setName(null);
        
        return userDetails;
    }

    private String createLogin(String fullName) {
        return fullName.replaceAll(" ", "");
    }

    public Volunteer getVolunteer(String fullName) {
        // return new Volunteer();
        System.out.println("jest null = " + volunteerRepo.findByUsername(createLogin(fullName)) != null);
        System.out.println("nie jest pusta = " + !volunteerRepo.findByUsername(createLogin(fullName)).isEmpty());
        return volunteerRepo.findByUsername(createLogin(fullName)) != null && !volunteerRepo.findByUsername(createLogin(fullName)).isEmpty() 
            ? volunteerRepo.findByUsername(createLogin(fullName)).get(0)
            : null;
    }
    public Volunteer saveVolunteer(Volunteer volunteer) {
        return null;
        // return volunteerRepo.save(volunteer);
    }
    
}