package com.example.Filmes.views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.Filmes.controllers.MainActivity;

import java.util.Calendar;

/*
Exemplo de https://github.com/udofritzke/FragmentosDataEHora
 */

public class FragmentoTimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static FragmentoTimePicker newInstance() {
        return new FragmentoTimePicker();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hora, minuto, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Log.d("DataHora", "Hora: "+String.valueOf(hourOfDay));
        Log.d("DataHora", "Minuto: "+String.valueOf(minute));

        MainActivity activity = (MainActivity) getActivity();

        assert activity != null;
        //activity.compromisso.setHora(hourOfDay);
        //activity.compromisso.setMinuto(minute);
    }
}