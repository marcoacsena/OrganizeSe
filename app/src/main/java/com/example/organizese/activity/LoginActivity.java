package com.example.organizese.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

public class LoginActivity extends AppCompatActivity {

    private static final int SELECAO_CAMERA = 1;
    private static final int SELECAO_GALERIA = 2;

    private EditText email, senha;
    private ImageView ivFoto;
    private Button btnAcessar;
    private Button btnCamera, ibtnGaleria;
    private Usuario usuario;
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();

//    private String [] permissoesNecessarias = new String[] {
//
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        btnAcessar = findViewById(R.id.btnAcessar);
        ivFoto = findViewById(R.id.ivFoto);
        btnCamera = findViewById(R.id.btnCamera);
        ibtnGaleria = findViewById(R.id.ibtnGaleria);

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

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(ACTION_IMAGE_CAPTURE);
                if(takePicture.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(takePicture, SELECAO_CAMERA);
                }
            }
        });

        ibtnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(pickImage.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(pickImage, SELECAO_GALERIA);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

        try {
            switch (requestCode){
                case SELECAO_CAMERA:
                    imagem = (Bitmap) data.getExtras().get("data");
                    break;
                case SELECAO_GALERIA:
                    Uri localImagemRecuperada = data.getData();
                    imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemRecuperada);
                    break;
            }

            if(imagem != null){
                ivFoto.setImageBitmap(imagem);
            }

        }catch (Exception e){e.printStackTrace();}

        }
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