package com.example.juancarlosmilena.jdafotochat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FotoChatsActivity extends AppCompatActivity {

    ListView chatsListView;
    ArrayAdapter<String> miAdapter;
    ArrayList<String> emails = new ArrayList<>();
    List<Message> fotochats = new ArrayList<>();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotochats);

        mAuth = FirebaseAuth.getInstance();

        cargar_emails_multimedia();

        miAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1 ,emails);

        chatsListView = findViewById(R.id.chatsListView);
        chatsListView.setAdapter(miAdapter);

        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), VerMessageActivity.class);
                intent.putExtra("fotoURL", fotochats.get(position).getUrl());
                intent.putExtra("fotoName", fotochats.get(position).getFotoname());
                intent.putExtra("mensaje", fotochats.get(position).getMsg());
                startActivity(intent);
            }
        });
    }

    //Esta función se encarga de leer de Firebase Realtime DB y genera el listado
    //con la información necesaria, en este caso leo por un lado los emails (porque uso listview)
    //y por otro lado leo toda la información del mensaje.
    private void cargar_emails_multimedia() {

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid())
                .child("multimedia")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        emails.add(dataSnapshot.child("fromUser").getValue(String.class));
                        miAdapter.notifyDataSetChanged();
                        fotochats.add(dataSnapshot.getValue(Message.class));

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_msg:
                Intent intent = new Intent(this, NewMessageActivity.class);
                intent.putExtra("EMAIL", mAuth.getCurrentUser().getEmail());
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
