package semit.isppd.studyload2025.auth.service;

import semit.isppd.studyload2025.auth.entities.User;
import semit.isppd.studyload2025.auth.repositories.RoleRepository;
import semit.isppd.studyload2025.auth.repositories.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User getUserByUsername(String email){
        return userRepository.getUserByUsername(email);
    }

    public void addUser(User user){
        user.getRoles().add(roleRepository.getRoleByName("USER"));
        userRepository.save(user);
    }
    public List<User> listAll(){
        return userRepository.findAll();
    }

    public void save(User user){
        userRepository.save(user);
    }
}
