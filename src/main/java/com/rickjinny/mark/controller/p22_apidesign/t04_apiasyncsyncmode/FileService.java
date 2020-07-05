package com.rickjinny.mark.controller.p22_apidesign.t04_apiasyncsyncmode;

import com.rickjinny.mark.controller.p22_apidesign.t04_apiasyncsyncmode.bean.SyncQueryUploadTaskResponse;
import com.rickjinny.mark.controller.p22_apidesign.t04_apiasyncsyncmode.bean.UploadRequest;
import com.rickjinny.mark.controller.p22_apidesign.t04_apiasyncsyncmode.bean.UploadResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 有一个文件上传服务 FileService, 其中一个 upload 文件上传接口特别慢，原因是这个上传接口在内部需要进行两步操作。
 * 首先是上传原图, 然后压缩上传缩略图。如果每一步都耗时 5 秒的话，那么这个接口返回至少需要 10 秒的时间。
 */
@Service
public class FileService {

    private ExecutorService threadPool = Executors.newFixedThreadPool(2);

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private ConcurrentHashMap<String, SyncQueryUploadTaskResponse> downloadUrl = new ConcurrentHashMap<>();

    private String uploadFile(byte[] data) {
        try {
            TimeUnit.MILLISECONDS.sleep(500 + ThreadLocalRandom.current().nextInt(1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.demo.com/download/" + UUID.randomUUID().toString();
    }

    private String uploadThumbnailFile(byte[] data) {
        try {
            TimeUnit.MILLISECONDS.sleep(1500 + ThreadLocalRandom.current().nextInt(1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.demo.com/download/" + UUID.randomUUID().toString();
    }

    /**
     * 上传文件
     */
    public UploadResponse upload(UploadRequest request) {
        UploadResponse response = new UploadResponse();
        // 上传原始文件任务，提交到线程池处理
        Future<String> uploadFile = threadPool.submit(() -> uploadFile(request.getFile()));
        // 上传缩略图任务，提交到线程池处理
        Future<String> uploadThumbnailFile = threadPool.submit(() -> uploadThumbnailFile(request.getFile()));
        // 等待上传原始文件任务完成, 最多等待1秒
        try {
            response.setDownloadUrl(uploadFile.get(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 等待上传缩略图任务完成, 最多等待1秒
        try {
            response.setThumbnailDownloadUrl(uploadThumbnailFile.get(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
