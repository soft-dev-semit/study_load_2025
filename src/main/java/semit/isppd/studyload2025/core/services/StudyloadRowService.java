package semit.isppd.studyload2025.core.services;

import semit.isppd.studyload2025.core.entities.StudyloadRow;
import semit.isppd.studyload2025.core.repositories.StudyloadRowRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudyloadRowService {

    private final StudyloadRowRepository studyloadRowRepository;

    public StudyloadRowService(StudyloadRowRepository studyloadRowRepository) {
        this.studyloadRowRepository = studyloadRowRepository;
    }

    public void save(StudyloadRow studyloadRow) {
        studyloadRowRepository.save(studyloadRow);
    }

    public void deleteAll() {
        studyloadRowRepository.deleteAll();
    }

}
