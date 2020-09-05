package tucodigo.tuscompras.Fragmentos;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONObject;

import tucodigo.tuscompras.MainActivity;
import tucodigo.tuscompras.Models.LoginModel;
import tucodigo.tuscompras.Models.User;
import tucodigo.tuscompras.Models.UserMovil;
import tucodigo.tuscompras.R;
import tucodigo.tuscompras.Services.ServiceApi;

/**
 * A simple {@link Fragment} subclass.
 */
public class login extends Fragment {


    public login() {
        // Required empty public constructor
    }
    EditText txt_usuario;
    EditText txt_password;
    Button btn_iniciasesion;
    ServiceApi service_Api;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_login, container, false);
        txt_usuario=(EditText)v.findViewById(R.id.txt_usuario);
        txt_password=(EditText)v.findViewById(R.id.txt_password);
        btn_iniciasesion=(Button)v.findViewById(R.id.btn_iniciasesion);
        service_Api=new ServiceApi();
        btn_iniciasesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Mensasaje="";
                boolean error=false;
                if(TextUtils.isEmpty(txt_usuario.getText().toString()))
                {
                    error=true;
                    Mensasaje+="*El campo usuario debe contener un valor.\n";
                }
                if(TextUtils.isEmpty(txt_password.getText().toString()))
                {
                    error=true;
                    Mensasaje+="*El campo password debe contener un valor.";
                }
                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                if(error)
                {
                    builder.setTitle("Faltan algunos campos por llenar")
                            .setMessage(Mensasaje)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                //codigo cuando las validaciones esten correctas
                LoginService login=new LoginService();
                LoginModel loginM=new LoginModel();
                loginM.setUserName(txt_usuario.getText().toString());
                loginM.setPassword(txt_password.getText().toString());
                login.execute(new Gson().toJson(loginM));
                try {
                    final JSONObject result = login.get();
                    if(result!=null)
                    {
                        builder.setTitle("Se ha logueado con exito")
                                .setMessage("Username:"+result.getString("Username")+"\nToken:"+result.getString("Token"))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        User.UserInfo= new Gson().fromJson(result.toString(), UserMovil.class);

                                        Intent intent=new Intent(getActivity(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
                catch (Exception e)
                {
                    String ex=e.getMessage();
                }

            }
        });

        return v;
    }
    class LoginService extends AsyncTask<String,Void,JSONObject>
    {
        ProgressDialog dialog;
        JSONObject result=null;
        String Error="";

        @Override
        protected JSONObject doInBackground(String... strings) {
            if(!TextUtils.isEmpty(strings[0]))
            {
                try
                {
                    result = service_Api.Post(getResources().getString(R.string.UrlLogin),strings[0]);
                    if(result==null)
                    {
                        Error="Usuario o Contraseña Incorrecta";
                        this.cancel(true);
                    }
                }
                catch (Exception ex)
                {
                    Error=ex.getMessage();
                    this.cancel(true);
                }
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
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                dialog.setMessage("Bienvenido" + jsonObject.getString("Username"));
            }catch (Exception ex){}

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
