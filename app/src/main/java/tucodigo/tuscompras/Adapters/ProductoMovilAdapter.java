package tucodigo.tuscompras.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.datatype.Duration;

import tucodigo.tuscompras.Fragmentos.producto_detalle;
import tucodigo.tuscompras.Models.ProductoMovil;
import tucodigo.tuscompras.R;

public class ProductoMovilAdapter extends RecyclerView.Adapter<ProductoMovilAdapter.ViewHolder> {
    List<ProductoMovil> Reports;
    FragmentManager fragment;
    public ProductoMovilAdapter(List<ProductoMovil> reports,FragmentManager f) {
        Reports = reports;
        fragment=f;
    }
    public ProductoMovilAdapter() {
    }


    @Override
    public ProductoMovilAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_movil_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductoMovilAdapter.ViewHolder holder, int position) {
        try {
            final int pos = position;
            holder.label_nombre.setText(Reports.get(position).getNombre());
            holder.label_precio.setText(Reports.get(position).getPrecio().toString());
            holder.label_categoria.setText(Reports.get(position).getCategoria().getNombre());
            if(Reports.get(position).getImage()!=null && Reports.get(position).getImage()!="") {
                byte[] decodedString = Base64.decode(Reports.get(position).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.image.setImageBitmap(decodedByte);

                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment pd=new producto_detalle();
                        Bundle bundle =new Bundle();
                        bundle.putLong("Id",Reports.get(pos).getId());
                        pd.setArguments(bundle);

                        fragment.beginTransaction().replace(R.id.contenedor_principal , pd).addToBackStack( "tag" ).commit();;
                    }
                });
            }

        }
        catch (Exception e){
            Log.e("Null","Dato vacio");
        }

    }

    @Override
    public int getItemCount() {
        return Reports.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView label_nombre;
        TextView label_precio;
        TextView label_categoria;
        ImageView image;
        int Id;


        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.item_producto);
            label_nombre = (TextView) itemView.findViewById(R.id.Nombre);
            label_precio = (TextView) itemView.findViewById(R.id.Precio);
            label_categoria = (TextView) itemView.findViewById(R.id.Categoria);
            image = (ImageView) itemView.findViewById(R.id.imagen_producto);
            /*
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment pd=new producto_detalle();
                    fragment.beginTransaction().replace(R.id.contenedor_principal , pd).addToBackStack( "tag" ).commit();;
                }
            });
            */
        }

    }
}
