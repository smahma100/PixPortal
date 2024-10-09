package com.saberdot.photoapp.album.controllers;


import com.saberdot.photoapp.album.data.AlbumEntity;
import com.saberdot.photoapp.album.service.AlbumsService;
import com.saberdot.photoapp.album.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.TypeToken;

@RestController
@RequestMapping("/users/{id}/albums")
public class AlbumsController {

    @Autowired
    AlbumsService albumsService;

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_ATOM_XML_VALUE,
            })
    public List<AlbumResponseModel> userAlbums(@PathVariable String id) {

        List<AlbumResponseModel> returnValue = new ArrayList<>();
        List<AlbumEntity> albumsEntities = albumsService.getAlbum(id);

        if (albumsEntities == null || albumsEntities.isEmpty()) {
            return returnValue;
        }

        Type listType = new TypeToken<List<AlbumResponseModel>>() {
        }.getType();
        return new ModelMapper().map(albumsEntities, listType);

    }

}
