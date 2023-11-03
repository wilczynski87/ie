package dlarodziny.wolontariusze.ie.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

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

import dlarodziny.wolontariusze.ie.IeApplication;

@Service
public class ReadFromSheets {
	private Sheets sheetsService;
    private String APPLICATION_NAME = "Google Sheets Example";
    private String SPREADSHEET_ID = "1txK2VhZ_pojHliFtKzjimlIirYqSf6cLTYN11pW_R-M";
    private String fold = "Opiekun";

    private Credential authorize() throws IOException, GeneralSecurityException {

        InputStream in = IeApplication.class.getResourceAsStream("/google-sheets-client-secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            GsonFactory.getDefaultInstance(), 
            new InputStreamReader(in)
            );

		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			GoogleNetHttpTransport.newTrustedTransport(),
			GsonFactory.getDefaultInstance(),
			clientSecrets,
			scopes)
			.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
			.setAccessType("offline")
			.build();

		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");


        return credential;
    }

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }

    public void setUpCredentials() throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
    }

    public Sheets readSheetsService() {
        return this.sheetsService;
    }

    public String getSpreadshitId() {
        return this.SPREADSHEET_ID;
    }

    public String setSpreadshitId(String id) {
        this.SPREADSHEET_ID = id;
        return this.SPREADSHEET_ID;
    }
    public void setSheetsService(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    public String getAPPLICATION_NAME() {
        return this.APPLICATION_NAME;
    }

    public void setAPPLICATION_NAME(String APPLICATION_NAME) {
        this.APPLICATION_NAME = APPLICATION_NAME;
    }

    public String getSPREADSHEET_ID() {
        return this.SPREADSHEET_ID;
    }

    public void setSPREADSHEET_ID(String SPREADSHEET_ID) {
        this.SPREADSHEET_ID = SPREADSHEET_ID;
    }

    public String getFold() {
        return this.fold;
    }

    public void setFold(String fold) {
        this.fold = fold;
    }
    
}