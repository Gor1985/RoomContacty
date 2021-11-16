package com.android.mycontacts;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts_table")// создаем аннотацию называем таблицу
public class Contact {

    @PrimaryKey(autoGenerate = true)// создаем аннотацию ставим автогенерацию ид т.к все будет динамично по первому чтобцу
    private long contactId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @Ignore// создаем аннотацию потому что в рум можно использовать только один заполненный конструктор
    public Contact() {
    }

    public Contact(long contactId, String firstName, String lastName,
                   String email, String phoneNumber) {
        this.contactId = contactId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
// создаем сеттеры и геттеры
    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
