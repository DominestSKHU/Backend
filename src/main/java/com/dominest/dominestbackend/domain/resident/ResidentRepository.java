package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    @Query("SELECT r FROM Resident r" +
            " join fetch r.room" +
            " where r.residenceSemester = :residenceSemester")
    List<Resident> findAllByResidenceSemesterFetchRoom(@Param("residenceSemester") ResidenceSemester residenceSemester);

    Resident findByNameAndResidenceSemester(String name, ResidenceSemester residenceSemester);

    Resident findByResidenceSemesterAndRoom(ResidenceSemester residenceSemester, Room room);

    List<Resident> findAllByResidenceSemester(ResidenceSemester semester);

    // [학번, 전화번호, 이름] 중복제한:  똑같은 학생이 한 학기에 둘 이상 있을 순 없다.
    boolean existsByStudentIdAndPhoneNumberAndNameAndResidenceSemester(String studentId, String phoneNumber, String name, ResidenceSemester residenceSemester);


}
