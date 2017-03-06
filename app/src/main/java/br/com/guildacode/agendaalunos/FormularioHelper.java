package br.com.guildacode.agendaalunos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Double2;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.guildacode.agendaalunos.models.Aluno;

/**
 * Created by carlos on 04/03/17.
 */

public class FormularioHelper {

    FormularioActivity activity;

    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoTelefone;
    private final EditText campoSite;
    private final RatingBar campoRate;
    private final ImageView campoPathFoto;
    private Aluno aluno;

    public FormularioHelper(FormularioActivity activity) {
        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        campoRate = (RatingBar) activity.findViewById(R.id.formulario_rate);
        campoPathFoto = (ImageView) activity.findViewById(R.id.formulario_img_user);
        aluno = new Aluno();
    }

    public Aluno getAluno() {
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setRate(Double.valueOf(campoRate.getProgress()));
        aluno.setPathFoto((String) campoPathFoto.getTag());
        return aluno;
    }

    public void setFormAluno(Aluno aluno) {
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoRate.setProgress(aluno.getRate().intValue());
        loadFoto(aluno.getPathFoto());
        this.aluno = aluno;
    }

    public void loadFoto(String pathFoto) {
        if(pathFoto != null){

            Bitmap bitmap = BitmapFactory.decodeFile(pathFoto);
            Bitmap bitmapReduce = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoPathFoto.setImageBitmap(bitmapReduce);
            campoPathFoto.setScaleType(ImageView.ScaleType.CENTER);
            campoPathFoto.setTag(pathFoto);
        }
    }
}

