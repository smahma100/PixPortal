package com.saberdot.photoapp.api.users.service;


import com.saberdot.photoapp.api.users.data.AlbumsServiceClient;
import com.saberdot.photoapp.api.users.data.UserEntity;
import com.saberdot.photoapp.api.users.data.UserRepository;
import com.saberdot.photoapp.api.users.shared.UserDto;
import com.saberdot.photoapp.api.users.ui.model.AlbumResponseModel;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImp implements UsersService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    // RestTemplate restTemplate;
    Environment environment;
    AlbumsServiceClient albumsServiceClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          RestTemplate restTemplate,
                          Environment environment,
                          AlbumsServiceClient albumsServiceClient) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        this.albumsServiceClient = albumsServiceClient;
    }


    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(
                bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) throw new UsernameNotFoundException(username);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(userEntity, UserDto.class);

    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("user not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        // Fetch albums using RestTemplate
       /* String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(
                albumsUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AlbumResponseModel>>() {
                }
        );

        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
*/


/*
        List<AlbumResponseModel> albumsList = null;
        try {
            albumsList = albumsServiceClient.getAlbums(userId);
        } catch (FeignException e) {
            logger.error(e.getLocalizedMessage());
        }
 */

        logger.debug("before album");
        List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
        logger.debug("after album");

        // Set the fetched albums to the UserDto
        userDto.setAlbums(albumsList);
        return userDto;
    }
}