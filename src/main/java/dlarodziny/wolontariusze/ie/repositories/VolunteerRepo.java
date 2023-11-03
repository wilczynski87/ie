package dlarodziny.wolontariusze.ie.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dlarodziny.wolontariusze.ie.model.Volunteer;

public interface VolunteerRepo extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findByUsername(String username);
}