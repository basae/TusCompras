package tucodigo.tuscompras.Models;

public class ProductoMovil {
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double precio) {
        Precio = precio;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Generico getCategoria() {
        return Categoria;
    }

    public void setCategoria(Generico categoria) {
        Categoria = categoria;
    }

    private Long Id;
    private String Nombre;
    private Double Precio;
    private String FechaCreacion;
    private String Image;
    private Generico Categoria;

    public  ProductoMovil(){}

    public ProductoMovil(Long id) {
        Id = id;
    }
}