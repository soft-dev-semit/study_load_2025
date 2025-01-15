package semit.isppd.studyload2025.core.services;

import semit.isppd.studyload2025.core.entities.Teacher;
import semit.isppd.studyload2025.core.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> listAll() {
        return teacherRepository.listAllOrderName();
    }

    public List<Teacher> listAllOrderName() {
        return teacherRepository.listAllOrderName();
    }

    public void save(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public Teacher findByName(String name) {
        return teacherRepository.getTeacherByName(name);
    }

    public Teacher getByID(long id) {
        return teacherRepository.getById(id);
    }

//    public List<String> listIpFilenames(){
//        return teacherRepository.listIpFilenames();
//    }

    public void deleteAll() {
        teacherRepository.deleteAll();
    }

    public List<Teacher> listWithEmails() {
        return teacherRepository.listWithEmails();
    }

}
