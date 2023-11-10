package dlarodziny.wolontariusze.ie.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.sheets.v4.model.ValueRange;

import dlarodziny.wolontariusze.ie.model.Contact;
import dlarodziny.wolontariusze.ie.model.MappedVolunteerAndDetails;
import dlarodziny.wolontariusze.ie.service.ContactService;
import dlarodziny.wolontariusze.ie.service.ReadFromSheets;
import dlarodziny.wolontariusze.ie.service.VolunteerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MainController {

	private final ReadFromSheets credentials;
	private final VolunteerService volunteerService;
	private final ContactService contactService;

	public MainController(ReadFromSheets credentials, VolunteerService volunteerService, ContactService contactService) {
		this.credentials = credentials;
		this.volunteerService = volunteerService;
		this.contactService = contactService;
	}
    
    @GetMapping("/getVolunteers")
    public List<MappedVolunteerAndDetails> getVolunteers(@RequestParam String id, @RequestParam String fold, @RequestParam String scope) throws IOException, GeneralSecurityException {
        log.info("\n\ngetVolunteers endpoint reached!\nid = {}\nfold = {}", id, fold);

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

        return volunteerService.saveVolunteersAndDetails();
    }

    @GetMapping("/getContacts")
    public List<Contact> getContacts(@RequestParam String id, @RequestParam String fold, @RequestParam String scope) throws IOException, GeneralSecurityException {
        log.info("\n\ngetContacts endpoint reached!" +
        "\nid = {}" +
        "\nfold = {}" +
        "\nscope = {}", 
        id, fold, scope);

        credentials.setSPREADSHEET_ID(id);
        credentials.setUpCredentials();

		String range = fold + "!" + scope;

		ValueRange response = credentials.readSheetsService().spreadsheets().values()
			.get(credentials.getSpreadshitId(), range)
			.execute();

		// import rows from sheet
		List<List<Object>> rows = response.getValues();

		// mapping contacts to service class
		rows.stream()
			.filter(row -> row != null && !row.isEmpty())
			.map(contactService::mapContactFromRow)
			.forEach(contact -> contactService.getContactsFromRows().add(contact));

		// removing duplicates
		contactService.removeDuplicatesFromRows();

        return contactService.saveContactsToDb();
    }
}