package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.user.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FindUserByLoginUseCase {
    
    private final UserService userService;
    
    public FindUserByLoginUseCase(UserService userService) {
        this.userService = userService;
    }
    
    public UserResponse execute(String login) {
        return userService.findByLogin(login);
    }
}