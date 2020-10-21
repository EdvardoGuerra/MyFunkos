package br.com.myfunkos.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.myfunkos.R;
import br.com.myfunkos.model.Item;

public class NovoItemActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Novo Item";

    private Item item;
    public FirebaseAuth fAuth;
    public FirebaseUser fUser;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);
        setTitle(TITULO_APPBAR);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        imageView = findViewById(R.id.detalhe_item_imagem);

        configuraBotaoTirarFoto();
        configuraBotaoAbrirGaleria();
        configuraBotaoNovoItem();

    }

    private void configuraBotaoTirarFoto() {
        Button tirarFoto = findViewById(R.id.botao_tirar_foto);
        tirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });
    }

    private void tirarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    private void configuraBotaoAbrirGaleria() {
        final Button abrirGaleriaButton = findViewById(R.id.botao_abrir_galeria);
        abrirGaleriaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri pathImg = data.getData();
            Bitmap imagemBitmap;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    imagemBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), pathImg));
                    imageView.setImageBitmap(imagemBitmap);
                } else {
                    imagemBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pathImg);
                    imageView.setImageBitmap(imagemBitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void configuraBotaoNovoItem() {
        Button salvarItem = findViewById(R.id.botao_salvar);
        salvarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarImagem();
                item = obterNovoItem();
                Log.v("Funko", item.toString());
            }
        });
    }

    private void salvarImagem() {
        Bitmap imagemBitmap = null;
        imagemBitmap = obtemImagemEmBitmap();
        salvaImagemNoFirebaseStorage(imagemBitmap);
    }

    private Bitmap obtemImagemEmBitmap() {
        Bitmap imagemBitmap;
        try {
            imagemBitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(imagemBitmap);
            imageView.draw(canvas);
        } catch (Exception e) {
            BitmapDrawable imagemDrawable = (BitmapDrawable) imageView.getDrawable();
            imagemBitmap = imagemDrawable.getBitmap();
        }
        return imagemBitmap;
    }

    private void salvaImagemNoFirebaseStorage(Bitmap imagemBitmap) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pastaImagens = storageReference.child("imagens");
        final String nomeFoto = UUID.randomUUID().toString();
        StorageReference foto = pastaImagens.child(nomeFoto + ".jpeg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imagemBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
        UploadTask uploadTask = foto.putBytes(outputStream.toByteArray());

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.v("Funko", "Foto salva com sucesso");
                salvarItem(nomeFoto);
            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("Funko", "Falha no envio da foto");
            }
        });
    }

    private void salvarItem(String nomeFoto) {
        DatabaseReference banco = FirebaseDatabase.getInstance().getReference();
        DatabaseReference JSONItens = banco.child("itens");
        DatabaseReference JSONItensPorUsuario = JSONItens.child(fUser.getUid());

        String key = JSONItensPorUsuario.push().getKey();
        Item item = obterNovoItem();
        item.setImagem(nomeFoto);

        JSONItensPorUsuario.child(key).setValue(item);

        Toast.makeText(this, "Item salvo com sucesso", Toast.LENGTH_SHORT).show();
        vaiParaMain();

    }

    private void vaiParaMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        Double valorEmDouble = (double) Float.parseFloat(valorEmString);

        novoItem = new Item(tituloEmTexto, imagemEmTexto, descricaoEmTexto,
                universoEmTexto, dataEmTexto, valorEmDouble);

        return novoItem;
    }
}