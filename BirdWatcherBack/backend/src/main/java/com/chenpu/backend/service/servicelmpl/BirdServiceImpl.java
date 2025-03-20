package com.chenpu.backend.service.servicelmpl;

import com.chenpu.backend.domain.Bird;
import com.chenpu.backend.domain.Infer;
import com.chenpu.backend.mapper.BirdMapper;
import com.chenpu.backend.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chenpu.backend.utils.crawer;
import com.chenpu.backend.utils.KeyWordCrawer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.List;

@Service
public class BirdServiceImpl implements BirdService {
    @Autowired
    private BirdMapper birdMapper;

    @Override
    public Bird getBirdByName(String name) {
        Bird bird_info =  birdMapper.findByName(name);
        if(bird_info == null){
            try{
                bird_info = crawer.getBirdSql(name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return bird_info;
    }

    @Override
    public List<String> getBirdByKeyWord(String word) {
        try {
            return KeyWordCrawer.getBirdList(word);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insertBird(Bird bird_info) {
        return birdMapper.insert(bird_info);
    }

    @Override
    public Bird getRandomBird() {
        return birdMapper.getRandom();
    }

    @Override
    public List<Bird> getSixBirds() {
        return birdMapper.getSixRandomBirds();
    }

    @Override
    public Infer inferBird() {
        try {
            // Python 解释器路径（如 Python3，请改为 "python3"）
            String pythonInterpreter = "python";

            // Python 脚本路径
            String scriptPath = "D:/BirdWork/Model/AudioClassification-Pytorch/infer.py"; // 替换为你的 infer.py 绝对路径

            // 音频文件路径
            String audioPath = "dataset/target_file/target.wav";

            // 构造命令
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonInterpreter, scriptPath, "--audio_path=" + audioPath
            );

            // 启动进程
            Process process = processBuilder.start();

            // 读取 Python 标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String outputLine;
            String label = null;
            double score = 0.0;

            while ((outputLine = reader.readLine()) != null) {
                System.out.println("Python Output: " + outputLine);

                // 解析 Python 输出，例如：音频的预测结果标签为：Speech，得分：0.95
                if (outputLine.contains("音频的预测结果标签为")) {
                    String[] parts = outputLine.split("：|，得分：");
                    if (parts.length == 3) {
                        label = parts[1].trim();
                        score = Double.parseDouble(parts[2].trim());
                    }
                }
            }

            // 读取错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("Python Error: " + errorLine);
            }

            // 等待 Python 进程执行完毕
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

            // 输出结果
            if (label != null) {
                return new Infer(label,score);
            } else {
                return null;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
