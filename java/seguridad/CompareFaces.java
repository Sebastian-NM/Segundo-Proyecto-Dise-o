package seguridad;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CompareFaces {

    private final String accessKeyId = "AKIA5FTZFLYV4MJFADM4";
    private final String secretKey = "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";
    private final String carpetaRostros = "C:\\Users\\sjwor\\Desktop\\DesignProyect\\src\\main\\java\\utilities\\rostrosRegistrados\\";

    public String compararRostrosConCarpeta(ByteBuffer imagen) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretKey);
        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion("us-east-2")
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        List<File> imagenesCarpeta = obtenerImagenesCarpeta(carpetaRostros);
        if (imagenesCarpeta.isEmpty()) {
            System.err.println("La carpeta no contiene imágenes.");
            return null;
        }

        try {
            CompareFacesRequest testRequest = new CompareFacesRequest()
                    .withSourceImage(new Image().withBytes(imagen))
                    .withTargetImage(new Image().withBytes(imagen))
                    .withSimilarityThreshold(80F);

            rekognitionClient.compareFaces(testRequest);
        } catch (AmazonRekognitionException e) {
            if (e.getErrorCode().equals("InvalidParameterException")) {
                return null;
            } else {
                e.printStackTrace();
                return null;
            }
        }

        for (File imagenCarpeta : imagenesCarpeta) {
            try {
                ByteBuffer imagenCarpetaBytes = loadImageBytes(imagenCarpeta.getAbsolutePath());
                CompareFacesRequest compareFacesRequest = new CompareFacesRequest()
                        .withSourceImage(new Image().withBytes(imagen))
                        .withTargetImage(new Image().withBytes(imagenCarpetaBytes))
                        .withSimilarityThreshold(95F);
                CompareFacesResult result = rekognitionClient.compareFaces(compareFacesRequest);
                List<CompareFacesMatch> faceMatches = result.getFaceMatches();
                if (!faceMatches.isEmpty()) {
                    String fileNameWithoutExtension = imagenCarpeta.getName().substring(0, imagenCarpeta.getName().lastIndexOf('.'));
                    return fileNameWithoutExtension;
                }
            } catch (AmazonRekognitionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private ByteBuffer loadImageBytes(String imagePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(imagePath));
        byte[] bytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return ByteBuffer.wrap(bytes);
    }

    private List<File> obtenerImagenesCarpeta(String carpetaPath) {
        File carpeta = new File(carpetaPath);
        File[] listaArchivos = carpeta.listFiles();
        List<File> imagenes = new ArrayList<>();

        if (listaArchivos != null) {
            for (File archivo : listaArchivos) {
                if (archivo.isFile() && esImagen(archivo.getName())) {
                    imagenes.add(archivo);
                }
            }
        }

        return imagenes;
    }

    private boolean esImagen(String nombreArchivo) {
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif");
    }
}
