package com.elian.market.api.service;

import com.elian.market.api.domain.User;
import com.elian.market.api.dto.UserResponseDto;

import java.util.List;

public interface IUserService {

    User getCurrentUser();

    List<UserResponseDto> findAll();

    UserResponseDto findById(Long id);
}