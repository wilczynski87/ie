package dlarodziny.wolontariusze.ie.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.sheets.v4.model.ValueRange;

import dlarodziny.wolontariusze.ie.service.Credentials;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MainController {

	private final Credentials credentials;

	public MainController(Credentials credentials) {
		this.credentials = credentials;
	};
    
    @GetMapping("/getVolunteers")
    public List<List<Object>> getVolunteers(@RequestParam String id, @RequestParam String fold) throws IOException, GeneralSecurityException {
        log.info("\n\ngetVolunteers endpoint reached!\nid = {}\nfold = {}", id, fold);

        credentials.setSPREADSHEET_ID(id);
        credentials.setUpCredentials();

		String range = fold + "!A1:B27";

		ValueRange response = credentials.readSheetsService().spreadsheets().values()
			.get(credentials.getSpreadshitId(), range)
			.execute();

		List<List<Object>> values = response.getValues();

		if(values == null || values.isEmpty()) {
			System.out.println("gówno");
		} else {
			for(List<Object> row : values) {
				System.out.println(row);
			}
		}

        return values;
    }

    @GetMapping("/getContacts")
    public List<List<Object>> getContacts(@RequestParam String id, @RequestParam String fold, @RequestParam String scope) throws IOException, GeneralSecurityException {
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

		List<List<Object>> values = response.getValues();

		if(values == null || values.isEmpty()) {
			System.out.println("gówno");
		} else {
			for(List<Object> row : values) {
				System.out.println(row);
			}
		}

        return values;
    }
}