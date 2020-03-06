package com.example.reto_deezer_andres_varela;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;

import java.util.ArrayList;
import java.util.List;

public class AdapterCanciones extends RecyclerView.Adapter<AdapterCanciones.CustomViewHolder> {


    List<Track> data;

    public AdapterCanciones(){
        data = new ArrayList<>();
    }



    public void showAllCanciones(List<Track> allplaylists) {
        for(int i = 0 ; i<allplaylists.size() ; i++){
            if(!data.contains(allplaylists.get(i))) data.add(allplaylists.get(i));
        }
        notifyDataSetChanged();
    }
    @Override
    public AdapterCanciones.CustomViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_cancion, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        ((TextView) holder.root.findViewById(R.id.renglon_cancion_nombre)).setText(data.get(position).getTitle());
        ((TextView) holder.root.findViewById(R.id.renglon_cancion_artista)).setText(data.get(position).getArtist().getName());
       ((TextView) holder.root.findViewById(R.id.renglon_cancion_lanzamiento)).setText("Duración: "+tiempoMinutos(data.get(position).getDuration()));
        Glide.with(holder.root.getContext()).load(data.get(position).getAlbum().getMediumImageUrl()).into((ImageView)(holder.root.findViewById(R.id.foto_perfil_renglon_cancion)));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.root.getContext(),CancionActivity.class);
                i.putExtra("id",data.get(position).getId());
                holder.root.getContext().startActivity(i);
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
    public int getItemCount() {
        return data.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;


        }
    }


}
