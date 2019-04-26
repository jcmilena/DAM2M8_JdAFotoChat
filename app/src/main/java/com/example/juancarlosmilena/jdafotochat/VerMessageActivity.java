package com.example.juancarlosmilena.jdafotochat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StreamDownloadTask;

public class VerMessageActivity extends AppCompatActivity {

    ImageView fotoViewer;
    TextView fotoMsg;
    String fotoName, mensaje , fotoURL;
    FirebaseStorage storage;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_message);

        storage = FirebaseStorage.getInstance();

        mensaje = getIntent().getExtras().getString("mensaje");
        fotoURL = getIntent().getExtras().getString("fotoURL");
        Log.i("FOTOURL", fotoURL);

        fotoViewer = findViewById(R.id.FotoViewer);
        fotoMsg = findViewById(R.id.msgTextView);

        fotoMsg.setText(mensaje);



        //Este código permite descargar una imagen desde Firebase Storage
        //sin necesidad de crear un thread adicional.
        //Descargo apuntando a una referencia de Storage (sirve la URL)
        //y pido que descargue una array de Bytes
        //Al ser un thread generado automáticamente debo implementar un Listener
        //que me indique que se ha acabado la tarea con éxito
        //En acabar con éxito recibo el array de bytes con la imagen.
        storage.getReferenceFromUrl(fotoURL).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {


                bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                fotoViewer.setImageBitmap(bm);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //En este modo entro cuando pulso el botón de atrás en el movil
        //Aquí debería escribir el código para borrar el mensaje una vez visualizado
    }
}
