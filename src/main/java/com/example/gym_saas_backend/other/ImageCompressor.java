package com.example.gym_saas_backend.other;

import net.coobird.thumbnailator.Thumbnails;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageCompressor {

    public static byte[] compressImage(byte[] inputImage) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(inputImage);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Thumbnails.of(in)
                    .scale(1.0)
                    .outputQuality(0.5f)
//                    .outputFormat("jpg")
                    .toOutputStream(out);
        } catch (IOException e) {
            throw new IOException("Image compression failed. Ensure uploaded file is a valid image.", e);
        }

        return out.toByteArray();
    }

}

