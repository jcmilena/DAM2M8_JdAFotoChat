package com.example.juancarlosmilena.jdafotochat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class NewMessageActivity extends AppCompatActivity {

    Button galeria, subir;
    ImageView imagen;
    EditText msgEditText;
    Uri uri;
    UUID uuid;
    String fromEmail;
    String fotoname;
    String fotomsg;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        fromEmail = getIntent().getExtras().getString("EMAIL");

        Log.i("FIREBASE", storage.getReference().child("fotos").child("prueba.jpg").getDownloadUrl().toString());


        galeria = findViewById(R.id.galeriaButton);
        subir = findViewById(R.id.subirButton);
        imagen = findViewById(R.id.FotoViewer);
        msgEditText = findViewById(R.id.msgEditText);

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Escoger imagen galeria
                cargar_imagen_galeria();


            }
        });

        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uuid = UUID.randomUUID();
                fotomsg = msgEditText.getText().toString();

                //Subir un fichero a FirebaseStorage requiere definir donde
                //lo queremos guardar y necesito generar una referencia
                final StorageReference storageRef = storage.getReference()
                        .child("fotos").child(uuid+".jpg");


                //Una vez creada la referencia necesito generar una Tarea
                //que se encargue de subir el fichero, esta tarea genera
                //un thread y necesito añadirle un Listener que me avise
                //cuando la tarea(thread) ha finalizado
                //Para subir el fichero utilizamos la Uri con el camino
                //de la imagen dentro de mi telefono
                UploadTask uploadTask;
                uploadTask = storageRef.putFile(uri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();
                            Log.i("FIREBASE2", downloadUri.toString());

                            //Paso a la activity de seleccionar el usuario

                            Intent intent = new Intent(getApplicationContext(), UsersListActivity.class);
                            intent.putExtra("downloadURL", downloadUri.toString());
                            intent.putExtra("fromName", fromEmail);
                            intent.putExtra("name", uuid+".jpg");
                            intent.putExtra("mensaje", fotomsg);

                            startActivity(intent);



                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });



            }
        });




    }


    //Esta función genera un Intent Implícito pidiendo que queremos
    //seleccionar una imagen de la Memoria Externa, Android nos abrirà alguna App
    //que tenga acceso a las imagenes para seleccionar una
    private void cargar_imagen_galeria() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //Abro la petición de Intent pero esperando una respuesta
        //porque en seleccionar un imagen quiero volver a mi App y
        //quiero conocer que imagen se ha seleccionado, esta info la
        //recibimos en el metodo onActivityResult
        startActivityForResult(intent, 10);


    }


    //El SO Android llama automáticamente a esta función cuando
    //un Intent tiene que devolver una respuesta, en este caso concreto
    //la respuesta consistirá en la Uri de la imagen seleccionada.
    //la respuesta viene en un objeto llamado data.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;

        if (requestCode == 10 && resultCode == RESULT_OK) {


            uri = data.getData();

            try {

                //Puedo conseguir un bitmap directamente desde una Uri
                //Para ello utilizo la clase MediaStore.Images
                bitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), uri);


            } catch (Exception e) {
                e.printStackTrace();
            }


            if (bitmap != null) {
                imagen.setImageBitmap(bitmap);
            }


        }
    }
}
