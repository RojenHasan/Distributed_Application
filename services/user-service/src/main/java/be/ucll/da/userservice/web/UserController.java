package be.ucll.da.userservice.web;

import be.ucll.da.userservice.api.UserApiDelegate;
import be.ucll.da.userservice.api.model.AddUserRequest;
import be.ucll.da.userservice.api.model.ApiUser;
import be.ucll.da.userservice.api.model.ApiUsers;
import be.ucll.da.userservice.domain.User;
import be.ucll.da.userservice.domain.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserController implements UserApiDelegate {
    private  final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<ApiUser> getUserByUserId(Integer id) {
        User user = userService.getUser1(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));


        ApiUser apiUser = new ApiUser();
        apiUser.setUserId(user.getId());
        apiUser.setFirstName(user.getFirstName());
        apiUser.setLastName(user.getLastName());
        apiUser.setEmail(user.getEmail());

        return ResponseEntity.ok(apiUser);
    }

    @Override
    public ResponseEntity<ApiUsers> getAllUsers() {
        ApiUsers users = new ApiUsers();
        users.setUsers(
                userService.getAllUsers().stream()
                        .map(this::toDto)
                        .toList()
        );

        return ResponseEntity.ok(users);
    }

    private ApiUser toDto(User user) {
        return new ApiUser()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .isClient(user.isClient());
    }
    @Override
    @PostMapping
    public ResponseEntity<ApiUser> createUser(@Valid @RequestBody ApiUser apiUser) {
        User user = new User(apiUser.getUserId(), apiUser.getFirstName(), apiUser.getLastName(), apiUser.getEmail(), apiUser.getIsClient());
        userService.addUser(user);

        return ResponseEntity.ok().build();
    }

}
