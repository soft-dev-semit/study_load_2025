package semit.isppd.studyload2025.core.services;

import semit.isppd.studyload2025.core.entities.views.EdAsStView;
import semit.isppd.studyload2025.core.repositories.EdAsStViewRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EdAsStViewService {

    private final EdAsStViewRepository edAsStViewRepository;

    public EdAsStViewService(EdAsStViewRepository edAsStViewRepository) {
        this.edAsStViewRepository = edAsStViewRepository;
    }

    public List<EdAsStView> getEASVM13Data(String semester, String course) {
        return edAsStViewRepository.getEASVM13(semester, course);
    }

    public List<EdAsStView> getEASVMData(String semester, String course) {
        return edAsStViewRepository.getEASVM(semester, course);
    }

}
