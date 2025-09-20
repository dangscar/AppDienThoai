package com.nlhd.domain.usecase.authentication

import android.content.Context
import android.net.Uri
import com.nlhd.domain.repository.AuthenticationRepository

class UploadAvatar(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(token: String, context: Context, uri: Uri) = repository.uploadAvatar(token, context, uri)
}