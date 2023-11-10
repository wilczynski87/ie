package dlarodziny.wolontariusze.ie.service;

import org.apache.logging.log4j.util.StringBuilders;
import org.springframework.stereotype.Service;

import java.util.List;

import dlarodziny.wolontariusze.ie.model.MappedVolunteerAndDetails;
import dlarodziny.wolontariusze.ie.model.Role;
import dlarodziny.wolontariusze.ie.model.Volunteer;
import dlarodziny.wolontariusze.ie.model.Volunteerdetails;
import dlarodziny.wolontariusze.ie.repositories.VolunteerDetailsRepo;
import dlarodziny.wolontariusze.ie.repositories.VolunteerRepo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
public class VolunteerService {

    private final VolunteerRepo volunteerRepo;
    private final VolunteerDetailsRepo volunteerDetailsRepo;

    public VolunteerService(VolunteerRepo volunteerRepo, VolunteerDetailsRepo volunteerDetailsRepo) {
        this.volunteerRepo = volunteerRepo;
        this.volunteerDetailsRepo = volunteerDetailsRepo;
    }

    private List<Volunteer> volunteerList;
    private List<Volunteerdetails> volunteerDetailsList;
    private List<MappedVolunteerAndDetails> potentialVolunteers;

    public void importVolunteerList() {
        this.volunteerList = volunteerRepo.findAll();
    }

    public List<Volunteerdetails> importVolunteerdetailsList() {
        this.volunteerDetailsList = volunteerDetailsRepo.findAll();
        // this.volunteerDetailsList.forEach(System.out::println);
        return this.volunteerDetailsList;
    }

    public List<MappedVolunteerAndDetails> saveVolunteersAndDetails() {
        return potentialVolunteers.stream()
			// .peek(System.out::println)
            // cutting of rows, from potential voluntters, what have same email!
            .filter(row -> !emailMatchVolunteerDetails(row.getVolunteerdetails().getEmail()))
			// .peek(System.out::println)
            // cutting of rows, from potential voluntters, what have same username!
            .filter(row -> !isDublicatedUsername(row.getVolunteer()))
			// .peek(System.out::println)
			.map(this::saveVolunteerAndDetailsToDatabase)
            .toList()
			;
    }

    private MappedVolunteerAndDetails saveVolunteerAndDetailsToDatabase(MappedVolunteerAndDetails mappedVolunteerAndDetails) {
        //save volunteer to db
        var savedMappedVolunteer = volunteerRepo.save(mappedVolunteerAndDetails.getVolunteer());
        // set Patron for VolunteersDetails
        mappedVolunteerAndDetails.getVolunteerdetails().setPatron(savedMappedVolunteer.getId());
        // save VolunteersDetails to db
        var test1 = volunteerDetailsRepo.save(mappedVolunteerAndDetails.getVolunteerdetails());

        // for test
        mappedVolunteerAndDetails.setVolunteer(savedMappedVolunteer); 
        mappedVolunteerAndDetails.setVolunteerdetails(test1);
        return mappedVolunteerAndDetails;
    }

    private boolean emailMatchVolunteerDetails(String email) {
        // System.out.println(name + " " + surname + " " + email);
        // System.out.println(this.volunteerDetailsList);
        return this.volunteerDetailsList.stream()
            .anyMatch(volunteerDetails -> volunteerDetails.getEmail().equals(email))
            ;
    }
    private boolean isDublicatedUsername(Volunteer volunteer) {
        return this.volunteerList.stream()
            .map(v -> v.getUsername())
            .anyMatch(username -> username.equals(volunteer.getUsername()))
            ;
    }

    public Long findPatronByName(String name) {
        StringBuilder sB = new StringBuilder();
        if(this.volunteerDetailsList == null || this.volunteerDetailsList.isEmpty()) importVolunteerdetailsList();
        if(this.volunteerDetailsList.isEmpty()) return null;
        
        return this.volunteerDetailsList.stream()
            .filter(details -> {
                 String fullName = sB.append(details.getName()).append(" ").append(details.getSurname()).toString();
                 return fullName.equals(name) || fullName.contains(name) || name.contains(fullName);
            })
            .map(Volunteerdetails::getPatron)
            .findFirst()
            .orElse(0l);
    }

    
}