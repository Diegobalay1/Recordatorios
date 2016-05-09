package es.diego3l.recordatorios;

/**
 * Created by dlolh on 07/05/2016.
 */
//POJO
//(Plain Old Java Object)
public class Aviso {
    private int mId; //número único para identificar a cada aviso
    private String mContenido; //Contiene el texto del aviso
    private int mImportante; //a cada uno de los avisos importante o no. 1 Sí 0 No

    public Aviso(int id, String contenido, int importante) {
        mId = id;
        mContenido = contenido;
        mImportante = importante;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getContenido() {
        return mContenido;
    }

    public void setContenido(String contenido) {
        mContenido = contenido;
    }

    public int getImportante() {
        return mImportante;
    }

    public void setImportante(int importante) {
        mImportante = importante;
    }
}
