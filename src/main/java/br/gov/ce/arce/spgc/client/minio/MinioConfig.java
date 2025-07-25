package br.gov.ce.arce.spgc.client.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.server.endpoint}")
    private String endpoint;
    @Value("${minio.server.access-key}")
    private String accessKey;
    @Value("${minio.server.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        try {
            return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        } catch (Exception e) {
            throw new RuntimeException("Erro no serviço do minio",e);
        }
    }

}
