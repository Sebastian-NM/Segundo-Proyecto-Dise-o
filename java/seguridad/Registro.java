package seguridad;

public class Registro {
    
    public String fecha;
    public String tipo;
    public String info;
    public String autor;
    
    public Registro(String fecha, String tipo, String info, String autor) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.info=info;
        this.autor=autor;
    }

    public Registro(String fecha, String tipo, String info) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.info=info;
    }
   
}