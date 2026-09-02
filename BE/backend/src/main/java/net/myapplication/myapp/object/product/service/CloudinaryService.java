package net.myapplication.myapp.object.product.service;

import java.io.File;

public interface CloudinaryService {
    public String uploadProductImage(
            File imageFile,
            String externalId
    );
}
