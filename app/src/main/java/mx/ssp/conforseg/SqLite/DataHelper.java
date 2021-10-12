package mx.ssp.conforseg.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {
    public static final String DataBase_Name = "Conforseg";
    public static final int Database_Version = 1 ;

    public static final String Table_TempoServiciosSup = "TempoServiciosSup";
    public static final String Create_TempoServiciosSup = "CREATE TABLE IF NOT EXISTS " + Table_TempoServiciosSup +"(IdServicio INTEGER NOT NULL, Usuario TEXT NOT NULL ,Nombre TEXT NOT NULL, PRIMARY KEY (IdServicio, Usuario))";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_TempoServiciosSup);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public DataHelper(Context context){
        super(context,DataBase_Name,null,Database_Version);
    }

    /************************************************** CatSubMarcaVehiculos ********************************************************************/
    public void insertTempoServiciosSup(int idServicio, String usuario, String nombre){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdServicio",idServicio);
            values.put("Usuario",usuario);
            values.put("Nombre",nombre);
            dbSqLiteDatabase.insert(Table_TempoServiciosSup,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllTempoServiciosSup(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_TempoServiciosSup;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_TempoServiciosSup;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String nombre = cursor.getString(cursor.getColumnIndex("Nombre"));
                    list.add(nombre);
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }
    public int getIdTempoServiciosSup(String nombre){
        int idServicio = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_TempoServiciosSup;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdServicio FROM " + Table_TempoServiciosSup +" WHERE Nombre = '"+nombre+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idServicio = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idServicio;
    }

}
