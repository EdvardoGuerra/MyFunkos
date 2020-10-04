package br.com.myfunkos.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;

import br.com.myfunkos.R;
import br.com.myfunkos.model.Item;

public class NovoItemActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Novo Item";

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);
        setTitle(TITULO_APPBAR);

        Button salvarItem = findViewById(R.id.botao_salvar);
        salvarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = obterNovoItem();
                Log.v("Funko", item.toString());

            }
        });
    }

    private Item obterNovoItem() {

        Item novoItem = null;

        EditText titulo = findViewById(R.id.detalhe_item_titulo);
        String tituloEmTexto = titulo.getText().toString();

        String imagemEmTexto = "";

        EditText descricao = findViewById(R.id.novo_item_descricao);
        String descricaoEmTexto = descricao.getText().toString();

        EditText universo = findViewById(R.id.novo_item_universo);
        String universoEmTexto = universo.getText().toString();

        EditText data = findViewById(R.id.novo_item_data);
        String dataEmTexto = data.getText().toString();

        EditText valor = findViewById(R.id.novo_item_valor);
        String valorEmString = valor.getText().toString();
        BigDecimal valorEmBigDecimal = BigDecimal.valueOf(Float.parseFloat(valorEmString));

        novoItem = new Item(tituloEmTexto, imagemEmTexto, descricaoEmTexto,
                universoEmTexto, dataEmTexto, valorEmBigDecimal);

        return novoItem;
    }
}