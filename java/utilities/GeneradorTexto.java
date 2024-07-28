package utilities;

/**
 * La interfaz OperacionTexto define el contrato para realizar operaciones en un texto.
 */
public interface GeneradorTexto {

    /**
     * Realiza una operación en un texto dado.
     * 
     * @param texto El texto en el que se realizará la operación.
     * @return El resultado de la operación aplicada al texto.
     */
    public String realizarOperacion(String Prompt, String texto);
}
