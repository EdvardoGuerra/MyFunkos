package br.com.myfunkos.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

import br.com.myfunkos.R;
import br.com.myfunkos.adapter.ListaItensAdapter;
import br.com.myfunkos.dao.ItemDAO;
import br.com.myfunkos.model.Item;

public class MainActivity extends AppCompatActivity {

    public static final String PATH_IMAGENS = "D:/Documentos/AndroidStudioProjects/MyFunkos/app/src/main/res/raw/";
    public static final String TITULO_APPBAR = "Minha Coleção";
    public FirebaseAuth fAuth;
    public FirebaseUser fUser;
    private StorageReference mStorageRef;
    private URL riversRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(TITULO_APPBAR);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        configuraLista();
        configuraBotaoNovoItem();

//        lerMeusFunkos();
//        lerUmFunko();
//        salvaFunko();






    }

    private void salvaFunko() {
        Item item = new Item("teste", "imagem", "qualquer",
                "marvel", "12/12/12", 15.45);
        DatabaseReference meuJSON = FirebaseDatabase.getInstance().getReference();
        DatabaseReference meuJSON_FunkoPop = meuJSON.child("Colecoes/Funko Pop!");

        String chave = meuJSON_FunkoPop.push().getKey();
        meuJSON_FunkoPop.child(chave).setValue(item);
    }

    private void lerMeusFunkos() {
        DatabaseReference meuJSON = FirebaseDatabase.getInstance().getReference();
        DatabaseReference meuJSON_FunkoPop = meuJSON.child("Colecoes/Funko Pop!");


        meuJSON_FunkoPop.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = snapshot.getValue().toString();
                Log.v("Funko", "Lista do snapshot: " + s);
//                Item item = snapshot.getValue(Item.class);
//                Log.v("Funko", "item: " + item.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lerUmFunko() {
        DatabaseReference meuJSON = FirebaseDatabase.getInstance().getReference();
        DatabaseReference meuJSON_FunkoPop = meuJSON.child("Colecoes/Funko Pop!");
        DatabaseReference meuJSON_FunkoPop_0001 = meuJSON.child("Colecoes/Funko Pop!").child("0001");

        meuJSON_FunkoPop_0001.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.v("Funko", "0001: " + snapshot.getValue());
                Log.v("Funko", "0001: " + snapshot.child("titulo").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configuraLista() {
        ListView listaItens = findViewById(R.id.lista_itens_listview);
        final List<Item> itens = new ItemDAO().lista();
        listaItens.setAdapter(new ListaItensAdapter(itens, this));
        listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                Item itemClicado = itens.get(posicao);
                vaiParaDetalhesItem(itemClicado);
            }
        });

    }

    private void vaiParaDetalhesItem(Item itemClicado) {
        Intent intent = new Intent(MainActivity.this, DetalhesItemActivity.class);
        intent.putExtra("item", itemClicado);
        startActivity(intent);
    }

    private void configuraBotaoNovoItem() {
        Button botaoNovoItem = findViewById(R.id.botao_novo_item);
        vaiParaNovoItem(botaoNovoItem);
    }

    private void vaiParaNovoItem(Button botaoNovoItem) {
        botaoNovoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NovoItemActivity.class);
                startActivity(intent);
            }
        });
    }

}