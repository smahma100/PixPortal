package com.saberdot.photoapp.album.service;

import com.saberdot.photoapp.album.data.AlbumEntity;

import java.util.List;

public interface AlbumsService {

    List<AlbumEntity> getAlbum(String userId);
}
