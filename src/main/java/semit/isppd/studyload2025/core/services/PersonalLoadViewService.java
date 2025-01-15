package semit.isppd.studyload2025.core.services;

import semit.isppd.studyload2025.core.entities.views.PersonalLoadView;
import semit.isppd.studyload2025.core.repositories.PersonalLoadViewRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PersonalLoadViewService {

    private final PersonalLoadViewRepository personalLoadViewRepository;

    public PersonalLoadViewService(PersonalLoadViewRepository personalLoadViewRepository) {
        this.personalLoadViewRepository = personalLoadViewRepository;
    }

    public List<PersonalLoadView> getPSLVMData(String semester, String pname) {
        return personalLoadViewRepository.getPSLVM(semester, pname);
    }
}
