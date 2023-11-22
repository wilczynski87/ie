package dlarodziny.wolontariusze.ie.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.sheets.v4.model.ValueRange;

import dlarodziny.wolontariusze.ie.model.MappedVolunteerAndDetails;
import dlarodziny.wolontariusze.ie.model.Volunteer;
import dlarodziny.wolontariusze.ie.service.ContactService;
import dlarodziny.wolontariusze.ie.service.ReadFromSheets;
import dlarodziny.wolontariusze.ie.service.SaveByRestService;
import dlarodziny.wolontariusze.ie.service.VolunteerService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class SaveController {

    private final SaveByRestService saveByRestService;
    private final ReadFromSheets credentials;
	private final VolunteerService volunteerService;
	private final ContactService contactService;

    public SaveController(SaveByRestService saveByRestService, ReadFromSheets credentials, VolunteerService volunteerService, ContactService contactService) {
        this.saveByRestService = saveByRestService;
        this.credentials = credentials;
        this.volunteerService = volunteerService;
        this.contactService = contactService;
    }

    @GetMapping("/saveVolunteers")
    public Flux<Object> getVolunteers(@RequestParam String id, @RequestParam String fold, @RequestParam String scope) throws IOException, GeneralSecurityException {
        log.info("\n\saveVolunteers endpoint reached!\nid = {}\nfold = {}", id, fold);

        credentials.setSPREADSHEET_ID(id);
        credentials.setUpCredentials();

		String range = fold + "!" + scope;

		ValueRange response = credentials.readSheetsService().spreadsheets().values()
			.get(credentials.getSpreadshitId(), range)
			.execute();

		//import from sheet and translat to classes
		var testList = response.getValues().stream()
			.filter(row -> row != null && !row.isEmpty())
			.map(MappedVolunteerAndDetails::new)
			.toList();
		volunteerService.setPotentialVolunteers(testList);

		// import from database
		volunteerService.importVolunteerdetailsList();
		volunteerService.importVolunteerList();
        // removing volunteer from list, if they are in db
        volunteerService.filterVolunteersToSave();
        // logger
        System.out.println("MappedVolunteerAndDetails");
        volunteerService.getPotentialVolunteers().forEach(System.out::println);
        System.out.println();

        var savedFluxVlounteers = saveByRestService.saveNewVolunteers(volunteerService.getPotentialVolunteers().stream().map(MappedVolunteerAndDetails::getVolunteer).toList());
        var savedVlounteers = savedFluxVlounteers.collectList().block();
        // logger
        System.out.println("Volunteers saved:");
        savedVlounteers.forEach(System.out::println);
        System.out.println();

        volunteerService.mapVolunteersWithId(savedVlounteers.stream().map(x -> (Volunteer)x).toList());
        /* wyślij Volunteerdetails do API*/

        // logger
        System.out.println("VolunteersDetails before save:");
        return saveByRestService.saveNewVolunteerDetails(volunteerService.getPotentialVolunteers().stream()
            .map(MappedVolunteerAndDetails::getVolunteerdetails)
            .peek(System.out::println)
            .toList());
    }
    

}