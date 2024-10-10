package com.saberdot.photoapp.api.users.data;

import com.saberdot.photoapp.api.users.ui.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ALBUMS-WS")
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albumss")
    List<AlbumResponseModel> getAlbums(@PathVariable("id") String id);
}
