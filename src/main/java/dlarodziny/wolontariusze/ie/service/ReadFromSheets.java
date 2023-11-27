package dlarodziny.wolontariusze.ie.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.hibernate.engine.internal.Collections;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import dlarodziny.wolontariusze.ie.IeApplication;

@Service
public class ReadFromSheets {
	private Sheets sheetsService;
    private String APPLICATION_NAME = "Google Sheets Example";
    private final static GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final static String TOKENS_DIRECTORY_PATH = "tokens";
    private final static String CREDENTIALS_FILE_PATH = "/google-sheets-client-secret.json";
    private final static List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

    private String SPREADSHEET_ID = "1txK2VhZ_pojHliFtKzjimlIirYqSf6cLTYN11pW_R-M";
    private String fold = "Opiekun";

    private Credential authorize() throws IOException, GeneralSecurityException {

        InputStream in = IeApplication.class.getResourceAsStream("/google-sheets-client-secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            JSON_FACTORY, 
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

    /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = IeApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileUploadException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        
    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }
}