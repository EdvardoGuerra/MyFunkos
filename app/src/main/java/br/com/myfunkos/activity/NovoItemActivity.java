package br.com.myfunkos.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import br.com.myfunkos.R;
import br.com.myfunkos.model.Item;

public class NovoItemActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Novo Item";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1234;

    private Item item;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);
        setTitle(TITULO_APPBAR);

        configuraBotaoNovoItem();

        Button tirarFoto = findViewById(R.id.botao_tirar_foto);
        tirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obterPermissoes();

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }


            }
        });
    }

    private void obterPermissoes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else
            dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile("PHOTOAPP", ".jpg", storageDir);
                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "Erro ao tirar a foto", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                ImageView imagem = (ImageView) findViewById(R.id.detalhe_item_imagem);
                Bitmap bm1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(mCurrentPhotoPath)));
                imagem.setImageBitmap(bm1);
            } catch (FileNotFoundException fnex) {
                Toast.makeText(getApplicationContext(), "Foto não encontrada!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void configuraBotaoNovoItem() {
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