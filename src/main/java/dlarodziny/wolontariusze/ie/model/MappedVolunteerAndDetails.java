package dlarodziny.wolontariusze.ie.model;

import lombok.Data;
import java.util.List;

@Data
public class MappedVolunteerAndDetails {

    private Volunteer volunteer;
    private Volunteerdetails volunteerdetails;
    
    public MappedVolunteerAndDetails(){}

    public MappedVolunteerAndDetails(List<Object> objectList){
        if(objectList == null || objectList.isEmpty()) throw new NullPointerException("row is empty");
        this.volunteer = mapVolunteerFromRow(objectList);
        this.volunteerdetails = mapVolunteerDetailsFromRow(objectList);
    }

    private Volunteer mapVolunteerFromRow(List<Object> listOfObjects) {
        var fullName = listOfObjects.get(0).toString();
        var email = listOfObjects.get(1).toString();

        var user = new Volunteer();
        user.setUsername(createLogin(fullName));
        user.setPassword(email.toLowerCase());
        user.setRole(Role.USER.getRoleDesc());
        user.setActive(true);
        
        return user;
    }

    public Volunteerdetails mapVolunteerDetailsFromRow(List<Object> listOfObjects) {
        var fullName = listOfObjects.get(0).toString().trim();
        var email = listOfObjects.get(1).toString();
        
        var userDetails = new Volunteerdetails();
        userDetails.setEmail(email);
        userDetails.setName(mapName(fullName));
        userDetails.setSurname(mapSurname(fullName));
        
        return userDetails;
    }
    
    public void setIdAndPatron(Long id) {
        this.volunteer.setId(id);
        this.volunteerdetails.setPatron(id);
    }

    private String createLogin(String fullName) {
        return fullName.replaceAll(" ", "");
    }

    private String mapName(String fullName) {
		return fullName.split(" ").length > 0 ? fullName.split(" ")[0] : "";
	}
	private String mapSurname(String fullName) {
		return fullName.split(" ").length > 1 ? fullName.split(" ")[1] : "";
	}

}