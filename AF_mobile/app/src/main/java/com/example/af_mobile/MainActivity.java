package com.example.af_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.af_mobile.adapters.LivroFirebaseAdapter;
import com.example.af_mobile.models.LivroFirebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    List<LivroFirebase> livros;
    Button btnRedirectNewBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        WireElements();
        CarregaLivros();
    }

    @Override
    protected void onResume(){
        super.onResume();
        CarregaLivros();
    }

    private void WireElements(){
        btnRedirectNewBook = findViewById(R.id.btnRedirectNewBook);
        btnRedirectNewBook.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchBook.class);
            startActivity(intent);
        });
    }

    private void CarregaLivros(){
        db.collection("user_livros")
                .get()
                .addOnSuccessListener(result -> {
                    livros = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : result) {
                        LivroFirebase livro = doc.toObject(LivroFirebase.class);
                        livro.setId(doc.getId());
                        livros.add(livro);
                    }
                    RecyclerView rvLivros = findViewById(R.id.rvUserBooks);
                    rvLivros.setLayoutManager(new LinearLayoutManager(this));
                    rvLivros.setAdapter(new LivroFirebaseAdapter(livros));
                });
    }
}