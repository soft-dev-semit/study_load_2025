package semit.isppd.studyload2025.core.services;

import semit.isppd.studyload2025.core.entities.Discipline;
import semit.isppd.studyload2025.core.repositories.DisciplineRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public List<Discipline> listAll(){
        return disciplineRepository.findAll();
    }

    public void save(Discipline discipline) {
        disciplineRepository.save(discipline);
    }

    public Discipline findByName(String name) {
        return disciplineRepository.getDisciplineByName(name);
    }

    public void deleteAll(){
        disciplineRepository.deleteAll();
    }
}
