package br.com.restaurant_hub.restauranthub.usertype.application.service;

import br.com.restaurant_hub.restauranthub.usertype.application.dto.*;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserTypeService {
    
    UserType createUserType(CreateUserTypeRequest request);
    
    Optional<UserType> findById(Long id);

    Page<UserType> findAll(Integer page, Integer pageSize, String orderBy);

    Optional<UserType> updateById(Long id, UpdateUserTypeRequest request);
    
    Boolean deleteById(Long id);
    
    Boolean existsByName(String name);
}