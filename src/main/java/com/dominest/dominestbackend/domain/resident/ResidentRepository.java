package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    @Query("SELECT r FROM Resident r" +
            " join fetch r.room" +
            " where r.residenceSemester = :residenceSemester")
    List<Resident> findAllByResidenceSemester(@Param("residenceSemester") ResidenceSemester residenceSemester);

    boolean existsByResidenceSemester(ResidenceSemester residenceSemester);

    Resident findByNameAndResidenceSemester(String name, ResidenceSemester residenceSemester);

    Resident findByStudentIdAndResidenceSemester(String studentId, ResidenceSemester residenceSemester);

    Resident findByResidenceSemesterAndRoom(ResidenceSemester residenceSemester, Room room);
}
