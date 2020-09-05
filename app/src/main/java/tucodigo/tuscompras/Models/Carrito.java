package tucodigo.tuscompras.Models;

public class Carrito {
    private ProductoMovil Producto;
    private Integer Cantidad;
    private Double Subtotal;
    private Double Iva;
    private Double Total;

    public ProductoMovil getProducto() {
        return Producto;
    }

    public void setProducto(ProductoMovil producto) {
        Producto = producto;
    }

    public Integer getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Integer cantidad) {
        Cantidad = cantidad;
    }

    public Double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(Double subtotal) {
        Subtotal = subtotal;
    }

    public Double getIva() {
        return Iva;
    }

    public void setIva(Double iva) {
        Iva = iva;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public Carrito() {
    }
    public Carrito(ProductoMovil producto, Integer cantidad, Double subtotal, Double iva, Double total) {
        Producto = producto;
        Cantidad = cantidad;
        Subtotal = subtotal;
        Iva = iva;
        Total = total;
    }
}
