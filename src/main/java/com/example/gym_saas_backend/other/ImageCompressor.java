package com.example.gym_saas_backend.other;

import net.coobird.thumbnailator.Thumbnails;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageCompressor {

    public static byte[] compressImage(byte[] inputImage) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(inputImage);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Thumbnails.of(in)
                .scale(1.0)               // no resizing
                .outputQuality(0.5f)      // 50% quality
                .outputFormat("jpg")
                .toOutputStream(out);

        return out.toByteArray();
    }
}

