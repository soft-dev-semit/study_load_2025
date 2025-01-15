package semit.isppd.studyload2025.core.repositories;

import semit.isppd.studyload2025.core.entities.StudyloadRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StudyloadRowRepository extends JpaRepository<StudyloadRow,Long> {
    @Modifying
    @Query("delete from StudyloadRow c")
    void deleteAll();
}
