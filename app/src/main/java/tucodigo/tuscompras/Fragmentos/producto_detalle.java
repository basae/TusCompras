package tucodigo.tuscompras.Fragmentos;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tucodigo.tuscompras.Adapters.ProductoMovilAdapter;
import tucodigo.tuscompras.DB.MySQLiteHelper;
import tucodigo.tuscompras.Models.ProductoMovil;
import tucodigo.tuscompras.R;
import tucodigo.tuscompras.Services.ServiceApi;

/**
 * A simple {@link Fragment} subclass.
 */
public class producto_detalle extends Fragment {

    long id;
    ServiceApi service;

    ImageView productoImagen;
    TextView ProductoNombre;
    TextView ProductoCantidad;
    TextView ProductoTotal;
    TextView ProductoPrecio;
    TextView ProductoId;
    ImageButton mas;
    ImageButton menos;
    ImageButton agregar;

    NumberFormat formatter;
    ProductoMovil _producto;

    MySQLiteHelper dbHelper;

    public producto_detalle() {
        // Required empty public constructor
        service=new ServiceApi();
        formatter = new DecimalFormat("$ ###,###,###.00");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_producto_detalle, container, false);

        productoImagen = v.findViewById(R.id.producto_imagen);
        ProductoNombre =v.findViewById(R.id.producto_nombre);
        ProductoCantidad = v.findViewById(R.id.producto_cantidad);
        ProductoTotal = v.findViewById(R.id.producto_total);
        ProductoPrecio =v.findViewById(R.id.producto_precio);
        ProductoId   = v.findViewById(R.id.producto_id);

        agregar = v.findViewById(R.id.agregar_producto);
        mas = v.findViewById(R.id.mas);
        menos= v.findViewById(R.id.menos);
        dbHelper = new MySQLiteHelper(v.getContext());
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _cantidad= ProductoCantidad.getText().toString();
                _cantidad=_cantidad.replace(",","").replace("#","").replace(".","");
                int Incremento =Integer.parseInt(_cantidad)+1;
                ProductoCantidad.setText(String.valueOf(Incremento));
                //CalculaTotal(Incremento);
            }
        });
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _cantidad= ProductoCantidad.getText().toString();
                _cantidad=_cantidad.replace(",","").replace("#","").replace(".","");
                int Incremento =Integer.parseInt(_cantidad)-1;
                if(Incremento < 0)
                    Incremento=0;
                ProductoCantidad.setText(String.valueOf(Incremento));
                //CalculaTotal(Incremento);
            }
        });
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = db.rawQuery("SELECT id,cantidad,total FROM carrito where productoId=?", new String[]{ProductoId.getText().toString()});
                //no existe el producto en el carrito
                ContentValues values = new ContentValues();
                if(!c.moveToFirst())
                {
                    values.put("productoId", Integer.parseInt(ProductoId.getText().toString()));
                    values.put("producto", ProductoNombre.getText().toString());
                    values.put("cantidad", Integer.parseInt(ProductoCantidad.getText().toString()));
                    values.put("precio", Float.parseFloat(ProductoPrecio.getText().toString().replace("$","")));
                    values.put("total", Float.parseFloat(ProductoTotal.getText().toString().replace("$","")));
                    values.put("estatus", 0);
                    db.insert("carrito", null, values);
                }
                else
                {
                    int nuevaCantidad= c.getInt(1) + Integer.parseInt(ProductoCantidad.getText().toString());
                    float nuevototal= c.getFloat(2)+Float.parseFloat(ProductoTotal.getText().toString().replace("$",""));
                    values.put("cantidad", nuevaCantidad);
                    values.put("total", nuevototal);
                    db.update("carrito",values,"productoId=?",new String[]{ProductoId.getText().toString()});
                }
            }
        });
        ProductoCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String _cantidad= ProductoCantidad.getText().toString();
                _cantidad=_cantidad.replace(",","").replace("#","").replace(".","");
                int Incremento =Integer.parseInt(_cantidad);
                CalculaTotal(Incremento);
            }
        });
        id = getArguments().getLong("Id");
        GetService _request = new GetService();


            _request.execute(id);




        return v;
    }
    class GetService extends AsyncTask<Long,Void, ProductoMovil>
    {
        ProgressDialog dialog;
        ProductoMovil result=null;
        String arrayJson;
        String Error="";

        @Override
        protected ProductoMovil doInBackground(Long... Longs) {
            try
            {
                arrayJson = service.Get(getResources().getString(R.string.UrlGetProducto)+"/"+Longs[0]);
                if(arrayJson==null)
                {
                    Error="Usuario o Contraseña Incorrecta";
                    this.cancel(true);
                }
                else
                {
                    Gson json=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                    result =json.fromJson(arrayJson,ProductoMovil.class);
                }
            }
            catch (Exception ex)
            {
                Error=ex.getMessage();
                this.cancel(true);
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                dialog=new ProgressDialog(getActivity());
            dialog.setTitle("Cargando..");
            dialog.setIndeterminate(true);
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL,"Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
            });

            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(ProductoMovil jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                _producto = jsonObject;
                ProductoNombre.setText(jsonObject.getNombre());
                ProductoPrecio.setText(formatter.format(jsonObject.getPrecio()));
                ProductoTotal.setText(formatter.format(jsonObject.getPrecio()));
                ProductoId.setText(jsonObject.getId().toString());
                ProductoCantidad.setText("1");

                if(jsonObject.getImage()!=null)
                {
                    byte[] decodedString = Base64.decode(jsonObject.getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    productoImagen.setImageBitmap(decodedByte);
                }
                dialog.dismiss();
            }catch (Exception ex){
                Log.d("Error",ex.getMessage());

            }

            dialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.setMessage("Ocurrio un Error:"+Error);
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
        }
    }
    void CalculaTotal(int cantidad)
    {
        double resultado=0;
        resultado=cantidad*_producto.getPrecio();
        ProductoTotal.setText(formatter.format(resultado));

    }
}
