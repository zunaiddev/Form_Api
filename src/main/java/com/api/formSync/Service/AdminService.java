package com.api.formSync.Service;

import com.api.formSync.dto.UserInfo;
import com.api.formSync.model.User;
import com.api.formSync.util.Role;
import com.api.formSync.util.Validator;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AdminService {
    private UserService userService;
    private PasswordEncoder encoder;

    public List<UserInfo> getUsers() {
        return userService.get().stream()
                .map(UserInfo::new).toList();
    }

    public UserInfo getUser(Long id) {
        return new UserInfo(userService.get(id));
    }

    public UserInfo getUser(String email) {
        return new UserInfo(userService.get(email));
    }

    public UserInfo updateUser(Long id, Map<String, Object> updates) {
        User user = userService.get(id);
        System.out.println(user);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    if (!Validator.name(value.toString())) {
                        throw new ValidationException("Invalid name Format");
                    }

                    user.setName(value.toString());
                    break;
                case "email":
                    System.out.println(Validator.email(value.toString()));
                    System.out.println(value);
                    if (!Validator.email(value.toString())) {
                        throw new ValidationException("Invalid email");
                    }

                    user.setEmail((String) value);
                    break;
                case "password":
                    if (!Validator.password(value.toString())) {
                        throw new ValidationException("Invalid Password Format");
                    }

                    user.setPassword(encoder.encode((String) value));
                    break;
                case "role":
                    if (!Validator.isRole(value.toString())) {
                        throw new ValidationException("Role Can Be only ADMIN, ULTIMATE, USER");
                    }

                    user.setRole(Role.valueOf(value.toString()));
                    break;
                case "lock":
                    if (!Validator.isBool(value)) {
                        throw new ValidationException("Lock Should be a boolean");
                    }

                    user.setLocked((boolean) value);
                    break;
                case "regenerateKey":
                    if (!Validator.isBool(value)) {
                        throw new ValidationException("regenerateKey Should be a boolean");
                    }

                    if ((boolean) value) {
                        user.getKey().reGenerate();
                    }
                    break;
                case "resetLimit":
                    if (!Validator.isBool(value)) {
                        throw new ValidationException("resetLimit Should be a boolean");
                    }

                    if ((boolean) value) {
                        user.getKey().setRequestCount(0);
                    }
                    break;
                default:
                    throw new ValidationException("Invalid");
            }
        });

        return new UserInfo(userService.update(user));
    }

    public void deleteUser(Long id) {
        userService.delete(id);
    }
}
