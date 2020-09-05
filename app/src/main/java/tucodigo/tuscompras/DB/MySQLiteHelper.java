package tucodigo.tuscompras.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper {
    private  static  final String CreateTableUsers="create table carrito(id integer primary key,productoId int not null,producto text,cantidad int,total float, precio float,estatus bit)";
    private  static final String DB_NAME="carrito.sqlite";
    private static final int DB_VERSION=1;

    public MySQLiteHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTableUsers);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
