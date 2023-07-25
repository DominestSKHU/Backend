package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findAllByResidenceSemester(ResidenceSemester residenceSemester);

    boolean existsByResidenceSemester(ResidenceSemester residenceSemester);
}
