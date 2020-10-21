package br.com.myfunkos.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import br.com.myfunkos.R;
import br.com.myfunkos.model.Item;
import br.com.myfunkos.util.GlideApp;
import br.com.myfunkos.util.MoedaUtil;
import br.com.myfunkos.util.ResourcesUtil;

public class DetalhesItemActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Detalhes do Item";
    public FirebaseAuth fAuth;
    public FirebaseUser fUser;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        setTitle(TITULO_APPBAR);
        context = getApplicationContext();

        Intent intent = getIntent();
        if (intent.hasExtra("item")) {
            final Item itemRecuperado = (Item) intent.getSerializableExtra("item");

            TextView titulo = findViewById(R.id.detalhe_item_titulo);
            titulo.setText(itemRecuperado.getTitulo());

            final ImageView imagem = findViewById(R.id.detalhe_item_imagem);
            fAuth = FirebaseAuth.getInstance();
            fUser = fAuth.getCurrentUser();
            DatabaseReference banco = FirebaseDatabase.getInstance().getReference();
            DatabaseReference JSONItens = banco.child("itens");
            DatabaseReference JSONItensPorUsuario = JSONItens.child(fUser.getUid());

            JSONItensPorUsuario.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference pasta = storageReference.child("imagens");
                    StorageReference fotoItem = pasta.child(itemRecuperado.getImagem() + ".jpeg");

                    // Download directly from StorageReference using Glide
                    // (See MyAppGlideModule for Loader registration)
                    GlideApp.with(context)
                            .load(fotoItem)
                            .into(imagem);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




//            Drawable drawableImagem = ResourcesUtil.devolveDrawable(this,
//                    itemRecuperado.getImagem());
//            imagem.setImageDrawable(drawableImagem);

            TextView descricao = findViewById(R.id.detalhe_item_descricao);
            descricao.setText(itemRecuperado.getDescricao());

            TextView universo = findViewById(R.id.detalhe_item_universo);
            universo.setText(itemRecuperado.getUniverso());

            TextView data = findViewById(R.id.detalhe_item_data);
            data.setText(itemRecuperado.getData());

            TextView valor = findViewById(R.id.detalhe_item_valor);
            String valorEmTexto = MoedaUtil.formataParaReal(itemRecuperado.getValor());
            valor.setText(valorEmTexto);

        }
    }
}