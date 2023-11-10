package dlarodziny.wolontariusze.ie.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dlarodziny.wolontariusze.ie.model.Contact;
import dlarodziny.wolontariusze.ie.repositories.ContactRepo;

public class ContactServiceTest {

    private final ContactRepo contactRepo = Mockito.mock(ContactRepo.class);
    private final VolunteerService volunteerService = Mockito.mock(VolunteerService.class);
    ContactService contactService = new ContactService(contactRepo, volunteerService);

    Contact contact1 = new Contact(1l);
    Contact contact2 = new Contact(1l);
    Contact contact3 = new Contact(2l);
    Contact contact4 = new Contact(3l);

    @BeforeEach
    void setUp() {
        contact1.setId(1l);
        contact1.setContactName("contact 1");
        contact1.setPhone("1111111111");
        contact1.setLastContact(LocalDate.now());


        contact2.setId(2l);
        contact2.setContactName("contact 1");
        contact2.setPhone("1111111111");
        contact2.setLastContact(LocalDate.now());

        contact3.setId(3l);
        contact3.setContactName("contact 3");
        contact3.setPhone("3333333333");
        contact3.setLastContact(LocalDate.now().plusDays(1));

        contact4.setId(4l);
        contact4.setContactName("contact 4");
        contact4.setPhone("4444444444");
        contact4.setLastContact(LocalDate.now().minusDays(1));
    }

    @Test
    void removeDuplicatesRemoveContact() {
        // Given
        List<Contact> contactsFromRows = List.of(contact1);
        List<Contact> contactsFromDb = List.of(contact2);
        contactService.setContactsFromDb(contactsFromDb);
        contactService.setContactsFromRows(contactsFromRows);

        // When
        contactService.removeDuplicatesFromRows();

        // Then
        assertEquals(0, contactService.getContactsFromRows().size());
    }
    
}