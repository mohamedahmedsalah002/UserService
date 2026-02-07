package com.example.VirtualBank.controller;

import com.example.VirtualBank.model.dto.UserDto;
import com.example.VirtualBank.model.entity.User;
import com.example.VirtualBank.repository.UserRepository;
import com.example.VirtualBank.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")

@AllArgsConstructor
public class UserController {
private final UserService userService;

@GetMapping
public ResponseEntity <List<UserDto>> getAllUsers(){
  List<UserDto> userList= userService.getAllUsers();
  return ResponseEntity.ok(userList);
}
@PostMapping
public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
}


@DeleteMapping("/{userId}")
public ResponseEntity <Map<String,String>> deletUser(@PathVariable String userId){
    UUID userid1=UUID.fromString(userId);
    Boolean deleted=userService.deleteUser(userid1);
    Map<String,String>response=new HashMap<>();
    if(deleted){
        response.put("message","user deleted succesufully");
        return ResponseEntity.ok(response);
    }
    else{
        response.put("message","user did not deleted");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.updateUser(userId, userDto));
    }



}
