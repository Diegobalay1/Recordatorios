package es.diego3l.recordatorios;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecordatorioActivity extends AppCompatActivity {
    private ListView miListaVista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);
        miListaVista = (ListView) findViewById(R.id.recordatorios_lista_view);
        //El ArrayAdapter es el controlador o controller en nuestra
        //relación con model-vista-controller. (controller)
        ArrayAdapter<String> miArrayAdapter = new ArrayAdapter<String>(
                //context
                this,
                //layout (view)
                R.layout.recordatorio_fila,
                //row (view)
                R.id.fila_texto,
                //data (model) con datos falsos para probar nuestra listview "miListaVista"
                new String[]{"Primer recordatorio", "Segundo recordatorio", "Tercer recordatorio"}
        );

        miListaVista.setAdapter(miArrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recordatorio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_nuevo:
                //crear nuevo aviso
                Log.d(getLocalClassName(), "crear  nuevo Aviso");
                return true;
            case R.id.accion_salir:
                //Finalizar o salir
                finish();
                return true;
            default:
                return false;
        }
    }
}
