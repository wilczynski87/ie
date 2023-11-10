package dlarodziny.wolontariusze.ie.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dlarodziny.wolontariusze.ie.model.Contact;

public interface ContactRepo extends JpaRepository<Contact, Long>{
    
}