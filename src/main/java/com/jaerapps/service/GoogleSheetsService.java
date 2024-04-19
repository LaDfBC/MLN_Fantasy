package com.jaerapps.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.jaerapps.BotRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class GoogleSheetsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetsService.class);

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/creds/credentials.json";


    /**
     * Creates an authorized Credential object - This is the baseline way to do it and I don't care about making it more
     *  secure since this isn't getting deployed anywhere.
     * If you're not Kali and you're reading this, go <a href="https://developers.google.com/sheets/api/quickstart/java">here</a> and
     *  do the steps on that page to get your own credentials file.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static GoogleCredentials getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleSheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleCredentials googleCredentials;
        try(InputStream inputSteam = GoogleSheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH)) {
            googleCredentials = GoogleCredentials.fromStream(in).createScoped(SCOPES);
        }
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        return googleCredentials;
        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = null;
////        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
////                HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(), SCOPES)
////                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
////                .setAccessType("offline")
////                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Authorizes and creates the service object for Google Sheets that will do the heavy lifting in reading and writing
     */
    private static Sheets getSheetsService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Create the sheets API client
        return new Sheets.Builder(HTTP_TRANSPORT,
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(getCredentials(HTTP_TRANSPORT)))
                .setApplicationName("MLN Fantasy")
                .build();
    }


    public static ValueRange getValues(String spreadsheetId, String range) throws IOException {
        final Sheets service;
        try {
             service = getSheetsService();
        } catch (IOException | GeneralSecurityException ex) {
            LOGGER.error("Unable to get Sheets Object");
            ex.printStackTrace();
            return null;
        }

        ValueRange result = null;
        try {
            // Gets the values of the cells in the specified range.
            result = service.spreadsheets().values().get(spreadsheetId, range).execute();
            int numRows = result.getValues() != null ? result.getValues().size() : 0;
            System.out.printf("%d rows retrieved.", numRows);
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
            } else {
                LOGGER.error("failed");
            }
        }
        return result;
    }


    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final String spreadsheetId = "1l26vOYLGc48NKSXKCFEmXKFvjjUaywqsmeOBHS_jp6Q";
        final String range = "Weekly Schedule!A1:B";
        final ValueRange result = getValues(spreadsheetId, range);
        final List<List<Object>> values = result.getValues();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name, Major");
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("%s, %s\n", row.get(0), row.get(4));
            }
        }
    }

}
