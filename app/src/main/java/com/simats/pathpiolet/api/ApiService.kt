package com.simats.pathpiolet.api

import com.simats.pathpiolet.data.College
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

interface ApiService {
    @GET("api/colleges")
    fun getColleges(): Call<List<College>>

    @POST("api/colleges/save")
    fun saveCollege(@Body request: SaveCollegeRequest): Call<AuthResponse>

    @DELETE("api/colleges/save/{user_id}/{college_id}")
    fun unsaveCollege(
        @retrofit2.http.Path("user_id") userId: Int,
        @retrofit2.http.Path("college_id") collegeId: Int
    ): Call<AuthResponse>

    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("api/auth/register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @POST("api/auth/verify-signup")
    fun verifySignup(@Body request: VerifyOtpRequest): Call<AuthResponse>

    @GET("api/auth/profile/{user_id}")
    fun getProfile(@retrofit2.http.Path("user_id") userId: Int): Call<UserData>

    @PUT("api/auth/profile/{user_id}")
    fun updateProfile(
        @retrofit2.http.Path("user_id") userId: Int,
        @Body request: UpdateProfileRequest
    ): Call<AuthResponse>

    @retrofit2.http.Multipart
    @POST("api/auth/profile/{user_id}/photo")
    fun uploadProfilePhoto(
        @retrofit2.http.Path("user_id") userId: Int,
        @retrofit2.http.Part photo: okhttp3.MultipartBody.Part
    ): Call<AuthResponse>

    @DELETE("api/auth/profile/{user_id}/photo")
    fun deleteProfilePhoto(@retrofit2.http.Path("user_id") userId: Int): Call<AuthResponse>

    @GET("api/events/{user_id}")
    fun getEvents(@retrofit2.http.Path("user_id") userId: Int): Call<List<EventData>>

    @POST("api/events")
    fun addEvent(@Body request: EventRequest): Call<AuthResponse>

    @DELETE("api/events/{event_id}")
    fun deleteEvent(@retrofit2.http.Path("event_id") eventId: Int): Call<AuthResponse>

    @POST("api/auth/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<AuthResponse>

    @POST("api/auth/verify-otp")
    fun verifyOtp(@Body request: VerifyOtpRequest): Call<AuthResponse>

    @GET("api/activity/{user_id}")
    fun getActivityHistory(@retrofit2.http.Path("user_id") userId: Int): Call<List<ActivityItem>>

    @POST("api/auth/reset-password")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<AuthResponse>

    @POST("api/auth/change-password")
    fun changePassword(@Body request: ChangePasswordRequest): Call<AuthResponse>

    @POST("api/recommendations")
    fun getRecommendations(@Body request: RecommendationsRequest): Call<List<College>>

    @POST("api/feedback")
    fun sendFeedback(@Body request: FeedbackRequest): Call<AuthResponse>

    @POST("api/rate")
    fun rateApp(@Body request: RatingRequest): Call<AuthResponse>

    @POST("api/contact")
    fun contactUs(@Body request: ContactRequest): Call<AuthResponse>
}
