package es.diego3l.recordatorios;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordatorioActivity extends AppCompatActivity {
    private ListView miListaVista;
    private AvisosDBAdapter mDbAdapter;
    private AvisosSimpleCursorAdapter mCursorAdapter;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
                String[] modes = new String[]{"Editar Aviso", "Borrar Aviso"};
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
                            int nId = getIdFromPosition(masterListPosition);
                            Aviso aviso = mDbAdapter.obtenerRecordatorioPorId(nId);
                            fireCustomDialogo(aviso);
                            //borrar aviso
                        } else {
                            mDbAdapter.borrarRecordatorioPorId(getIdFromPosition(masterListPosition));
                            mCursorAdapter.changeCursor(mDbAdapter.obtenerTodosLosRecordatorios());
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { //HONEYCOMB tiene un valor de 11, que es a la api a la que apuntamos
            miListaVista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            miListaVista.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_recordatorio:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (miListaVista.isItemChecked(nC)) {
                                    mDbAdapter.borrarRecordatorioPorId(getIdFromPosition(nC));
                                }
                            }
                            mode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.obtenerTodosLosRecordatorios());
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                }
            });

        }
    }

    private int getIdFromPosition(int nC) {
        return (int)mCursorAdapter.getItemId(nC);
    }


    private void fireCustomDialogo(final Aviso aviso){
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogo_custom);

        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (aviso != null);

        //esto es para un edit
        if (isEditOperation){
            titleView.setText("Editar Aviso");
            checkBox.setChecked(aviso.getImportante() == 1);
            editCustom.setText(aviso.getContenido());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.azul_neutro));
        }

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = editCustom.getText().toString();
                if (isEditOperation) {
                    Aviso reminderEdited = new Aviso(aviso.getId(),
                            reminderText, checkBox.isChecked() ? 1 : 0);
                    mDbAdapter.actualizarRecordatorio(reminderEdited);
                    //esto es para nuevo aviso
                } else {
                    mDbAdapter.crearRecordatorio(reminderText, checkBox.isChecked());
                }
                mCursorAdapter.changeCursor(mDbAdapter.obtenerTodosLosRecordatorios());
                dialog.dismiss();
            }
        });

        Button buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
                fireCustomDialogo(null);
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
