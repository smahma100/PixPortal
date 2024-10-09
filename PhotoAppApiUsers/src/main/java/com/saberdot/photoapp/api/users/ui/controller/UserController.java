package com.saberdot.photoapp.api.users.ui.controller;

import com.saberdot.photoapp.api.users.service.UsersService;
import com.saberdot.photoapp.api.users.shared.UserDto;
import com.saberdot.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.saberdot.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.saberdot.photoapp.api.users.ui.model.UserResponseModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Environment env;

    @Autowired
    UsersService userService;

    @GetMapping("/status/check")
    public String status() {

        return "Photo-App-Api-Users is working on port " + env.getProperty("local.server.port") +
                ", with token= " + env.getProperty("token.secret");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel useDetails) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(useDetails, UserDto.class);
        UserDto createUser = userService.createUser(userDto);


        CreateUserResponseModel returnValue = modelMapper.map(createUser, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping(value = "/{userId}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_ATOM_XML_VALUE,
            })
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {

        UserDto userDto = userService.getUserByUserId(userId);
        UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
