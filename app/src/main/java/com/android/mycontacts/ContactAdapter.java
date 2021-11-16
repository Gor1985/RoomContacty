package com.android.mycontacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mycontacts.databinding.ContactListItemBinding;

import java.util.ArrayList;

public class ContactAdapter
        extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ArrayList<Contact> contactArrayList = new ArrayList<>();// создаем аррайлист для ресейклер
    private MainActivity mainActivity;// создаем переменную нашего класса майн активити

    public ContactAdapter(ArrayList<Contact> contactArrayList,
                          MainActivity mainActivity) {//создаем конструктор нашего конткт адаптера
        this.contactArrayList = contactArrayList;
        this.mainActivity = mainActivity;
    }

    public void setContactArrayList(ArrayList<Contact> contactArrayList) {//
        this.contactArrayList = contactArrayList;// инициализируем
        notifyDataSetChanged();// говорим адаптеру что обновили список
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.contact_list_item, parent, false);
//
//        return new ContactViewHolder(itemView);

        ContactListItemBinding contactListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),// раздуваем наш контакт листи итем
                R.layout.contact_list_item,
                parent,
                false
        );

        return new ContactViewHolder(contactListItemBinding);//возвращаем наш холдер с раздутым контакт листом
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
//делаем привязку кладываем к нее наш холдер и финальную позицию
        final Contact contact = contactArrayList.get(position);
// загружаем в переменную позиция аррай листа


        holder.contactListItemBinding.setContact(contact);
//вкладывыаем в нашу привязку этот контакт по позиции
        holder.itemView.setOnClickListener(new View.OnClickListener() {// создаем слушатель итемов
            @Override
            public void onClick(View view) {
                mainActivity.addAndEditContact(true, contact, position);
                // тут делаем привязку к майн активити и ставим  на наш диалог тру
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }// тут мы  передаем размер аррай листа

    class ContactViewHolder extends RecyclerView.ViewHolder {
// создаем класс и наследуемся от холдера
//        private TextView firstNameTextView;
//        private TextView lastNameTextView;
//        private TextView emailTextView;
//        private TextView phoneNumberTextView;

        private ContactListItemBinding contactListItemBinding;
// создаем переменную контакт лист  для отправки в ресейклер виев

        public ContactViewHolder(@NonNull ContactListItemBinding contactListItemBinding) {
// создаем конструктор класса
            super(contactListItemBinding.getRoot());
// инициализируем переменную
            this.contactListItemBinding = contactListItemBinding;

//            firstNameTextView = itemView.findViewById(R.id.firstNameTextView);
//            lastNameTextView = itemView.findViewById(R.id.lastNameTextView);
//            emailTextView = itemView.findViewById(R.id.emailTextView);
//            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);


        }
    }


}
