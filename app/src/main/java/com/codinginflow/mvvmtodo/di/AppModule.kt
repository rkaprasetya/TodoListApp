package com.codinginflow.mvvmtodo.di

import android.app.Application
import androidx.room.Room
import com.codinginflow.mvvmtodo.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    // Use provides because we dont own the class, like Room class
    // if the class is ours, we use inject
    @Provides
    @Singleton
    fun provideDatabase(app: Application, callback: TaskDatabase.Callback) =
        Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback) // everytime database created, there is preloaded with dummy data, for a preview.
            // This callback handle them
            .build()

    // no need to add singleton on dao
    // because room already always create only one dao
    // it is the Room policy
    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    // By Default, Coroutine will be cancelled if on of the operations fail(all other will be cancelled too)
    // this supervisor job make sure the other operations does not fail too if one fails
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

// if we want to create 2 different objects from 1 object(in this case CoroutineScope),
// we differ it using qualifier
// to diminish ambiguity causing by the same name
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope