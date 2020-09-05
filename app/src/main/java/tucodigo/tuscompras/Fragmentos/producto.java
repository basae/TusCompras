package tucodigo.tuscompras.Fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tucodigo.tuscompras.Adapters.ProductoMovilAdapter;
import tucodigo.tuscompras.Models.ProductoMovil;
import tucodigo.tuscompras.R;
import tucodigo.tuscompras.Services.ServiceApi;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link producto.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class producto extends Fragment {
    ProductoMovilAdapter adapter;
    RecyclerView rv;
    ServiceApi service;




    public producto() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_producto, container, false);
        service=new ServiceApi();
        adapter=new ProductoMovilAdapter();
        rv=(RecyclerView)v.findViewById(R.id.RecyclerView);
        GetService _request=new GetService();
        _request.execute();

        return v;
    }

    class GetService extends AsyncTask<Void,Void,List<ProductoMovil>>
    {
        ProgressDialog dialog;
        List<ProductoMovil> result=null;
        String arrayJson;
        String Error="";

        @Override
        protected List<ProductoMovil> doInBackground(Void... voids) {
            try
            {
                arrayJson = service.Get(getResources().getString(R.string.UrlGetProducto));
                if(arrayJson==null)
                {
                    Error="Usuario o Contraseña Incorrecta";
                    this.cancel(true);
                }
                else
                {
                    Gson json=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                    String test= arrayJson.toString();
                    ProductoMovil[] parse =json.fromJson(arrayJson,ProductoMovil[].class);
                    result = new ArrayList<>(Arrays.asList(parse));
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
        protected void onPostExecute(List<ProductoMovil> jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                adapter=new ProductoMovilAdapter(jsonObject,getFragmentManager());
                rv.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
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
}
