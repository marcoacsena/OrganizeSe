package com.example.organizese.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizese.R;
import com.example.organizese.config.ConfiguracaoFirebase;
import com.example.organizese.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome, email, senha;
    private Button cadastro;
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = findViewById(R.id.editNome);
        email = findViewById(R.id.editEmail);
        senha = findViewById(R.id.editSenha);
        cadastro = findViewById(R.id.btnCadastro);

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = nome.getText().toString();
                String emailUsuario = email.getText().toString();
                String senhaUsuario = senha.getText().toString();

                Boolean validacaoDeCadastro = validarCadastro(nomeUsuario, emailUsuario, senhaUsuario);

                if(validacaoDeCadastro){
                    usuario = new Usuario();
                    usuario.setNome(nomeUsuario);
                    usuario.setEmail(emailUsuario);
                    usuario.setSenha(senhaUsuario);

                    cadastrarUsuario(usuario);
                }
            }
        });

        //Verifica se o usuário está logado
        usuarioLogado();
    }

    public boolean validarCadastro(String nomeUsuario, String emailUsuario, String senhaUsuario) {
        Boolean validacao = false;
        if(!nomeUsuario.isEmpty()){
            if(!emailUsuario.isEmpty()){
                if(!senhaUsuario.isEmpty()){
                    validacao = true;
                }else {Toast.makeText(this, "Preencher a Senha do Usuário!", Toast.LENGTH_SHORT).show();}

            }else {Toast.makeText(this, "Preencher o Email do Usuário!", Toast.LENGTH_SHORT).show();}
        }else {
            Toast.makeText(this, "Preencher o Nome do Usuário!", Toast.LENGTH_SHORT).show();
        }
        return validacao;
    }

    private void cadastrarUsuario(Usuario usuario) {
        //autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,
                            "Sucesso ao cadastrar o Usuário",
                            Toast.LENGTH_SHORT).show();

                //Tratamento de exceção ao usar o Firebase
                }else {

                    String excecao = "";
                    try {

                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte, que tenha letras e números!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite uma email válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Já existe conta com esse email cadastrado!";
                    }catch (Exception e ){
                        excecao = "Erro ao cadastrar Usuário: " +e.getMessage();

                        //Para exibir a exeção no Logcat
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();}
            }
        });

    }


    private void usuarioLogado() {

    }
}