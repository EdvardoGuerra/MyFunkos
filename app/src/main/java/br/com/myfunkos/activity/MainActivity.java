package br.com.myfunkos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.myfunkos.R;
import br.com.myfunkos.adapter.ListaItensAdapter;
import br.com.myfunkos.model.Item;

public class MainActivity extends AppCompatActivity{

    public static final String PATH_IMAGENS = "D:/Documentos/AndroidStudioProjects/MyFunkos/app/src/main/res/raw/";
    public static final String TITULO_APPBAR = "Minha Coleção";
    public FirebaseAuth fAuth;
    public FirebaseUser fUser;
    private StorageReference mStorageRef;
    private URL riversRef;
    public List<Item> itens;
    public ListView listaItens;

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(TITULO_APPBAR);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        itens = new ArrayList<>();
        configuraLista();
        configuraBotaoNovoItem();
    }


    private void configuraLista() {
        DatabaseReference banco = FirebaseDatabase.getInstance().getReference();
        DatabaseReference JSONItens = banco.child("itens");
        DatabaseReference JSONItensPorUsuario = JSONItens.child(fUser.getUid());

        listaItens = findViewById(R.id.lista_itens_listview);

        JSONItensPorUsuario.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<Item> itensRecuperados = new ArrayList<>();

                Log.v("Funko", "itens por usuário: " + snapshot.getChildrenCount());
                Item itemRecuperado = null;
                for (DataSnapshot itemBanco : snapshot.getChildren()) {
                    itemRecuperado = itemBanco.getValue(Item.class);
                    itensRecuperados.add(itemRecuperado);
//                    acrescentaItemNaLista(itemRecuperado);
                }

                Log.v("Funko", "Qte itens recuparados no OnDataChange: " + itensRecuperados.size());
                listaItens.setAdapter(new ListaItensAdapter(itensRecuperados, MainActivity.this));
                listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                        Item itemClicado = itensRecuperados.get(posicao);
                        vaiParaDetalhesItem(itemClicado);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        Log.v("Funko", "Itens..." + itens.toString());

//        listaItens.setAdapter(new ListaItensAdapter(itens, this));
//        listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
//                Item itemClicado = itens.get(posicao);
//                vaiParaDetalhesItem(itemClicado);
//            }
//        });

    }

    private void acrescentaItemNaLista(Item item) {
        itens.add(item);
        Log.v("Funko", "item acrescentado. tamanho: " + itens.size());
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