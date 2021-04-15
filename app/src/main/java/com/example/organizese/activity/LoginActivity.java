package com.example.organizese.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizese.R;
import com.example.organizese.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText email, senha;
    private Button btnAcessar;
    private Usuario usuario;
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        btnAcessar = findViewById(R.id.btnAcessar);

        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUsuario = email.getText().toString();
                String senhaUsuario = senha.getText().toString();

                Boolean validacaoDeCadastro = validarCadastro(emailUsuario, senhaUsuario);

                if(validacaoDeCadastro){

                    usuario = new Usuario();
                    usuario.setEmail(emailUsuario);
                    usuario.setSenha(senhaUsuario);

                    autenticarUsuario(usuario);
                }

            }
        });
    }

    private Boolean validarCadastro(String emailUsuario, String senhaUsuario) {
        Boolean validacao = false;

        if (!emailUsuario.isEmpty()) {
            if (!senhaUsuario.isEmpty()) {
            } else {
                Toast.makeText(this, "Preencher a Senha do Usuário!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencher o Email do Usuário!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    private void autenticarUsuario(Usuario usuario) {

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    abrirTelaPrincipal();
                }else
                //Tratamento de execeções com FireBase
                    {
                    String excecao = "";
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email ou Senha não correspondem ao usuário cadastrado!";
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não está cadastrado!";
                    }catch (Exception e ){
                        excecao = "Erro ao cadastrar Usuário: " +e.getMessage();
                        //Para exibir a exeção no Logcat
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void abrirTelaPrincipal() {
            startActivity(new Intent(this, PrincipalActivity.class));
            finish();
    }
}