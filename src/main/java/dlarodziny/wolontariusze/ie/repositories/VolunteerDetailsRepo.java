package dlarodziny.wolontariusze.ie.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dlarodziny.wolontariusze.ie.model.Volunteerdetails;

public interface VolunteerDetailsRepo extends JpaRepository<Volunteerdetails, Long>{
    
}