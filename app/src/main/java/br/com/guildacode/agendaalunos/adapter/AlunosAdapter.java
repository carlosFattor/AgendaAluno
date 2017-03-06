package br.com.guildacode.agendaalunos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.guildacode.agendaalunos.R;
import br.com.guildacode.agendaalunos.models.Aluno;

/**
 * Created by carlos on 05/03/17.
 */

public class AlunosAdapter extends BaseAdapter {

    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context listaAlunoActivity, List<Aluno> alunos) {
        this.context = listaAlunoActivity;
        this.alunos = alunos;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Aluno aluno = alunos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if(convertView == null){

            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView nome = (TextView) view.findViewById(R.id.item_nome);
        nome.setText(aluno.getNome());
        TextView telefone = (TextView) view.findViewById(R.id.item_telefone);
        telefone.setText(aluno.getTelefone());
        ImageView foto = (ImageView) view.findViewById(R.id.item_foto);
        if(aluno.getPathFoto() != null){
            Bitmap bitmap = BitmapFactory.decodeFile(aluno.getPathFoto());
            Bitmap bitmapReduce = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            foto.setImageBitmap(bitmapReduce);
            foto.setScaleType(ImageView.ScaleType.FIT_XY);
        }


        return view;
    }
}
