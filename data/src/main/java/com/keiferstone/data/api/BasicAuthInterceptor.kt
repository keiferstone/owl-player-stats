package com.keiferstone.data.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BasicAuthInterceptor(username: String, password: String) : Interceptor {
  private val credentials: String = Credentials.basic(username, password)

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val authenticatedRequest = chain.request()
      .newBuilder()
      .header("Authorization", credentials)
      .build()
    return chain.proceed(authenticatedRequest)
  }
}