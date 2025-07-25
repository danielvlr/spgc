package br.gov.ce.arce.spgc.client.minio;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String minioBucketName;

    @PostConstruct
    public void verifyBucket() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(minioBucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(minioBucketName).build()
                );
                log.info("Bucket '{}' criado com sucesso.", minioBucketName);
            }
        } catch (Exception e) {
            log.error("Erro ao verificar ou criar bucket: {}", e.getMessage());
            throw new RuntimeException("Erro ao verificar ou criar bucket no MinIO", e);
        }
    }

    public String saveFileBase64(String base64Content, String nomeArquivo, String tipoDocumento, Long id) {
        verifyBucket();
        String objectName = tipoDocumento + "/" + id + "/" + nomeArquivo;

        try {
            byte[] fileBytes = Base64.getDecoder().decode(base64Content);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioBucketName)
                            .object(objectName)
                            .stream(inputStream, fileBytes.length, -1)
                            .contentType("application/pdf")
                            .build()
            );

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioBucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry((int) Duration.ofHours(1).toSeconds())
                            .build()
            );

        } catch (Exception e) {
            log.error("Erro ao salvar arquivo base64 no MinIO: {}", e.getMessage());
            throw new RuntimeException("Erro ao salvar arquivo base64", e);
        }
    }
}
