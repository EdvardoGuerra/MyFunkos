package br.com.myfunkos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.myfunkos.activity.MainActivity;

public class LoginActivity extends AppCompatActivity {

    public FirebaseAuth fAuth;
    EditText emailEditText;
    EditText senhaEditText;
    Button entrarBotao;
    Button criarBotao;
    Button esqueciBotao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        inicializarElementos();

    } //fim de onCreate

    private void inicializarElementos() {
        emailEditText = findViewById(R.id.login_email);
        senhaEditText = findViewById(R.id.login_senha);
        configurarBotaoEntrar();
        configurarBotaoCriar();
        configurarBotaoRedefinir();
    }

    private void configurarBotaoEntrar() {
        entrarBotao = findViewById(R.id.login_botao_login);
        entrarBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checarLoginSenha();
            }
        });
    }

    private void configurarBotaoRedefinir() {
        esqueciBotao = findViewById(R.id.login_botao_esqueci_senha);
        esqueciBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redefinirSenha();
            }
        });
    }

    private void configurarBotaoCriar() {
        criarBotao = findViewById(R.id.login_botao_criar);
        criarBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarUser();
            }
        });
    }

    private void checarLoginSenha() {

        String email = emailEditText.getText().toString();
        String senha = senhaEditText.getText().toString();
        email = "edvardo@uol.com.br";
        senha = "smdpdm2020";

        fAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            if (!user.isEmailVerified()) { //checa se email foi verificado
                                Toast.makeText(getApplicationContext(), "Verificar email de confirmação",
                                        Toast.LENGTH_LONG).show();
                                user.sendEmailVerification();
                            } else {
                                Toast.makeText(getApplicationContext(), "Usuário logado...",
                                        Toast.LENGTH_LONG).show();
                                vaiParaMain();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Deu errado",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void redefinirSenha() {
        String email = emailEditText.getText().toString();
        fAuth.sendPasswordResetEmail(email);
    }

    private void vaiParaMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void criarUser() {
        String email = emailEditText.getText().toString();
        String senha = senhaEditText.getText().toString();

        fAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Usuário criado
                            Toast.makeText(getApplicationContext(), "Usuário Criado", Toast.LENGTH_LONG).show();
                        } else {
                            //Não criado
                            Log.v("myfunko", "Exceção " + task.getException());
                        }
                    }
                });


    }

}