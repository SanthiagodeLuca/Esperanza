package com.example.esperanzaapk.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.esperanzaapk.R;
import com.example.esperanzaapk.databinding.FragmentDashboardBinding;
import com.example.esperanzaapk.ui.qr.AuthInterceptor;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private PieChart chart;
    private EstudianteService estudianteService;
    private AsistenciaService asistenciaService;

    private TextView textdashboard;

    private int totalEstudiantes;

    private EditText editTextDate;
    private EditText editTextDate2;

    private List<Asistencia> asistencias;
    private String selectedCategory = "Almuerzo";

    private static final String FILE_NAME = "server_ip.txt";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        chart = binding.pieChart;

        textdashboard = binding.textDashboard;

        Button buttonBreakfast = binding.buttonBreakfast;
        Button buttonLunch = binding.buttonLunch;
        Button buttonRefri = binding.buttonRefri;

        editTextDate = binding.editTextDate;
        editTextDate2 = binding.editTextDate2;

        // Obtener la fecha de hoy
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String todayFormatted = today.format(dateFormatter);

        // Configurar las fechas de hoy por defecto
        editTextDate.setText(todayFormatted);
        editTextDate2.setText(todayFormatted);

        selectedCategory = "Desayuno";
        textdashboard.setText(selectedCategory);


        buttonBreakfast.setOnClickListener(v -> {
            selectedCategory = "Desayuno";
            obtenerAsistencias();
            textdashboard.setText(selectedCategory);
        });
        buttonLunch.setOnClickListener(v -> {
            selectedCategory = "Almuerzo";
            obtenerAsistencias();
            textdashboard.setText(selectedCategory);
        });
        buttonRefri.setOnClickListener(v -> {
            selectedCategory = "Refrigerio";
            obtenerAsistencias();
            textdashboard.setText(selectedCategory);
        });

        editTextDate.setOnClickListener(v -> showDatePickerDialog(editTextDate));
        editTextDate2.setOnClickListener(v -> showDatePickerDialog(editTextDate2));

        String savedIp = readServerIpFromFile();

        Log.d(TAG, "Using IP: " + savedIp); // Verifica qué IP se está usando

        if (savedIp == null) {
            Toast.makeText(requireContext(), "IP no encontrada", Toast.LENGTH_SHORT).show();
        } else {
            // Obtener el token almacenado en SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", "");

            // Configurar OkHttpClient con un interceptor para agregar el token JWT a las peticiones
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new AuthInterceptor(token));

            // Log del token antes de la llamada HTTP
            Log.d("API_CALL", "Token enviado: " + token);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + savedIp + ":8085/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // Usar el cliente OkHttpClient configurado
                    .build();

            estudianteService = retrofit.create(EstudianteService.class);
            asistenciaService = retrofit.create(AsistenciaService.class);

            obtenerTotalEstudiantes();
            obtenerAsistencias();
        }
        return root;
    }

    private String readServerIpFromFile() {
        File file = new File(requireActivity().getExternalFilesDir(null), FILE_NAME);
        if (file.exists()) {
            try {
                byte[] data = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(data);
                fis.close();
                return new String(data, "UTF-8").trim(); // Trim para eliminar espacios en blanco al inicio o final
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; // O manejar de acuerdo a tu lógica si no se encuentra el archivo
    }

    private void setupPieChart(PieChart chart, int[] values) {
        chart.clearChart();

        int[] colors = {Color.parseColor("#66BB6A"), Color.parseColor("#EF5350")};
        String[] labels = {"Ya reclamaron", "Faltan por reclamar"};

        for (int i = 0; i < labels.length; i++) {
            chart.addPieSlice(new PieModel(labels[i], values[i], colors[i]));
        }

        chart.startAnimation();

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

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(formattedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private String formatDate(int day, int month, int year) {
        // Formatea la fecha en formato DD/MM/AAAA
        return String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
    }

    private void obtenerTotalEstudiantes() {
        estudianteService.obtenerTotalEstudiantes().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    totalEstudiantes = response.body();
                    Log.d("API_CALL", "Total de estudiantes: " + totalEstudiantes);
                    // No actualizar el gráfico aquí, esperar hasta obtener asistencias
                } else {
                    Log.e("API_CALL", "Error al obtener el total de estudiantes");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("API_CALL", "Error de red al obtener el total de estudiantes", t);
            }
        });
    }

    private void obtenerAsistencias() {
        if (editTextDate.getText().toString().isEmpty() || editTextDate2.getText().toString().isEmpty()) {
            Log.e("API_CALL", "Fechas no seleccionadas");
            return;
        }

        String startDate = formatDateForBackend(editTextDate.getText().toString(), true);
        String endDate = formatDateForBackend(editTextDate2.getText().toString(), false);

        FilterAsistencia filter = new FilterAsistencia(startDate, endDate);

        // Imprime el filtro para verificación
        Log.d("API_CALL", "Filter data: " + startDate + " - " + endDate);

        asistenciaService.obtenerAsistencias(filter).enqueue(new Callback<List<Asistencia>>() {
            @Override
            public void onResponse(Call<List<Asistencia>> call, Response<List<Asistencia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    asistencias = response.body();
                    Log.d("API_CALL", "Asistencias recibidas: " + asistencias.size());
                    procesarAsistencias();
                } else {
                    Log.e("API_CALL", "Error al obtener las asistencias");
                }
            }

            @Override
            public void onFailure(Call<List<Asistencia>> call, Throwable t) {
                Log.e("API_CALL", "Error de red al obtener las asistencias", t);
            }
        });
    }

    private String formatDateForBackend(String date, boolean isStartDate) {
        // Asume que la fecha está en formato DD/MM/AAAA
        String[] parts = date.split("/");
        String formattedDate = parts[2] + "-" + parts[1] + "-" + parts[0];

        // Formatea la fecha en LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(formattedDate + (isStartDate ? "T00:00:00" : "T23:59:59"));

        // Obtén la zona horaria de Bogotá
        ZoneId zoneId = ZoneId.of("America/Bogota");

        // Convierte a ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        // Formatea con el offset
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        return zonedDateTime.format(formatter);
    }

    private void procesarAsistencias() {
        int totalAsistencias = 0;

        // Filtrar las asistencias por la categoría seleccionada
        for (Asistencia asistencia : asistencias) {
            if (selectedCategory.equalsIgnoreCase("Desayuno") && asistencia.getAlmuerzo().getNombre().equalsIgnoreCase("Desayuno")) {
                totalAsistencias++;
            } else if (selectedCategory.equalsIgnoreCase("Almuerzo") && asistencia.getAlmuerzo().getNombre().equalsIgnoreCase("Almuerzo")) {
                totalAsistencias++;
            } else if (selectedCategory.equalsIgnoreCase("Refrigerio") && asistencia.getAlmuerzo().getNombre().equalsIgnoreCase("Refrigerio")) {
                totalAsistencias++;
            }
        }

        int asistenciasFaltantes = totalEstudiantes - totalAsistencias;
        setupPieChart(chart, new int[]{totalAsistencias, asistenciasFaltantes});
    }

    /*

    private FragmentDashboardBinding binding;
    private PieChart chart;
    private EstudianteService estudianteService;
    private AsistenciaService asistenciaService;

    private int totalEstudiantes;
    private int asistenciaEstudiantes;

    private int[] breakfastValues = {30, 70}; // Valores para desayuno
    private int[] lunchValues = {50, 50}; // Valores para almuerzo
    private int[] refriValues = {20, 80}; // Valores para cena;

    private EditText editTextDate;
    private EditText editTextDate2;

    private List<Asistencia> asistencias;
    private String selectedCategory = "Almuerzo";

    private static final String FILE_NAME = "server_ip.txt";

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
        Button buttonRefri = binding.buttonRefri;

        editTextDate = binding.editTextDate;
        editTextDate2 = binding.editTextDate2;

        buttonBreakfast.setOnClickListener(v -> setupPieChart(chart, breakfastValues));
        buttonLunch.setOnClickListener(v -> setupPieChart(chart, lunchValues));
        buttonRefri.setOnClickListener(v -> setupPieChart(chart, refriValues));
        editTextDate.setOnClickListener(v -> showDatePickerDialog(editTextDate));
        editTextDate2.setOnClickListener(v -> showDatePickerDialog(editTextDate2));

        String savedIp = readServerIpFromFile();

        Log.d(TAG, "Using IP: " + savedIp); // Verifica qué IP se está usando

        if (savedIp == null) {
            Toast.makeText(requireContext(), "IP no encontrada", Toast.LENGTH_SHORT).show();

        }else{

            // Obtener el token almacenado en SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", "");

            // Configurar OkHttpClient con un interceptor para agregar el token JWT a las peticiones
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new AuthInterceptor(token));

            // Log del token antes de la llamada HTTP
            Log.d("API_CALL", "Token enviado: " + token);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + savedIp + ":8085/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // Usar el cliente OkHttpClient configurado
                    .build();

            estudianteService = retrofit.create(EstudianteService.class);
            asistenciaService = retrofit.create(AsistenciaService.class); // Corregido para inicializar AsistenciaService

            obtenerTotalEstudiantes();
            obtenerAsistencias();

        }
        return root;
    }

    private String readServerIpFromFile() {
        File file = new File(requireActivity().getExternalFilesDir(null), FILE_NAME);
        if (file.exists()) {
            try {
                byte[] data = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(data);
                fis.close();
                return new String(data, "UTF-8").trim(); // Trim para eliminar espacios en blanco al inicio o final
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; // O manejar de acuerdo a tu lógica si no se encuentra el archivo
    }

    private void setupPieChart(PieChart chart, int[] values) {
        chart.clearChart();

        int[] colors = {Color.parseColor("#66BB6A"), Color.parseColor("#EF5350")};
        String[] labels = {"Ya reclamaron", "Faltan por reclamar"};

        for (int i = 0; i < labels.length; i++) {
            chart.addPieSlice(new PieModel(labels[i], values[i], colors[i]));
        }

        chart.startAnimation();

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

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(formattedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private String formatDate(int day, int month, int year) {
        // Formatea la fecha en formato DD/MM/AAAA
        return String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
    }

    private void obtenerTotalEstudiantes() {
        estudianteService.obtenerTotalEstudiantes().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    totalEstudiantes = response.body();
                    Log.d("API_CALL", "Total de estudiantes: " + totalEstudiantes);
                    actualizarPieChart(); // Llama a actualizar la gráfica de pastel con el nuevo total
                } else {
                    Log.e("API_CALL", "Error al obtener el total de estudiantes");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("API_CALL", "Error de red al obtener el total de estudiantes", t);
            }
        });
    }

    private void actualizarPieChart() {
        // Calcula los valores necesarios para la gráfica basada en totalEstudiantes
        int faltanPorReclamar = totalEstudiantes - 50; // Aquí deberías ajustar tu lógica según tus necesidades

        // Actualiza los valores en el arreglo y llama a setupPieChart nuevamente
        int[] values = {50, faltanPorReclamar};
        setupPieChart(chart, values);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void obtenerAsistencias() {
        /*
        if (editTextDate.getText().toString().isEmpty() || editTextDate2.getText().toString().isEmpty()) {
            Log.e("API_CALL", "Fechas no seleccionadas");
            return;
        }*/
/*
        String startDate = formatDateForBackend("2024-07-01T00:00:00-05:00");
        String endDate = formatDateForBackend("2024-07-20T00:00:00-05:00");

        FilterAsistencia filter = new FilterAsistencia(startDate, endDate);

        // Imprime el filtro para verificación
        Log.d("API_CALL", "Filter data: " + startDate + " - " + endDate);

        // Asumiendo que tienes un servicio para obtener asistencias
        asistenciaService.obtenerAsistencias(filter).enqueue(new Callback<List<Asistencia>>() {
            @Override
            public void onResponse(Call<List<Asistencia>> call, Response<List<Asistencia>> response) {
                if (response.isSuccessful()) {
                    asistencias = response.body();
                    inicializarPastel(asistencias);
                } else {
                    Log.e("API_CALL", "Error al obtener asistencias");
                }
            }

            @Override
            public void onFailure(Call<List<Asistencia>> call, Throwable t) {
                Log.e("API_CALL", "Error de red al obtener asistencias", t);
            }
        });
    }

    private void inicializarPastel(List<Asistencia> asistencias) {
        // Agrupar asistencias por categoría de almuerzo
        Map<String, Integer> asistenciasPorCategoria = new HashMap<>();
        for (Asistencia asistencia : asistencias) {
            String categoriaNombre = obtenerNombreCategoria(asistencia.getAlmuerzo().getId());
            asistenciasPorCategoria.put(categoriaNombre, asistenciasPorCategoria.getOrDefault(categoriaNombre, 0) + 1);
        }

        // Imprimir datos de cada categoría
        for (Map.Entry<String, Integer> entry : asistenciasPorCategoria.entrySet()) {
            Log.d("API_CALL", "Categoría: " + entry.getKey() + " - Cantidad: " + entry.getValue());
        }

        // Calcular estudiantes no asistidos en la categoría seleccionada
        int estudiantesAsistidosEnCategoria = asistenciasPorCategoria.getOrDefault(selectedCategory, 0);
        int estudiantesSinAsistenciaEnCategoria = totalEstudiantes - estudiantesAsistidosEnCategoria;

        Log.d("Calculo", "estudiantesAsistidosEnCategoria: " + estudiantesAsistidosEnCategoria + " - estudiantesSinAsistenciaEnCategoria: " + estudiantesSinAsistenciaEnCategoria);

        int[] data = {estudiantesAsistidosEnCategoria, estudiantesSinAsistenciaEnCategoria};
        setupPieChart(chart, data);

        // Actualizar categorías para el gráfico de pastel
        //categorias = new String[]{selectedCategory, "No asistido"};
    }

    private String obtenerNombreCategoria(int almuerzoId) {
        // Implementa esta función para mapear el id de almuerzo al nombre de la categoría
        switch (almuerzoId) {
            case 1:
                return "Desayuno";
            case 2:
                return "Refrigerio";
            case 3:
                return "Almuerzo";
            default:
                return "No asistido";
        }
    }

    private String formatDateForBackend(String date) {
        // Formatea la fecha según el formato requerido por el backend (ISO)
        return date.replaceAll("/", "-");
    }
*/

/*

    private FragmentDashboardBinding binding;
    private PieChart chart;

    private int[] breakfastValues = {30, 70}; // Valores para desayuno
    private int[] lunchValues = {50, 50}; // Valores para almuerzo
    private int[] dinnerValues = {20, 80}; // Valores para cena;

    private EditText editTextDate;
    private EditText editTextDate2;

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
        editTextDate2 = binding.editTextDate2;

        buttonBreakfast.setOnClickListener(v -> setupPieChart(chart, breakfastValues));
        buttonLunch.setOnClickListener(v -> setupPieChart(chart, lunchValues));
        buttonDinner.setOnClickListener(v -> setupPieChart(chart, dinnerValues));
        editTextDate.setOnClickListener(v -> showDatePickerDialog(editTextDate));
        editTextDate2.setOnClickListener(v -> showDatePickerDialog(editTextDate2));

        return root;
    }

    private void setupPieChart(PieChart chart, int[] values) {
        chart.clearChart();

        int[] colors = {Color.parseColor("#66BB6A"), Color.parseColor("#EF5350")};
        String[] labels = {"Ya reclamaron", "Faltan por reclamar"};

        for (int i = 0; i < labels.length; i++) {
            chart.addPieSlice(new PieModel(labels[i], values[i], colors[i]));
        }

        chart.startAnimation();

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

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(formattedDate);
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
    }*/
}
