package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class FindUserData {
    @SerializedName("email")
    private String email;

    public FindUserData(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
