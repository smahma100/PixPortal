package com.saberdot.photoapp.api.users.data;

import com.saberdot.photoapp.api.users.ui.model.AlbumResponseModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    @Retry(name = "albums-ws")
    @CircuitBreaker(name = "albums-ws", fallbackMethod = "getFallbackMethod")
    List<AlbumResponseModel> getAlbums(@PathVariable("id") String id);

   /*
    {
         throw new RuntimeException("Forced exception to test Circuit Breaker.");
    }
    */

    default List<AlbumResponseModel> getFallbackMethod(String id, Throwable exception) {
        System.out.println("Fallback method called for user ID: " + id);
        System.out.println("Exception occurred: " + exception.getMessage());
        return new ArrayList<>();
    }
}
