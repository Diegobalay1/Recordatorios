package es.diego3l.recordatorios;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RecordatorioActivity extends AppCompatActivity {
    private ListView miListaVista;
    private AvisosDBAdapter mDbAdapter;
    private AvisosSimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);
        miListaVista = (ListView) findViewById(R.id.recordatorios_lista_view);
        findViewById(R.id.recordatorios_lista_view);
        miListaVista.setDivider(null);
        mDbAdapter = new AvisosDBAdapter(this);
        mDbAdapter.abrir();


        /*if (savedInstanceState == null) { //Comprueba si hay un estado salvado de la instancia
            //limpiar todos los datos
            mDbAdapter.borrarTodosLosRecordatorios();
            //Añadir algunos datos
            mDbAdapter.crearRecordatorio("Visitar el Centro de JC", true);
            mDbAdapter.crearRecordatorio("Revisar código Orange", false);
            mDbAdapter.crearRecordatorio("Revisar Currículum", false);
            mDbAdapter.crearRecordatorio("Llamar o esperar esa llmada", true);
            mDbAdapter.crearRecordatorio("La que se avecina Serie pendiente", false);

        }*/

        // cuando pulsamos un item individual en la  listview
        miListaVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordatorioActivity.this);
                ListView modeListView = new ListView(RecordatorioActivity.this);
                String[] modes = new String[] { "Editar Aviso", "Borrar Aviso" };
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(RecordatorioActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //editar aviso
                        if (position == 0) {
                            Toast.makeText(RecordatorioActivity.this, "editar "
                                    + masterListPosition, Toast.LENGTH_SHORT).show();
                            //borrar aviso
                        } else {
                            Toast.makeText(RecordatorioActivity.this, "borrar "
                                    + masterListPosition, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });


        Cursor cursor = mDbAdapter.obtenerTodosLosRecordatorios();

        //desde las columnas definidas en la base de datos
        String[] from = new String[]{
                AvisosDBAdapter.COL_CONTENIDO
        };

        //a la id de views en el layout
        int[] to = new int[]{
                R.id.fila_texto
        };

        mCursorAdapter = new AvisosSimpleCursorAdapter(
                //context
                RecordatorioActivity.this,
                //el layout de la fila
                R.layout.recordatorio_fila,
                //cursor
                cursor,
                //desde columnas definidas en la base de datos
                from,
                //a las ids de vies en el layout
                to,
                //flag ~ no usado
                0
        );

        //el cursorAdapter (controller) está ahora actualizando la listView (view)
        //con datos desde la base de datos (modelo)
        miListaVista.setAdapter(mCursorAdapter);


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
