package com.chenpu.backend.controller;

import java.io.File;

import com.chenpu.backend.domain.Bird;
import com.chenpu.backend.domain.Infer;
import com.chenpu.backend.domain.ResultObject;
import com.chenpu.backend.service.BirdService;
import com.chenpu.backend.utils.AudioFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/audio")
@CrossOrigin
public class AudioController {
    @Autowired
    private BirdService birdService;

    @PostMapping("/upload")
    public ResultObject uploadAudio(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResultObject(400, "上传文件为空", null);
        }

        try {
            // 将 MultipartFile 转换为临时文件
            String fixedDirectory = "D:/BirdWork/Model/AudioClassification-Pytorch/dataset/target_file/"; // 这里修改为你希望存储的目录
            File targetFile = new File(fixedDirectory + "target.wav");
            // Infer result = birdService.inferBird();
            Bird result = birdService.getRandomBird();
            return new ResultObject(200, "上传成功", result);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObject(500, "服务器错误: " + e.getMessage(), null);
        }
    }
}