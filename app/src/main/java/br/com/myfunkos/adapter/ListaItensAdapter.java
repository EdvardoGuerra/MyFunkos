package br.com.myfunkos.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.myfunkos.R;
import br.com.myfunkos.model.Item;
import br.com.myfunkos.util.MoedaUtil;
import br.com.myfunkos.util.ResourcesUtil;

public class ListaItensAdapter extends BaseAdapter {

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

    private void mostraImagem(View viewCriada, Item item) {
        ImageView imagem = viewCriada.findViewById(R.id.item_colecao_imagem);
        Drawable drawableImagem = ResourcesUtil.devolveDrawable(context, item.getImagem());
        imagem.setImageDrawable(drawableImagem);
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
