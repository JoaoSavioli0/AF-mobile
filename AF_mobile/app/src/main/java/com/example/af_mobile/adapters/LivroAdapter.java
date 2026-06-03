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

import java.util.List;

public class LivroAdapter extends RecyclerView.Adapter<LivroAdapter.LivroViewHolder> {

    private final List<Livro> livros;

    public LivroAdapter(List<Livro> livros) {
        this.livros = livros;
    }

    @NonNull
    @Override
    public LivroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livro, parent, false);
        return new LivroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivroViewHolder holder, int position) {
        Livro livro = livros.get(position);
        holder.tvTitulo.setText(livro.getTitulo());

        List<String> autores = livro.getAutor();
        holder.tvAutor.setText(autores != null && !autores.isEmpty() ? autores.get(0) : "");

        if (livro.getCapa() != null) {
            Glide.with(holder.itemView.getContext())
                    .load("https://covers.openlibrary.org/b/id/" + livro.getCapa() + "-M.jpg")
                    .into(holder.ivBookCover);
        }

        holder.btnAddBook.setOnClickListener(v -> {
            List<String> autoresClick = livro.getAutor();
            String autorStr = autoresClick != null && !autoresClick.isEmpty() ? String.join(", ", autoresClick) : "";

            Intent intent = new Intent(v.getContext(), AddBook.class);
            intent.putExtra("livro", livro.getId());
            intent.putExtra("titulo", livro.getTitulo());
            intent.putExtra("autor", autorStr);
            intent.putExtra("capa", livro.getCapa());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    static class LivroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        TextView tvAutor;
        ImageView ivBookCover;
        Button btnAddBook;

        LivroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            ivBookCover = itemView.findViewById(R.id.ivBookCover);
            btnAddBook = itemView.findViewById(R.id.btnAddBook);
        }
    }
}
