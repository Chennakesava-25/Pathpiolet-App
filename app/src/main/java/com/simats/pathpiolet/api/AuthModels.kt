package com.simats.pathpiolet.api

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val phone: String,
    val age: Int
)

data class AuthResponse(
    val message: String? = null,
    val error: String? = null,
    val user: UserData? = null
)

data class UserData(
    val id: Int,
    val username: String,
    val email: String,
    val phone: String? = null,
    val age: Int? = null,
    val education_level: String? = null,
    val interested_field: String? = null
)

data class UpdateProfileRequest(
    val username: String,
    val phone: String,
    val age: Int,
    val education_level: String,
    val interested_field: String
)

data class EventData(
    val id: Int,
    val user_id: Int,
    val title: String,
    val description: String?,
    val event_date: String,
    val time: String?
)

data class EventRequest(
    val user_id: Int,
    val title: String,
    val description: String,
    val event_date: String,
    val time: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class ResetPasswordRequest(
    val email: String,
    val password: String
)

data class SaveCollegeRequest(
    val user_id: Int,
    val college_id: Int
)

data class ActivityItem(
    val title: String,
    val subtitle: String,
    val time: String,
    val type: String
)

data class ChangePasswordRequest(
    val user_id: Int,
    val current_password: String,
    val new_password: String
)

data class RecommendationsRequest(
    val location: String,
    val budget: Int,
    val collegeTypes: List<String>,
    val hostel: Boolean,
    val placementPriority: String,
    val campusSize: String,
    val examScore: String,
    val specializations: List<String>
)

data class FeedbackRequest(
    val user_id: Int,
    val message: String
)

data class RatingRequest(
    val user_id: Int,
    val rating: Int
)

data class ContactRequest(
    val user_id: Int,
    val subject: String,
    val message: String
)
