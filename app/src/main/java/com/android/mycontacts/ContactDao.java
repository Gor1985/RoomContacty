package com.android.mycontacts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao// это аннотация из рум помогающая управлять нашей бд
public interface ContactDao {

    @Insert// метод для добавления
    void insertContact(Contact contact);

    @Query("SELECT * FROM contacts_table")// метод для вытаскивания всех контактов
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contacts_table WHERE contactId ==:contactId")// метод для вытаскивания определенного контакта
    // в данном случае по стобцу сонтактид
    Contact getContact(long contactId);

    @Update// метод для изменения контакта
    void updateContact(Contact contact);

    @Delete// метод для удаления контакта
    void deleteContact(Contact contact);

}
