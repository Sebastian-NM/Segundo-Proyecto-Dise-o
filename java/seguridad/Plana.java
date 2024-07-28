package seguridad;

import DAOS.BitacoraDAO;

/**
 *
 * @author chave
 */
public class Plana extends Bitacoras {

    @Override
    public String formato(String autor, String info) {
        String trama = "";
        trama += "Autor:";
        trama += autor;
        trama += "Informacion:";
        trama += info;
        trama += "IP:";
        trama += obtenerDireccionIPFinal();
        trama += "Hora";
        trama += obtenerFechaHora();
        trama += "Ubicacion";
        trama += obtenerUbicacionPorIP();
        trama += "SistemaOperativo:";
        trama += obtenerSistemaOperativo();

        BitacoraDAO bitacora = new BitacoraDAO(getBase());
        bitacora.agregarEntradaBitacora(trama, autor, "PLANA");
        desconectar();
        return trama;
    }

}
