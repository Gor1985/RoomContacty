package com.android.mycontacts;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1)//
// создаем аннотация датабейс где инициализируем нашу бд и ее версию
public abstract class MyContactsDatabase extends RoomDatabase {
// наследуемся от рум
    public abstract ContactDao getContactDao();
// создаем абстрактный метод как прокладку для управления методами
}
