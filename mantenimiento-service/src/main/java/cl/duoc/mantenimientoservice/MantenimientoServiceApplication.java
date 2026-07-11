package cl.duoc.mantenimientoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MantenimientoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MantenimientoServiceApplication.class, args);
    }
}