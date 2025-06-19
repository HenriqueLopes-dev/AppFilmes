package com.example.Agenda.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.widget.DatePicker;

import com.example.Agenda.controllers.MainActivity;
import com.example.Agenda.models.DaoCompromisso;
import com.example.Agenda.models.EntityCompromisso;

import java.util.Calendar;
import java.util.List;

public class FragmentoDatePickerData extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static FragmentoDatePickerData newInstance(){
        return new FragmentoDatePickerData();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, ano, mes, dia);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.d("DataHora", "Ano: "+String.valueOf(year));
        Log.d("DataHora", "Mês: "+String.valueOf(month+1));
        Log.d("DataHora", "Dia: "+String.valueOf(day));

        MainActivity activity = (MainActivity) getActivity();

        assert activity != null;
        activity.compromisso.setDia(day);
        activity.compromisso.setMes(month+1);
        activity.compromisso.setAno(year);

        if (activity.isOutraData){
            activity.listarOutraDataCompromisso();
        }
    }
}
