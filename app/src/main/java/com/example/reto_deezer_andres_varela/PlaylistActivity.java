package com.example.reto_deezer_andres_varela;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends Activity {


    private RecyclerView listaCanciones;
    private AdapterCanciones adapterCanciones;
    private ImageView imagen_playlist;
    private TextView txt_nombre_playlist;
    private TextView txt_descripcion;
    private TextView txt_canciones;
    private TextView txt_fans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        String applicationID = "348804";
        final DeezerConnect deezerConnect = new DeezerConnect(this, applicationID);

        imagen_playlist=findViewById(R.id.imagen_playlist);
        txt_nombre_playlist=findViewById(R.id.titulo_playlist);
        txt_descripcion =findViewById(R.id.descripcion_playlist);
        txt_canciones = findViewById(R.id.numero_canciones);
        txt_fans=findViewById(R.id.numero_fans);

        adapterCanciones=new AdapterCanciones();
        listaCanciones=findViewById(R.id.lista_canciones_playlist_activity);

        listaCanciones.setLayoutManager(new LinearLayoutManager(this));
        listaCanciones.setAdapter(adapterCanciones);
        listaCanciones.setHasFixedSize(true);

        String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY };

        ActivityCompat.requestPermissions(this,permissions,0);


        final RequestListener rListener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                Playlist play = (Playlist)result;
                List<Track> canciones =  play.getTracks();
                adapterCanciones=new AdapterCanciones();
                listaCanciones.setAdapter(adapterCanciones);
                adapterCanciones.showAllCanciones(canciones);
                Glide.with(PlaylistActivity.this).load(play.getBigImageUrl()).into(imagen_playlist);
                txt_nombre_playlist.setText(play.getTitle());
                txt_descripcion.setText(play.getDescription());
                txt_canciones.setText("Pistas: "+play.getTracks().size());
                txt_fans.setText(" Fans: "+play.getFans());

            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}

            public void onException(Exception e, Object requestId) {}
        };


        long id = (long) getIntent().getExtras().get("id");

        DeezerRequest request = DeezerRequestFactory.requestPlaylist(id);
        deezerConnect.requestAsync(request,rListener);
        request.setId("busquedacanciones");
    }
}
