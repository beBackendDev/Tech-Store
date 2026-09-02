package net.myapplication.myapp.object.product.service.impl;

import java.io.File;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.product.service.CloudinaryService;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadProductImage(
            File imageFile,
            String externalId) {

        try {

            Map<?, ?> result = cloudinary.uploader().upload(
                    imageFile,
                    ObjectUtils.asMap(
                            "folder",
                            "tech-store/laptops",

                            "public_id",
                            externalId,

                            "resource_type",
                            "image",

                            "overwrite",
                            true));

            return (String) result.get("secure_url");

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to upload image: "
                            + externalId,
                    e);
        }
    }
}
