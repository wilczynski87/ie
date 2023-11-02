package dlarodziny.wolontariusze.ie;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import dlarodziny.wolontariusze.ie.model.SheetsAndProject;
import dlarodziny.wolontariusze.ie.service.Credentials;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class IeApplication {
	// private static Sheets sheetsService;
    // private static String APPLICATION_NAME = "Google Sheets Example";
    // private static String SPREADSHEET_ID = "1txK2VhZ_pojHliFtKzjimlIirYqSf6cLTYN11pW_R-M";

    // private static Credential authorize() throws IOException, GeneralSecurityException {

    //     InputStream in = SheetsAndProject.class.getResourceAsStream("/google-sheets-client-secret.json");
    //     GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
    //         GsonFactory.getDefaultInstance(), 
    //         new InputStreamReader(in)
    //         );

	// 	List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

	// 	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	// 		GoogleNetHttpTransport.newTrustedTransport(),
	// 		GsonFactory.getDefaultInstance(),
	// 		clientSecrets,
	// 		scopes)
	// 		.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
	// 		.setAccessType("offline")
	// 		.build();

	// 	Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");


    //     return credential;
    // }

    // public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
    //     Credential credential = authorize();
    //     return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
    //         .setApplicationName(APPLICATION_NAME)
    //         .build();
    // }

	public static void main(String[] args) {
		SpringApplication.run(IeApplication.class, args);
		log.info("\n\napp started\n");
	}

}
