package seguridad;

import DAOS.BitacoraDAO;

/**
 *
 * @author chave
 */
public class Csv extends Bitacoras {

    @Override
    public String formato(String autor, String info) {
        StringBuilder csvTemplate = new StringBuilder();
        csvTemplate.append("Autor,InformacionCambio,DireccionIP,HoraDelSistema,Ubicacion,SistemaOperativo\n");
        csvTemplate.append(autor).append(",").append(info).append(",").append(obtenerDireccionIPFinal()).append(",").append(obtenerFechaHora()).append(",").append(obtenerUbicacionPorIP()).append(",").append(obtenerSistemaOperativo());

        // Retornar la plantilla CSV generada
        BitacoraDAO bitacora = new BitacoraDAO(getBase());
        bitacora.agregarEntradaBitacora(csvTemplate.toString(), autor, "CSV");
        desconectar();
        return csvTemplate.toString();
    }

}
