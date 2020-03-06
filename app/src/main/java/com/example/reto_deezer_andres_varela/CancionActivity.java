package com.example.reto_deezer_andres_varela;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;

import org.w3c.dom.Text;

import java.util.List;

public class CancionActivity extends Activity {


    private ImageView imagen_album;
    private TextView titulo_cancion;
    private TextView nombre_artista;
    private TextView nombre_album;


    private TrackPlayer trackPlayer;
    private TextView duracion;
    private Button btn_escuchar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion);

        final long id = (long) getIntent().getExtras().get("id");
        imagen_album = findViewById(R.id.imagen_cancion);
        titulo_cancion = findViewById(R.id.txt_nombre_cancion);
        nombre_artista = findViewById(R.id.txt_artista);
        nombre_album = findViewById(R.id.txt_album);
        duracion = findViewById(R.id.txt_duracion);
        btn_escuchar = findViewById(R.id.btn_escuchar);

        String applicationID = "348804";
        final DeezerConnect deezerConnect = new DeezerConnect(this, applicationID);
        String[] permissions = new String[]{
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY};

        ActivityCompat.requestPermissions(this, permissions, 0);

        final RequestListener rListener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {

                Track cancion = (Track) result;
                Glide.with(CancionActivity.this).load(cancion.getAlbum().getBigImageUrl()).into(imagen_album);
                titulo_cancion.setText(cancion.getTitle());
                nombre_artista.setText(cancion.getArtist().getName());
                nombre_album.setText("Álbum: " + cancion.getAlbum().getTitle());
                duracion.setText("Duración: "+tiempoMinutos(cancion.getDuration()));
            }
            public void onUnparsedResult(String requestResponse, Object requestId) {
            }

            public void onException(Exception e, Object requestId) {
            }
        };

        DeezerRequest request = DeezerRequestFactory.requestTrack(id);
        deezerConnect.requestAsync(request,rListener);
        request.setId("busquedacancion");

        try {
            trackPlayer = new TrackPlayer((Application) getApplicationContext(),deezerConnect,
                    new WifiAndMobileNetworkStateChecker());

        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        }

        btn_escuchar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackPlayer.playTrack(id);
            }
        });
    }

    private String tiempoMinutos(int segundos){
        int minutos=segundos/60;
        int seg = segundos-(60*minutos);
        if(seg<10)
            return minutos+":0"+seg;
        else
            return minutos+":"+seg;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(trackPlayer!=null) {
            trackPlayer.stop();
            trackPlayer.release();
        }
            finish();
    }
}
