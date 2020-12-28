package com.codinginflow.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    // Provider<Class> is used on circular dependency
    // it acts like a Lazy. it will created later, not rightaway
    class Callback @Inject constructor(private val database: Provider<TaskDatabase>,
    @ApplicationScope private val applicationScope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //db operations for preloaded data for preview
            val dao = database.get().taskDao()
            applicationScope.launch {
                dao.insert(Task(name = "Wash the dishes"))
                dao.insert(Task(name = "Do laundry"))
                dao.insert(Task(name = "Cook Dinner"))
                dao.insert(Task(name = "Wash my plane", important = true))
                dao.insert(Task(name = "Pray", important = true))
                dao.insert(Task(name = "Be grateful", completed = true))
            }
        }
    }
}