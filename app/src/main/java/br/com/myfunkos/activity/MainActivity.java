package br.com.myfunkos.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URL;
import java.util.List;

import br.com.myfunkos.R;
import br.com.myfunkos.adapter.ListaItensAdapter;
import br.com.myfunkos.dao.ItemDAO;
import br.com.myfunkos.model.Item;

public class MainActivity extends AppCompatActivity {

    public static final String PATH_IMAGENS = "D:/Documentos/AndroidStudioProjects/MyFunkos/app/src/main/res/raw/";
    public static final String TITULO_APPBAR = "Minha Coleção";
    private StorageReference mStorageRef;
    private URL riversRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(TITULO_APPBAR);
        configuraLista();
        configuraBotaoNovoItem();


        mStorageRef = FirebaseStorage.getInstance().getReference();

//        uploadImagem();
//        downloadImagem();


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

//    private void downloadImagem() throws IOException {
//        File localFile = File.createTempFile("images", "jpg");
//        riversRef.getFile(localFile)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        // Successfully downloaded data to local file
//                        // ...
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle failed download
//                // ...
//            }
//        });
//    }

    private void uploadImagem() {

        //After starting the UploadTask you can manage it using the pause() , resume() and cancel() methods.
        Uri file;
        file = Uri.fromFile(new File(PATH_IMAGENS + "morte.png"));

        StorageReference morteRef = mStorageRef.child("images/morte.jpg");

        morteRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
}