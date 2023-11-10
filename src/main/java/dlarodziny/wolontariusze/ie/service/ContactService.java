package dlarodziny.wolontariusze.ie.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.stereotype.Service;

import dlarodziny.wolontariusze.ie.model.Attitude;
import dlarodziny.wolontariusze.ie.model.Contact;
import dlarodziny.wolontariusze.ie.repositories.ContactRepo;
import lombok.Data;

@Service
@Data
public class ContactService {

    private final ContactRepo contactRepo;
    private final VolunteerService volunteerService;

    public ContactService(ContactRepo contactRepo, VolunteerService volunteerService) {
        this.contactRepo = contactRepo;
        this.volunteerService = volunteerService;
    }

    private List<Contact> contactsFromRows = new ArrayList<>();
    private List<Contact> contactsFromDb;
    
    public Contact mapContactFromRow(List<Object> row) {
        var contact = new Contact();
        contact.setContactName(mapName(row));
        contact.setPhone(mapPhone(row));
        contact.setEmail(mapEmail(row));
        contact.setLastContact(mapLastContact(row));
        contact.setAttitude(mapAttitude(row).getNumber());
        contact.setComment(mapComment(row));
        contact.setPatron(mapVolunteer(row));
        return contact;
    }

    public List<Contact> importFromDb() {
        this.contactsFromDb = contactRepo.findAll();
        return this.contactsFromDb;
    }

    public List<Contact> removeDuplicatesFromRows() {
        if(this.contactsFromDb == null) this.contactsFromDb = this.contactRepo.findAll();
        if(this.contactsFromRows.isEmpty() || this.contactsFromDb.isEmpty()) return new ArrayList<>();
        List<Contact> duplicates = new ArrayList<>();
        for(Contact contactR : contactsFromRows) {
            for(Contact contactDb: contactsFromDb) {
                if(compareContacts(contactR, contactDb)) duplicates.add(contactR);
            }
        }
        this.contactsFromRows.removeAll(duplicates);
        return duplicates;
    }

    public List<Contact> saveContactsToDb() {
        return contactRepo.saveAll(this.contactsFromRows);
    }

    private boolean compareContacts(Contact contactR, Contact contactDb) {
        if(contactR.getContactName() == null) contactR.setContactName("");
        if(contactDb.getContactName() == null) contactDb.setContactName("");
        var name = contactR.getContactName().equals(contactDb.getContactName());

        if(contactR.getPhone() == null) contactR.setPhone("");
        if(contactDb.getPhone() == null) contactDb.setPhone("");
        var phone = contactR.getPhone().equals(contactDb.getPhone());

        if(contactR.getLastContact() == null) contactR.setLastContact(LocalDate.now());
        if(contactDb.getLastContact() == null) contactDb.setLastContact(LocalDate.now());
        var lastContact = contactR.getLastContact().equals(contactDb.getLastContact());

        return name && phone && lastContact;
    }

    private String mapName(List<Object> row) {
        if(row == null || row.isEmpty() || row.size() < 1) return "brak nazwy...";
        else return row.get(0).toString().trim(); 
    }
    private String mapPhone(List<Object> row) {
        if(row == null || row.isEmpty() || row.size() < 2) return "";
        else return row.get(1).toString().trim();
    }
    private String mapEmail(List<Object> row) {
        if(row == null || row.isEmpty() || row.size() < 3) return "";
        else return row.get(2).toString().trim();
    }
    private LocalDate mapLastContact(List<Object> row) {
        if(row == null || row.isEmpty() || row.size() < 4) return null;
        else {
            if(row.get(3) == null) return null;
            var stringDate = row.get(3).toString().trim().split("[./-]");
            if(stringDate.length < 3) return null;
            return LocalDate.of(Integer.parseInt(stringDate[2]), Integer.parseInt(stringDate[1]), Integer.parseInt(stringDate[0]));
        }
    }
    private Attitude mapAttitude(List<Object> row) {
        if(row == null || row.isEmpty() || row.size() < 5 || row.get(4).toString().isBlank()) return Attitude.NIEPODANO;
        else return Attitude.findByValue(row.get(4).toString().trim());
    }
    private String mapComment(List<Object> row) {
        if(row == null || row.isEmpty() || row.size() < 6) return "";
        else return row.get(5).toString().trim();
    }
    private Long mapVolunteer(List<Object> row) {
        if(row == null || row.isEmpty() || row.size() < 7) return 0l;
        else return volunteerService.findPatronByName((row.get(6).toString().trim()));
    }

}