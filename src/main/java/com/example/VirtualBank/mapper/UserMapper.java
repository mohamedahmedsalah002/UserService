package com.example.VirtualBank.mapper;

import com.example.VirtualBank.model.dto.UserDto;
import com.example.VirtualBank.model.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {


    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
   /*     UserDto userDto= new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
return userDto;*/

        //we use Builder design pattern

    }


    // Convert List of Entities to List of DTOs
    public static List<UserDto> toDtoList(List<User> userList) {
        if (userList == null || userList.isEmpty()) {
            return List.of();  // Return empty list instead of null
        }
        return userList.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }







    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return User.builder()
                .id(userDto.getId())
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }
    // Convert List of DTOs to List of Entities
    public static List<User> toEntityList(List<UserDto> userDtoList) {
        if (userDtoList == null || userDtoList.isEmpty()) {
            return List.of();
        }
        return userDtoList.stream()
                .map(UserMapper::toEntity)
                .collect(Collectors.toList());
    }

}
