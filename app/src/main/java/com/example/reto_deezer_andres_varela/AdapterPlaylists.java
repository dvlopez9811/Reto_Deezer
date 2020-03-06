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

import java.util.ArrayList;
import java.util.List;

public class AdapterPlaylists extends RecyclerView.Adapter<AdapterPlaylists.CustomViewHolder> {

    List<Playlist> data;

    public AdapterPlaylists(){
        data = new ArrayList<>();
    }



    public void showAllPlaylists(List<Playlist> allplaylists) {
        for(int i = 0 ; i<allplaylists.size() ; i++){
            if(!data.contains(allplaylists.get(i))) data.add(allplaylists.get(i));
        }
        notifyDataSetChanged();
    }
    @Override
    public CustomViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_playlist, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        ((TextView) holder.root.findViewById(R.id.renglon_playlist_nombre)).setText(data.get(position).getTitle());
        ((TextView) holder.root.findViewById(R.id.renglon_playlist_creador)).setText(data.get(position).getCreator().getName());

       Glide.with(holder.root.getContext()).load(data.get(position).getMediumImageUrl()).into((ImageView)(holder.root.findViewById(R.id.foto_perfil_renglon_playlist)));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.root.getContext(),PlaylistActivity.class);
                i.putExtra("id",data.get(position).getId());
                holder.root.getContext().startActivity(i);
            }
        });
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