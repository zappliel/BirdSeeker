package com.chenpu.backend.utils;

import com.chenpu.backend.domain.Bird;
import com.chenpu.backend.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class AudioFileUtil {
    private static BirdService birdService;
    public static void createDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static File saveFile(String filePath, byte[] fileBytes) throws IOException {
        File file = new File(filePath);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
        }
        return file;
    }

    public static Bird getAudioInfo(File audio_file){
        return birdService.getRandomBird();
    }
}
