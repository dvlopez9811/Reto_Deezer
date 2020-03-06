package com.example.reto_deezer_andres_varela;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {



private RecyclerView listaPlaylists;
private AdapterPlaylists adapterPlaylists;
private Button btn_buscar;
private EditText txt_busqueda_playlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String applicationID = "348804";
        final DeezerConnect deezerConnect = new DeezerConnect(this, applicationID);

        adapterPlaylists= new AdapterPlaylists();

        txt_busqueda_playlist=findViewById(R.id.barra_buscar);
        listaPlaylists = findViewById(R.id.lista_listas_reproduccion);
        listaPlaylists.setLayoutManager(new LinearLayoutManager(this));
        listaPlaylists.setAdapter(adapterPlaylists);
        listaPlaylists.setHasFixedSize(true);
        btn_buscar=findViewById(R.id.btn_buscar);

        // The set of Deezer Permissions needed by the app

        String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY };

        ActivityCompat.requestPermissions(this,permissions,0);

// The listener for authentication events
        DialogListener listener = new DialogListener() {

            public void onComplete(Bundle values) {}

            public void onCancel() {}

            public void onException(Exception e) {}
        };


// Launches the authentication process deezerConnect.authorize(activity, permissions, listener);
        // the request listener
        final RequestListener rListener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                List<Playlist> playlists = (List<Playlist>) result;
                adapterPlaylists=new AdapterPlaylists();
                listaPlaylists.setAdapter(adapterPlaylists);
                adapterPlaylists.showAllPlaylists(playlists);
                // do something with the albums
            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}

            public void onException(Exception e, Object requestId) {}
        };

// create the request


        DeezerRequest request = DeezerRequestFactory.requestSearchPlaylists("Pop");
        request.setId("busquedaplaylist");
        deezerConnect.requestAsync(request,rListener);

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buscada = txt_busqueda_playlist.getText().toString();
                if(!buscada.equals("")){
                    DeezerRequest request = DeezerRequestFactory.requestSearchPlaylists(buscada);
                    deezerConnect.requestAsync(request,rListener);
                    request.setId("busquedaplaylis");
                }
// launch the request asynchronously deezerConnect.requestAsync(request, listener);
            }
        });


    }
}
