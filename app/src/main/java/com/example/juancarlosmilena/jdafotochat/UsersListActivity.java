package com.example.juancarlosmilena.jdafotochat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersListActivity extends AppCompatActivity {

    ListView miListView;
    ArrayList<String> emailsListado;
    Message msg;
    ArrayList<String> uids;
    String uid;

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        msg = new Message(getIntent().getExtras().getString("fromName"),
                getIntent().getExtras().getString("downloadURL"),
                getIntent().getExtras().getString("mensaje"),
                getIntent().getExtras().getString("name"));

        miListView = findViewById(R.id.listview);

        emailsListado = new ArrayList<>();
        uids = new ArrayList<>();


        //El siguiente código lee los email que tengo en Firebase Realtime DB
        //En este caso también quiero leer los UID, y como forman parte de la clave
        //los leo mediante getKey()
        DatabaseReference ref = database.getReference().child("users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String email;
                email = dataSnapshot.child("email").getValue(String.class);
                emailsListado.add(email);
                uids.add(dataSnapshot.getKey());
                uid = dataSnapshot.getKey();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        ArrayAdapter<String> miAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, emailsListado);

        miListView.setAdapter(miAdapter);

        miListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Escribir fotoMensaje en Firebase

                database.getReference().child("users").child(uids.get(position))
                        .child("multimedia").push().setValue(msg);


            }
        });




    }
}
