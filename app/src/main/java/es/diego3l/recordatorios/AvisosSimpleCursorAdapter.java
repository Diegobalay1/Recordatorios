package es.diego3l.recordatorios;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dlolh on 09/05/2016.
 */
public class AvisosSimpleCursorAdapter extends SimpleCursorAdapter{


    public AvisosSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    //para usar un viewholder, debemos sobrescribir los dos métodos siguientes y definir una clase ViewHolder
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.colImp = cursor.getColumnIndexOrThrow(AvisosDBAdapter.COL_IMPORTANTE);
            holder.listTab = view.findViewById(R.id.fila_tab);
            view.setTag(holder);
        }

        if (cursor.getInt(holder.colImp) > 0) {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.naranja));
        } else {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.gris_brown));
        }
    }

    static class ViewHolder {
        //almacena el index de la columna
        int colImp;
        //almacena la vista
        View listTab;
    }

}
