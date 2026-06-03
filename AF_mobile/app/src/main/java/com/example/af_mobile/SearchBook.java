package com.example.af_mobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.af_mobile.adapters.LivroAdapter;
import com.example.af_mobile.models.Pesquisa;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchBook extends AppCompatActivity {
    Pesquisa resultadoPesquisa = null;
    Button btnBackNewBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        WireElements();
    }

    private void WireElements(){
        btnBackNewBook = findViewById(R.id.btnBackNewBook);
        btnBackNewBook.setOnClickListener(v -> finish());
        findViewById(R.id.btnSearch).setOnClickListener(v -> pesquisarTitulos());
    }

    private void pesquisarTitulos(){
        listRequest();
    }

    private void listRequest(){
        EditText etBookSearch = findViewById(R.id.etBookSearch);
        String parametro = etBookSearch.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://openlibrary.org/search.json?q=" + parametro + "&page=1");
                    HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                    if (conexao.getResponseCode() != 200)
                        throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());

                    BufferedReader resposta = new BufferedReader(new
                            InputStreamReader((conexao.getInputStream())));

                    Log.d("Resposta", resposta.toString());
                    String aux, jsonEmString = "";
                    while ((aux = resposta.readLine()) != null) {
                        jsonEmString += aux;
                    }

                    String finalJsonEmString = jsonEmString;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            resultadoPesquisa = gson.fromJson(finalJsonEmString, Pesquisa.class);

                            RecyclerView rvLivros = findViewById(R.id.rvSearchBooks);
                            rvLivros.setLayoutManager(new LinearLayoutManager(SearchBook.this));
                            rvLivros.setAdapter(new LivroAdapter(resultadoPesquisa.getLivros()));
                        }
                    });
                }catch(Exception e){

                }
            }
        }).start();
    }
}