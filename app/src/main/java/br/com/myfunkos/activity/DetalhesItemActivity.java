package br.com.myfunkos.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.com.myfunkos.R;
import br.com.myfunkos.model.Item;
import br.com.myfunkos.util.MoedaUtil;
import br.com.myfunkos.util.ResourcesUtil;

public class DetalhesItemActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Detalhes do Item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        setTitle(TITULO_APPBAR);

        Intent intent = getIntent();
        if (intent.hasExtra("item")) {
            final Item itemRecuperado = (Item) intent.getSerializableExtra("item");

            TextView titulo = findViewById(R.id.detalhe_item_titulo);
            titulo.setText(itemRecuperado.getTitulo());

            ImageView imagem = findViewById(R.id.detalhe_item_imagem);
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