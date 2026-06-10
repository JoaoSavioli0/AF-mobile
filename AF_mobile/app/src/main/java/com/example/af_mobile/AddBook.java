package com.example.af_mobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.example.af_mobile.models.Livro;
import com.example.af_mobile.models.LivroFirebase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddBook extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST = 200;

    String livroId = "";
    String editandoId = "";
    String titulo = "";
    String autor = "";
    String capa = "";
    String coordenadas = "-0.000000,-0.000000";
    LivroFirebase editandoLivro = null;
    TextView tvTitulo, tvAutor;
    EditText etObservation;
    Spinner spnStatus, spnLocal;
    ImageView ivBookCover;
    Button btnBackAddBook, btnSaveBook;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        WireElements();
        capturarCoordenadas();

        livroId = getIntent().getStringExtra("livro");
        editandoId = getIntent().getStringExtra("editandoId") != null ? getIntent().getStringExtra("editandoId") : "";
        titulo = getIntent().getStringExtra("titulo");
        autor = getIntent().getStringExtra("autor");
        capa = getIntent().getStringExtra("capa");

        tvTitulo.setText(titulo);
        tvAutor.setText(autor);
        if (capa != null) {
            Glide.with(this)
                    .load("https://covers.openlibrary.org/b/id/" + capa + "-M.jpg")
                    .into(ivBookCover);
        }

        InicializaSpinners();

        if (!editandoId.isEmpty()) {
            btnSaveBook.setText("Atualizar");
            btnSaveBook.setOnClickListener(c -> atualizarLivro());

            db.collection("user_livros").document(editandoId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            editandoLivro = doc.toObject(LivroFirebase.class);
                            editandoLivro.setId(doc.getId());

                            tvTitulo.setText(editandoLivro.getTitulo());
                            tvAutor.setText(editandoLivro.getAutor());
                            etObservation.setText(editandoLivro.getObservacao());

                            ArrayAdapter<String> statusAdapter = (ArrayAdapter<String>) spnStatus.getAdapter();
                            int statusPos = statusAdapter.getPosition(editandoLivro.getStatus());
                            if (statusPos >= 0) spnStatus.setSelection(statusPos);

                            ArrayAdapter<String> localAdapter = (ArrayAdapter<String>) spnLocal.getAdapter();
                            int localPos = localAdapter.getPosition(editandoLivro.getLocal());
                            if (localPos >= 0) spnLocal.setSelection(localPos);

                            if (editandoLivro.getCover() != null) {
                                Glide.with(this)
                                        .load("https://covers.openlibrary.org/b/id/" + editandoLivro.getCover() + "-M.jpg")
                                        .into(ivBookCover);
                            }
                        }
                    });
        } else {
            btnSaveBook.setOnClickListener(c -> SalvarLivro());
        }
    }

    private void WireElements(){
        tvTitulo = findViewById(R.id.tvTitulo2);
        tvAutor = findViewById(R.id.tvAutor2);
        ivBookCover = findViewById(R.id.ivBookCover2);
        btnBackAddBook = findViewById(R.id.btnBackAddBook);
        btnSaveBook = findViewById(R.id.btnSaveBook);
        spnStatus = findViewById(R.id.spnStatus);
        spnLocal = findViewById(R.id.spnLocal);
        etObservation = findViewById(R.id.etObservation);

        btnBackAddBook.setOnClickListener(v -> finish());

        btnSaveBook.setOnClickListener(v -> {
            SalvarLivro();
        });
    }

    private void capturarCoordenadas() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST);
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    coordenadas = latitude + "," + longitude;
                } else {
                    Toast.makeText(AddBook.this, "Não foi possível obter a localização",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(AddBook.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturarCoordenadas();
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SalvarLivro(){
        String status = spnStatus.getSelectedItem().toString();
        String local = spnLocal.getSelectedItem().toString();
        String titulo = tvTitulo.getText().toString();
        String autor = tvAutor.getText().toString();
        String observacao = etObservation.getText().toString();

        LivroFirebase livro = new LivroFirebase(titulo, status, observacao, local, coordenadas, autor, livroId, capa);
        DocumentReference docRef = db.collection("user_livros").document();
        livro.setId(docRef.getId());
        docRef.set(livro)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Livro salvo!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void atualizarLivro() {
        if (editandoLivro == null) return;

        editandoLivro.setStatus(spnStatus.getSelectedItem().toString());
        editandoLivro.setLocal(spnLocal.getSelectedItem().toString());
        editandoLivro.setObservacao(etObservation.getText().toString());

        db.collection("user_livros").document(editandoId)
                .set(editandoLivro)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Livro atualizado!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void InicializaSpinners() {
        String[] opcoesStatus = {"Quero ler", "Lendo", "Concluído"};
        String[] opcoesLocal = {"Biblioteca", "Livraria", "Aula", "Indicação", "Leitura em casa", "Viagem", "Outro"};

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opcoesStatus
        );

        ArrayAdapter<String> localAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opcoesLocal
        );

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnStatus.setAdapter(statusAdapter);
        spnLocal.setAdapter(localAdapter);
    }
}