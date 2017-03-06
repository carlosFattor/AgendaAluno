package br.com.guildacode.agendaalunos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.jar.Manifest;

import br.com.guildacode.agendaalunos.adapter.AlunosAdapter;
import br.com.guildacode.agendaalunos.dao.AlunoDAO;
import br.com.guildacode.agendaalunos.models.Aluno;

public class ListaAlunoActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aluno);
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        loadList();

        //click normal
        setItemInListClicked();

        //click longo
//        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Aluno aluno = ListaAlunoActivity.this.getAlunoFromList(position);
//                Toast.makeText(ListaAlunoActivity.this, "Click Longo", Toast.LENGTH_SHORT).show();
                    //true-> encerra evento | false-> continua o evento
//                return false;
//            }
//        });

        Button novoAluno = (Button) findViewById(R.id.btn_novo_aluno);
        novoAluno.setOnClickListener(onClickBtnNovoAluno());

        registerForContextMenu(listaAlunos);
    }

    private void setItemInListClicked() {
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = ListaAlunoActivity.this.getAlunoFromList(position);
//                Toast.makeText(ListaAlunoActivity.this, "Aluno "+aluno.getNome()+" foi clicado", Toast.LENGTH_SHORT).show();

                Intent intentGoToForm = new Intent(ListaAlunoActivity.this, FormularioActivity.class);
                intentGoToForm.putExtra("aluno", aluno);

                startActivity(intentGoToForm);
            }
        });
    }

    @NonNull
    private View.OnClickListener onClickBtnNovoAluno() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunoActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Após obter permissão do usuário, é chamado esse método.
        if(requestCode == 123){
            //executa ação
        } else if(requestCode == 124){
            //executa outra acão
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem ligar = menu.add("Ligar");
        MenuItem site = menu.add("Visitar site");
        MenuItem sms = menu.add("Enviar SMS");
        MenuItem mapa = menu.add("Mapa");
        MenuItem edit = menu.add("Edit");
        MenuItem delete = menu.add("Deletar");


        menuDelete((AdapterView.AdapterContextMenuInfo) menuInfo, delete);
        menuEdit((AdapterView.AdapterContextMenuInfo) menuInfo, edit);
        menuSite((AdapterView.AdapterContextMenuInfo) menuInfo, site);
        menuSms((AdapterView.AdapterContextMenuInfo)menuInfo, sms);
        menuMapa((AdapterView.AdapterContextMenuInfo)menuInfo, mapa);
        menuLigar((AdapterView.AdapterContextMenuInfo)menuInfo, ligar);
    }

    private void menuLigar(AdapterView.AdapterContextMenuInfo menuInfo, MenuItem ligar) {
        Aluno aluno = getAlunoFromList(menuInfo.position);

//        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(ActivityCompat.checkSelfPermission(ListaAlunoActivity.this, Manifest.permission.CALL_PHONE)
//                        != PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(ListaAlunoActivity.this, new String[]{Manifest.permission.CALL_PHONE, 123});
//                } else {
//                    Intent intentLigar = new Intent(Intent.ACTION_VIEW);
//                    intentLigar.setData(Uri.parse("tel:"+this.aluno.getTelefone()));
//
//                    startActivity(intentLigar);
//                }
//                return false;
//            }
//        });

        Intent intentLigar = new Intent(Intent.ACTION_VIEW);
        intentLigar.setData(Uri.parse("tel:"+aluno.getTelefone()));
        ligar.setIntent(intentLigar);
    }

    private void menuMapa(AdapterView.AdapterContextMenuInfo menuInfo, MenuItem mapa) {
        Aluno aluno = getAlunoFromList(menuInfo.position);

        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?"+aluno.getEndereco()));
        mapa.setIntent(intentMapa);
    }

    private void menuSms(final AdapterView.AdapterContextMenuInfo menuInfo, MenuItem sms) {
        Aluno aluno = getAlunoFromList(menuInfo.position);

        Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.setData(Uri.parse("sms:" + aluno.getTelefone()));
        sms.setIntent(intentSms);

    }

    private void menuSite(final AdapterView.AdapterContextMenuInfo menuInfo, MenuItem site) {
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        Aluno aluno = getAlunoFromList(menuInfo.position);
        String url = aluno.getSite();

        if(!url.startsWith("http://")){
            url = "http://" + url;
        }

        intentSite.setData(Uri.parse(url));
        site.setIntent(intentSite);
    }


    private void menuEdit(final AdapterView.AdapterContextMenuInfo menuInfo, MenuItem edit) {
        edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo edit = menuInfo;
                Aluno aluno = getAlunoFromList(edit.position);
                Intent intentForm = new Intent(ListaAlunoActivity.this, FormularioActivity.class);
                intentForm.putExtra("aluno", aluno);
                startActivity(intentForm);
                return false;
            }
        });
    }

    private void menuDelete(final AdapterView.AdapterContextMenuInfo menuInfo, MenuItem delete) {
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AdapterView.AdapterContextMenuInfo info = menuInfo;
                Aluno aluno = getAlunoFromList(info.position);

                AlunoDAO alunoDAO = new AlunoDAO(ListaAlunoActivity.this);

                alunoDAO.delete(aluno);

                loadList();
                Toast.makeText(ListaAlunoActivity.this, "Aluno "+ aluno.getNome() +" apagado.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private Aluno getAlunoFromList(Integer position){
        return (Aluno) listaAlunos.getItemAtPosition(position);
    }

    private void loadList() {
        AlunoDAO alunoDAO = new AlunoDAO(this);
        List<Aluno> alunos = alunoDAO.getAlunos();
        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAlunos.setAdapter(adapter);
    }
}