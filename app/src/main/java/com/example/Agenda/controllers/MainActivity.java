package com.example.Agenda.controllers;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Agenda.R;
import com.example.Agenda.models.AppDatabase;
import com.example.Agenda.models.DaoCompromisso;
import com.example.Agenda.models.EntityCompromisso;
import com.example.Agenda.views.FragmentoDatePickerData;
import com.example.Agenda.views.FragmentoTimePicker;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/*
GitHub: https://github.com/udofritzke/FragmentosDataEHora
 */

public class MainActivity extends AppCompatActivity {
    static AppDatabase db = null;
    public EntityCompromisso compromisso = new EntityCompromisso();
    public boolean isOutraData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (db == null){
            db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "bd-compromisso").allowMainThreadQueries().build();
        }
        this.listarCompromissosHoje();
    }

    public void fragmentDatePickerData(View v) {
        isOutraData = false;
        DialogFragment fragment = FragmentoDatePickerData.newInstance();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void fragmentTimePickerHora(View v) {
        FragmentoTimePicker fragment = FragmentoTimePicker.newInstance();
        fragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void fragmentDatePickerOutraData(View v){
        isOutraData = true;
        DialogFragment fragment = FragmentoDatePickerData.newInstance();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }


    public void salvarDados(View v){
        DaoCompromisso daoCompromisso = db.daoCompromisso();

        EditText etDescricao = findViewById(R.id.etDescricao);
        String descricao = etDescricao.getText().toString();

        if (compromisso.getDia() == 0 || compromisso.getMes() == 0 || compromisso.getAno() == 0){
            Toast.makeText(this, "Selecione uma data!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (compromisso.getHora() == -1 || compromisso.getMinuto() == -1){
            Toast.makeText(this, "Selecione uma hora!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descricao.isEmpty()){
            Toast.makeText(this, "Digite uma descricao!", Toast.LENGTH_SHORT).show();
            return;
        }

        compromisso.setDescricao(descricao);

        if (daoCompromisso == null){
            Toast.makeText(this, "Compromisso não foi inserido!", Toast.LENGTH_SHORT).show();
            return;
        }

        daoCompromisso.inserir(compromisso);
        Toast.makeText(this, "Compromisso inserido com sucesso!", Toast.LENGTH_SHORT).show();
        this.listarOutraDataCompromisso();
        etDescricao.setText("");

    }

    public void limparDia(View v){
        DaoCompromisso daoCompromisso = db.daoCompromisso();
        int dia, mes, ano;
        dia = compromisso.getDia();
        mes = compromisso.getMes();
        ano = compromisso.getAno();
        List<EntityCompromisso> compromissos = daoCompromisso.compromissosPorData(dia, mes, ano);

        for(EntityCompromisso c : compromissos){
            daoCompromisso.deletar(c);
        }
        String texto = String.format(Locale.getDefault(), "Dia %02d/%02d/%d foi limpado!", dia, mes, ano);
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
        listarOutraDataCompromisso();
    }
    public void resetarBD(View v){
        db.clearAllTables();
        Toast.makeText(this, "Agenda foi deletada!", Toast.LENGTH_SHORT).show();
        listarOutraDataCompromisso();
    }

    public void botaoHoje(View v){
        this.listarCompromissosHoje();
    }
    public void listarCompromissosHoje(){
        // DATA ATUAL
        Calendar hoje = Calendar.getInstance();
        int dia = hoje.get(Calendar.DAY_OF_MONTH);
        int mes = hoje.get(Calendar.MONTH) + 1;
        int ano = hoje.get(Calendar.YEAR);

        compromisso.setDia(dia);
        compromisso.setMes(mes);
        compromisso.setAno(ano);

        DaoCompromisso daoCompromisso = db.daoCompromisso();
        List<EntityCompromisso> compromissos = daoCompromisso.compromissosPorData(dia, mes, ano);
        this.listarCompromissos(compromissos);
    }

    public void listarOutraDataCompromisso(){
        DaoCompromisso daoCompromisso = db.daoCompromisso();
        List<EntityCompromisso> compromissos = daoCompromisso.compromissosPorData(compromisso.getDia(), compromisso.getMes(), compromisso.getAno());
        this.listarCompromissos(compromissos);
    }

    public void listarCompromissos(List<EntityCompromisso> compromissos){
        LinearLayout listaLayout = findViewById(R.id.listaCompromissos);
        listaLayout.removeAllViews();

        TextView novoTextView = new TextView(this);
        String texto = String.format(Locale.getDefault(),"Dia %02d/%02d/%d", compromisso.getDia(), compromisso.getMes(), compromisso.getAno());

        novoTextView.setText(texto);
        novoTextView.setTextSize(20);
        novoTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        listaLayout.addView(novoTextView);

        if (compromissos == null || compromissos.isEmpty()) {
            TextView vazio = new TextView(this);
            vazio.setText("Nenhum compromisso nesse dia!");
            vazio.setTextSize(18);
            vazio.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            listaLayout.addView(vazio);
            return;
        }


        for(EntityCompromisso c : compromissos){

            novoTextView = new TextView(this);
            texto = String.format(Locale.getDefault(),"%02d:%02d - %s",
                    c.getHora(), c.getMinuto(), c.getDescricao());

            novoTextView.setText(texto);
            novoTextView.setTextSize(18);
            novoTextView.setPadding(0, 8, 0, 8);
            listaLayout.addView(novoTextView);
        }
    }
}
