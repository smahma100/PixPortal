package com.saberdot.photoapp.api.users.shared;

import com.saberdot.photoapp.api.users.ui.model.AlbumResponseModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -953297098295050686L;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userId;  // Corrected capitalization
    private String encryptedPassword;
    private List<AlbumResponseModel> albums;

    // Getter and setter for firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public List<AlbumResponseModel> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumResponseModel> albums) {
        this.albums = albums;
    }
}
