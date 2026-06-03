package com.example.af_mobile.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.af_mobile.AddBook;
import com.example.af_mobile.R;
import com.example.af_mobile.models.Livro;
import com.example.af_mobile.models.LivroFirebase;

import java.util.List;

public class LivroFirebaseAdapter extends RecyclerView.Adapter<LivroFirebaseAdapter.LivroViewHolder> {

    private final List<LivroFirebase> livros;

    public LivroFirebaseAdapter(List<LivroFirebase> livros) {
        this.livros = livros;
    }

    @NonNull
    @Override
    public LivroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livro_user, parent, false);
        return new LivroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivroViewHolder holder, int position) {
        LivroFirebase livro = livros.get(position);
        holder.tvTitulo.setText(livro.getTitulo());
        holder.tvAutor.setText(livro.getAutor());
        holder.tvStatus.setText(livro.getStatus());
        holder.tvObservation.setText(livro.getObservacao());
        holder.tvLocal.setText(livro.getLocal());
        holder.tvGeolocalizacao.setText(livro.getGeolocalizacao());

        if (livro.getCover() != null) {
            Glide.with(holder.itemView.getContext())
                    .load("https://covers.openlibrary.org/b/id/" + livro.getCover() + "-M.jpg")
                    .into(holder.ivBookCover);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddBook.class);
            intent.putExtra("editandoId", livro.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    static class LivroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvStatus, tvLocal, tvGeolocalizacao, tvObservation;
        ImageView ivBookCover;

        LivroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLocal = itemView.findViewById(R.id.tvLocal);
            tvGeolocalizacao = itemView.findViewById(R.id.tvGeolocalizacao2);
            tvObservation = itemView.findViewById(R.id.tvObservation);
            ivBookCover = itemView.findViewById(R.id.ivBookCover);
        }
    }
}
