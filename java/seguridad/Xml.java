package seguridad;

import DAOS.BitacoraDAO;

public class Xml extends Bitacoras {

    @Override
    public String formato(String autor, String info) {
        String xmlTemplate = "<Informacion>\n"
                + "    <Autor>" + autor + "</Autor>\n"
                + "    <Cambio>" + info + "</Cambio>\n"
                + "    <Usuario>\n"
                + "        <DireccionIP>" + obtenerDireccionIPFinal() + "</DireccionIP>\n"
                + "        <HoraDelSistema>" + obtenerFechaHora() + "</HoraDelSistema>\n"
                + "        <Ubicacion>" + obtenerUbicacionPorIP() + "</Ubicacion>\n"
                + "        <SistemaOperativo>" + obtenerSistemaOperativo() + "</SistemaOperativo>\n"
                + "    </Usuario>\n"
                + "</Informacion>";
        BitacoraDAO bitacora = new BitacoraDAO(getBase());
        bitacora.agregarEntradaBitacora(xmlTemplate, autor, "XML");
        desconectar();
        return xmlTemplate;
    }

}
