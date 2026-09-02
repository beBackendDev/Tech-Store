package net.myapplication.myapp.object.product.service.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.product.service.CloudinaryService;

@Service
@RequiredArgsConstructor
public class CloudinaryTestService {

    private final CloudinaryService cloudinaryService;

    public void test() {
        try{
            ClassPathResource resource = new ClassPathResource(
                "data/data-pipeline/image/BAD-000001.jpeg");
          File imageFile =
                resource.getFile();

        String url = cloudinaryService.uploadProductImage(
                imageFile,
                "BAD-000001");

        System.out.println(
                "Cloudinary URL: " + url);

         } catch (IOException e) {

            throw new RuntimeException(
                "Cannot load test image",
                e
            );
    }
}
}