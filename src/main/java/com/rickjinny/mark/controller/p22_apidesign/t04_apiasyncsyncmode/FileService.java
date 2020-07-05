package com.rickjinny.mark.controller.p22_apidesign.t04_apiasyncsyncmode;

import com.rickjinny.mark.controller.p22_apidesign.t04_apiasyncsyncmode.bean.*;
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
    // 线程池
    private ExecutorService threadPool = Executors.newFixedThreadPool(2);
    // 计数器, 作为上传任务的id
    private AtomicInteger atomicInteger = new AtomicInteger(0);
   // 暂存上传操作的结果, 生产代码需要考虑数据持久化
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

    /**
     * 同步上传操作：
     * 所谓同步处理，接口一定是同步上传原文件和缩略图，调用方可以自己选择调用超时, 如果来得及可以一直等待上传完成, 如果等不及可以结束等待，下一次再重试。
     * 同步上传接口，把超时的选择留给客户端。
     */
    public SyncUploadResponse syncUpload(SyncUploadRequest request) {
        SyncUploadResponse response = new SyncUploadResponse();
        response.setDownloadUrl(uploadFile(request.getFile()));
        response.setThumbnailDownloadUrl(uploadThumbnailFile(request.getFile()));
        return response;
    }

    /**
     * 异步上传操作：
     * 所谓异步上传，接口是两段式的，上传接口本身只是返回一个任务id，然后异步做上传操作，上传接口响应很快，客户端需要之后再拿着任务id，调用任务查询
     * 上传的文件 url。
     * 异步上传接口, 在出参上有点区别, 不再返回文件url, 而是返回一个任务id。
     */
    public AsyncUploadResponse asyncUpload(AsyncUploadRequest request) {
        AsyncUploadResponse response = new AsyncUploadResponse();
        // 生成唯一的上传任务id
        String taskId = "upload" + atomicInteger.incrementAndGet();
        // 异步上传操作, 只返回任务id
        response.setTaskId(taskId);
        // 提交上传原始文件，操作到线程池，异步处理
        threadPool.execute(() -> {
            String url = uploadFile(request.getFile());
            // 如果 ConcurrentHashMap 不包含 key, 则初始化一个 SyncQueryUploadTaskResponse, 然后设置 downloadUrl
            downloadUrl.computeIfAbsent(taskId, id -> new SyncQueryUploadTaskResponse(id)).setDownloadUrl(url);
        });
        // 提交上传缩略图，操作到线程池，异步处理
        threadPool.execute(() -> {
            String url = uploadThumbnailFile(request.getFile());
            downloadUrl.computeIfAbsent(taskId, id -> new SyncQueryUploadTaskResponse(id)).setThumbnailDownloadUrl(url);
        });
        return response;
    }

    public SyncQueryUploadTaskResponse syncQueryUploadTask(SyncQueryUploadTaskRequest request) {
        SyncQueryUploadTaskResponse response = new SyncQueryUploadTaskResponse(request.getTaskId());
        response.setDownloadUrl(downloadUrl.getOrDefault(request.getTaskId(), response).getDownloadUrl());
        response.setThumbnailDownloadUrl(downloadUrl.getOrDefault(request.getTaskId(), response).getThumbnailDownloadUrl());
        return response;
    }
}
