package br.com.guildacode.agendaalunos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import br.com.guildacode.agendaalunos.dao.AlunoDAO;
import br.com.guildacode.agendaalunos.models.Aluno;

public class FormularioActivity extends AppCompatActivity {

    public static final int CAM_CODE = 567;
    private FormularioHelper FormHelper;
    private AlunoDAO alunoDAO;
    private String pathFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        FormHelper = new FormularioHelper(FormularioActivity.this);
        alunoDAO = new AlunoDAO(this);
        Intent intent = getIntent();
        if(intent.getSerializableExtra("aluno") != null){

            Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");

            FormHelper.setFormAluno(aluno);
        }

        Button btnCam = (Button) findViewById(R.id.formulario_btn_cam);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pathFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File fileFoto = new File(pathFoto);
                intentCam.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileFoto));
                startActivityForResult(intentCam, CAM_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //recuperar foto
        if(resultCode == Activity.RESULT_OK){

            if(requestCode == CAM_CODE){
                FormHelper.loadFoto(pathFoto);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = FormHelper.getAluno();

                if(aluno.getId() == null){
                    alunoDAO.insertAluno(aluno);
                } else {
                    alunoDAO.updateAluno(aluno);
                }

                Toast.makeText(FormularioActivity.this, "Aluno "+ aluno.getNome() +" salvo", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
