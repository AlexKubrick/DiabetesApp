package ru.alexkubrick.android.diabetesapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.SugarData

@Database(entities = [SugarData::class], version = 3)
@TypeConverters(SugarDataTypeConverters::class)
abstract class SugarDatabase : RoomDatabase() {
    abstract fun sugarDataDao(): SugarDataDao
}

val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE SugarData ADD COLUMN measurementTime INTEGER NOT NULL DEFAULT 9"
        )
    }

}