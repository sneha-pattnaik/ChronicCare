package com.example.chroniccare.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DrGPTApiService {
    @POST("api/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest request);
    
    @GET("api/chat/{session_id}")
    Call<ChatHistoryResponse> getHistory(@Path("session_id") String sessionId);
    
    @DELETE("api/chat/{session_id}")
    Call<StatusResponse> deleteSession(@Path("session_id") String sessionId);
    
    @GET("api/booking/slots")
    Call<BookingSlotsResponse> getSlots(
        @Query("specialty") String specialty,
        @Query("days") int days
    );
}
