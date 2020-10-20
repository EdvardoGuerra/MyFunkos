package br.com.myfunkos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

public class ListaItensAdapter extends BaseAdapter {

    public FirebaseAuth fAuth;
    public FirebaseUser fUser;

    private final List<Item> itens;
    private final Context context;

    public ListaItensAdapter(List<Item> itens, Context context) {
        this.itens = itens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Item getItem(int posicao) {
        return itens.get(posicao);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup parent) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_colecao, parent, false);
        Item item = itens.get(posicao);

        mostraTitulo(viewCriada, item);
        mostraImagem(viewCriada, item);
        mostraDescricao(viewCriada, item);
        mostraUniverso(viewCriada, item);
        mostraData(viewCriada, item);
        mostraValor(viewCriada, item);

        return viewCriada;
    }

    private void mostraTitulo(View viewCriada, Item item) {
        TextView titulo = viewCriada.findViewById(R.id.item_colecao_titulo);
        titulo.setText(item.getTitulo());
    }

    private void mostraImagem(View viewCriada, final Item item) {

        final ImageView imagem = viewCriada.findViewById(R.id.item_colecao_imagem);
        recuperaImagemFirebase(item, imagem);

    }

    private void recuperaImagemFirebase(final Item item, final ImageView imagem) {
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
                StorageReference fotoItem = pasta.child(item.getImagem() + ".jpeg");

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
    }

    private void mostraDescricao(View viewCriada, Item item) {
        TextView descricao = viewCriada.findViewById(R.id.item_colecao_descricao);
        descricao.setText(item.getDescricao());
    }

    private void mostraUniverso(View viewCriada, Item item) {
        TextView universo = viewCriada.findViewById(R.id.item_colecao_universo);
        universo.setText(item.getUniverso());
    }

    private void mostraData(View viewCriada, Item item) {
        TextView data = viewCriada.findViewById(R.id.item_colecao_data);
        data.setText(item.getData());
    }

    private void mostraValor(View viewCriada, Item item) {
        TextView valor = viewCriada.findViewById(R.id.item_colecao_valor);
        String valorEmReal = MoedaUtil.formataParaReal(item.getValor());
        valor.setText(valorEmReal);
    }
}
