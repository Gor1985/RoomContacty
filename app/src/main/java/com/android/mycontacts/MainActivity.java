package com.android.mycontacts;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mycontacts.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyContactsDatabase myContactsDatabase;// создаем переменную бд
    private ArrayList<Contact> contactArrayList = new ArrayList<>();// создаем араайлист
    private ContactAdapter contactAdapter;// создаем  переменную нашего рециклер виев

    private ActivityMainBinding activityMainBinding;// создаем переменную для датабиндинг
    private MainActivityButtonHandler buttonHandler;//создаем переменную для отправки в очередь сообщений

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);// связываем наш тулбар
        setSupportActionBar(toolbar);// включаем его

        activityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);// создаем наш датабиндинг
        buttonHandler = new MainActivityButtonHandler(this);// добавляем ему контекст
        activityMainBinding.setButtonHandler(buttonHandler);// проводим очередь сообщений в баттон хандлер


        RecyclerView recyclerView = activityMainBinding.recyclerView;// связываем наш ресайклер виев
        recyclerView.setLayoutManager(new LinearLayoutManager (this));// связываем с менеджером(говорим сто он линейный)
        recyclerView.setHasFixedSize(true);// фиксируем

        contactAdapter = new ContactAdapter(contactArrayList, MainActivity.this);// вкладваем в нашу переменную
        // аррай лист и отсулку что он принадлежит майн активити
        recyclerView.setAdapter(contactAdapter);// вкладываем это все в адаптер

        myContactsDatabase = Room.databaseBuilder(getApplicationContext(),//связываем нашу переменную с рум
                MyContactsDatabase.class, "ContactsDB").build();//даем имя нашей бд

        loadContacts();// загружаем контакты

        new ItemTouchHelper (new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contact contact =
                        contactArrayList.get(viewHolder.getAdapterPosition());
                deleteContact(contact);
            }
        }).attachToRecyclerView(recyclerView);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addAndEditContact(false, null, -1);
//            }
//        });
    }

    public void addAndEditContact(final boolean isUpdate, final Contact contact,
                                  final int position) {// что бы понимать добавляем или обновляем контакт
        LayoutInflater layoutInflater = LayoutInflater
                .from(getApplicationContext());// раздуваем нашу разметку с добавлением контакта
        View view = layoutInflater.inflate(R.layout.add_edit_contact, null);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);// добавляем нашу разметку вью

        TextView contactTitleTextView =
                view.findViewById(R.id.contactTitleTextView);// связываем нашу разметку
        final EditText firstNameEditText =
                view.findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText =
                view.findViewById(R.id.lastNameEditText);
        final EditText emailEditText =
                view.findViewById(R.id.emailEditText);
        final EditText phoneNumberEditText =
                view.findViewById(R.id.phoneNumberEditText);

        contactTitleTextView.setText(!isUpdate ? "Add Contact" : "Edit contact");
// если наша кнопка не равна изменению  выходят два сообщения
        if (isUpdate && contact != null) {// если контакт и добавление не равно нулю
            firstNameEditText.setText(contact.getFirstName());// добавляем наши данные и изменяем
            lastNameEditText.setText(contact.getLastName());
            emailEditText.setText(contact.getEmail());
            phoneNumberEditText.setText(contact.getPhoneNumber());
        }

        builder.setCancelable(false)//что бф нельзя было изменить
                .setPositiveButton(isUpdate ? "Update" : "Save",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i) {
                                if (TextUtils.isEmpty(firstNameEditText.getText()// проверяем наши пстроки на пустоту
                                        .toString())) {
                                    Toast.makeText(MainActivity.this,
                                            "Enter first name",
                                            Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(lastNameEditText.getText()
                                        .toString())) {
                                    Toast.makeText(MainActivity.this,
                                            "Enter last name",
                                            Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(emailEditText.getText()
                                        .toString())) {
                                    Toast.makeText(MainActivity.this,
                                            "Enter email",
                                            Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(phoneNumberEditText.getText()
                                        .toString())) {
                                    Toast.makeText(MainActivity.this,
                                            "Enter phone number",
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    if (isUpdate && contact != null) {
                               // если изменение и контакты не равну нулю
                                        updateContact(// изменяем контакты
                                                firstNameEditText.getText().toString(),
                                                lastNameEditText.getText().toString(),
                                                emailEditText.getText().toString(),
                                                phoneNumberEditText.getText().toString(),
                                                position);

                                    } else {

                                        addContact(// добавляем контакты
                                                firstNameEditText.getText().toString(),
                                                lastNameEditText.getText().toString(),
                                                emailEditText.getText().toString(),
                                                phoneNumberEditText.getText().toString()
                                        );

                                    }

                                }
                            }
                        });

        AlertDialog alertDialog = builder.create();// создаем диалог
        alertDialog.show();// расшариваем
    }

    private void loadContacts() {
// загрузка контактов
        new GetAllContactsAsyncTask().execute();// вытаскиваем контакты из загрузчика

    }

    private void deleteContact(Contact contact) {
        new DeleteContactAsyncTask().execute(contact);
    }
// вытаскиваем запрос на удаление из загрузчика
    private void addContact(String firstName, String lastName,// добавляем контакт
                            String email, String phoneNumber) {

        Contact contact = new Contact(// создаем новый обьект ставим ид 0 т.к у нас автогенерация ид(создаем новый контакт)
                0,
                firstName,
                lastName,
                email,
                phoneNumber
        );

        new AddContactAsyncTask().execute(contact);// вытаскиваем методом из загрузчика
    }

    private void updateContact(String firstName, String lastName,// создаем метод для изменения контакта
                               String email, String phoneNumber, int position) {
        Contact contact = contactArrayList.get(position);// вкладываем контакт обьект контакт из араай листа по его позиции

        contact.setFirstName(firstName);///изменяем
        contact.setLastName(lastName);
        contact.setEmail(email);
        contact.setPhoneNumber(phoneNumber);

        new UpdateContactAsyncTask().execute(contact);// вытаскиваем изменение из обработчика

        contactArrayList.set(position, contact);// находим позицию в аррайлисте в контакте
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {// раздувакем нашу менюшку
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetAllContactsAsyncTask extends AsyncTask<Void, Void, Void> {
// создаем метод для показа всех контактов
        @Override
        protected Void doInBackground(Void... voids) {
//вытаскиваем все контакты из нашей рум(бд)
            contactArrayList = (ArrayList<Contact>) myContactsDatabase
                    .getContactDao().getAllContacts();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactAdapter.setContactArrayList(contactArrayList);//загружаем наш лист из бд
        }
    }

    private class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {


        @Override
        protected Void doInBackground(Contact... contacts) {

            myContactsDatabase.getContactDao().deleteContact(contacts[0]);
// удаляем по позиции
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadContacts();// загружаем контакты
        }
    }

    private class AddContactAsyncTask extends AsyncTask<Contact, Void, Void> {


        @Override
        protected Void doInBackground(Contact... contacts) {

            myContactsDatabase.getContactDao().insertContact(contacts[0]);
/// добавляем по позиции
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
// загружаем контакты
            loadContacts();
        }
    }

    private class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {


        @Override
        protected Void doInBackground(Contact... contacts) {

            myContactsDatabase.getContactDao().updateContact(contacts[0]);
// изменяем контакты
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//загружаем контакты
            loadContacts();
        }
    }

    public class MainActivityButtonHandler {

        Context context;// создаем переменную для контеста

        public MainActivityButtonHandler(Context context) {
            this.context = context;
        }
// создаем конструктор
        public void onButtonClicked(View view)
        { addAndEditContact(false, null, -1); }
        //при клике мы ставим наш флаг загрузки на фолз так как мы добавляем контакт
        // налл так как мы не редактируем обьект
        //-1 так как в данном слдучае у нас нет позиции(его нет)
    }

}
