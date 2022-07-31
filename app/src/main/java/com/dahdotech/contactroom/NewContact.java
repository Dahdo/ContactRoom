package com.dahdotech.contactroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dahdotech.contactroom.model.Contact;
import com.dahdotech.contactroom.model.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NewContact extends AppCompatActivity {
    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION_REPLY = "occupation_reply";
    private EditText enterName;
    private EditText enterOccupation;
    private Button saveInfoButton;
    private ContactViewModel contactViewModel;
    private int contactId = 0;
    private boolean isEdit = false;
    Button updateButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        enterName = findViewById(R.id.enter_name);
        enterOccupation = findViewById(R.id.enter_occupation);
        saveInfoButton = findViewById(R.id.save_button);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContact.this
                .getApplication()).create(ContactViewModel.class);

        if(getIntent().hasExtra(MainActivity.CONTACT_ID)){
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            contactViewModel.get(contactId).observe(this, contact -> {
                if(contact != null){
                    enterName.setText(contact.getName());
                    enterOccupation.setText(contact.getOccupation());
                }
            });
            isEdit = true;
        }


        saveInfoButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if(!TextUtils.isEmpty(enterName.getText())
                    && !TextUtils.isEmpty(enterOccupation.getText())){
                String name = enterName.getText().toString();
                String occupation = enterOccupation.getText().toString();
                replyIntent.putExtra(NAME_REPLY, name);
                replyIntent.putExtra(OCCUPATION_REPLY, occupation);
                setResult(RESULT_OK, replyIntent);

            }
            else {
                setResult(RESULT_CANCELED, replyIntent);
            }
            finish();
        });
        //update and delete button
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
        updateButton.setOnClickListener(view -> edit(false));

        deleteButton.setOnClickListener(view -> edit(true));

        if(isEdit){
            saveInfoButton.setVisibility(View.GONE);
        }
        else {
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void edit(boolean isDelete) {
        String name = enterName.getText().toString().trim();
        String occupation = enterOccupation.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)){
            Snackbar.make(enterName, R.string.empty, Snackbar.LENGTH_SHORT).show();
        }
        else{
            Contact contact = new Contact();
            contact.setId(contactId);
            contact.setName(name);
            contact.setOccupation(occupation);
            if(isDelete)
                ContactViewModel.delete(contact);
            else
                ContactViewModel.update(contact);
            finish();
        }
    }
}