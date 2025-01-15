package semit.isppd.studyload2025.core.repositories;

import semit.isppd.studyload2025.core.entities.views.EdAsStView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EdAsStViewRepository extends JpaRepository<EdAsStView,Long> {
    @Query(value = "select e from EdAsStView e where e.csem=:semester and e.ccor<:course")
    List<EdAsStView> getEASVM13(@Param("semester") String semester, @Param("course") String course);
    @Query(value = "select e from EdAsStView e where e.csem=:semester and e.ccor=:course")
    List<EdAsStView> getEASVM(@Param("semester") String semester, @Param("course") String course);

}
