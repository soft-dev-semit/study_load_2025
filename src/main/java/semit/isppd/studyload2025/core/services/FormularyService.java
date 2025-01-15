package semit.isppd.studyload2025.core.services;

import semit.isppd.studyload2025.core.entities.Formulary;
import semit.isppd.studyload2025.core.repositories.FormularyRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FormularyService {

    private final FormularyRepository formularyRepository;

    public FormularyService(FormularyRepository formularyRepository) {
        this.formularyRepository = formularyRepository;
    }

    public List<Formulary> listAll(){
        return formularyRepository.findAll();
    }

    public void deleteAll(){
        formularyRepository.deleteAll();
    }

    public void save(Formulary formulary){
        formularyRepository.save(formulary);
    }
}
