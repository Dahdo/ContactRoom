package com.dahdotech.contactroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dahdotech.contactroom.adapter.RecyclerViewAdapter;
import com.dahdotech.contactroom.model.Contact;
import com.dahdotech.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {
    public static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private ContactViewModel contactViewModel;
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication()).create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, contacts -> {
            //setup adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, this, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });


        fab = findViewById(R.id.add_conctact_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            assert data != null;
            String name = data.getStringExtra(NewContact.NAME_REPLY);
            String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);

            Contact contact = new Contact(name, occupation);
            ContactViewModel.insert(contact);
        }
    }

    @Override
    public void onContactClick(int postion) {
        Contact contact = Objects.requireNonNull(contactViewModel.getAllContacts().getValue().get(postion));
        Log.d("Clicked","onCreateClick: " + contact.getName());
        startActivity(new Intent(MainActivity.this, NewContact.class));
    }
}