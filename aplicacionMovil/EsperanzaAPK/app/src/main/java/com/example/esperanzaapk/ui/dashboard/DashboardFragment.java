package com.example.esperanzaapk.ui.dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.esperanzaapk.R;
import com.example.esperanzaapk.databinding.FragmentDashboardBinding;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Calendar;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private PieChart chart;

    private int[] breakfastValues = {30, 70}; // Valores para desayuno
    private int[] lunchValues = {50, 50}; // Valores para almuerzo
    private int[] dinnerValues = {20, 80}; // Valores para cena;

    private EditText editTextDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        chart = binding.pieChart;
        setupPieChart(chart, breakfastValues);

        Button buttonBreakfast = binding.buttonBreakfast;
        Button buttonLunch = binding.buttonLunch;
        Button buttonDinner = binding.buttonDinner;

        editTextDate = binding.editTextDate;

        buttonBreakfast.setOnClickListener(v -> setupPieChart(chart, breakfastValues));
        buttonLunch.setOnClickListener(v -> setupPieChart(chart, lunchValues));
        buttonDinner.setOnClickListener(v -> setupPieChart(chart, dinnerValues));
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        return root;
    }

    private void setupPieChart(PieChart chart, int[] values) {
        chart.clearChart();

        int[] colors = {Color.parseColor("#66BB6A"), Color.parseColor("#EF5350")};
        String[] labels = {"Ya reclamaron", "Faltan por reclamar"};

        for (int i = 0; i < labels.length; i++) {
            chart.addPieSlice(new PieModel(labels[i], values[i], colors[i]));
        }

        chart.startAnimation(); // Establece la duración de la animación en milisegundos

        updateLegend(colors, labels, values);
    }

    private void updateLegend(int[] colors, String[] labels, int[] values) {
        LinearLayout legendContainer = binding.legendContainer;
        legendContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i < labels.length; i++) {
            View legendItem = inflater.inflate(R.layout.legend_item, legendContainer, false);

            View legendColor = legendItem.findViewById(R.id.legend_color);
            TextView legendLabel = legendItem.findViewById(R.id.legend_label);
            TextView legendValue = legendItem.findViewById(R.id.legend_value);

            GradientDrawable drawable = (GradientDrawable) legendColor.getBackground();
            drawable.setColor(colors[i]);

            legendLabel.setText(labels[i]);
            legendValue.setText(String.valueOf(values[i]));

            legendContainer.addView(legendItem);
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear);
                    editTextDate.setText(formattedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private String formatDate(int day, int month, int year) {
        // Formatea la fecha en formato DD/MM/AAAA
        return String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
