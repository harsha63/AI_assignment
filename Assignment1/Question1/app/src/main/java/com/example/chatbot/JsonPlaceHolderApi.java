package com.example.chatbot;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @GET("send")
    Call<List<Post>> getPosts();

    @POST("posts")
    Call<Post>createPost(@Body Post post);
}
