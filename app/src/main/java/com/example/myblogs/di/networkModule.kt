package com.example.myblogs.di

import com.example.myblogs.api.BlogifyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.myblogs.utils.Constants.BASE_URL
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

@Module
@InstallIn(SingletonComponent::class)
class networkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): BlogifyApi{
        return retrofit.create(BlogifyApi::class.java)
    }

//    @Singleton
//    @Provides
//    fun firebaseStorage(){
//        val storage = Firebase.storage
//        // Create a storage reference from our app
//        var storageRef = storage.reference
//
//
//        var imagesRef: StorageReference? = storageRef.child("images")
//    }
}