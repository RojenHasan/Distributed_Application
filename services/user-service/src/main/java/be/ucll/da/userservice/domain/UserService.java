package be.ucll.da.userservice.domain;

import be.ucll.da.userservice.api.model.AddUserRequest;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository;

    private final Faker faker = new Faker();

    private final List<User> users = new ArrayList<>();

    public User validateUser(Integer id) {
      return  userRepository.findById(id).orElse(null);
    }

    public User getUser(Integer id) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String email = firstName + "." + lastName + "@google.com";
        String password = "123";

        return new User(id, firstName, lastName, email, true);
    }

    public Optional<User> getUser1(Integer id) {
        return getAllUsers().stream()
                .filter(d -> Objects.equals(d.getId(), id))
                .findFirst();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void addUser(User user) {
        userRepository.save(user);
    }
}
