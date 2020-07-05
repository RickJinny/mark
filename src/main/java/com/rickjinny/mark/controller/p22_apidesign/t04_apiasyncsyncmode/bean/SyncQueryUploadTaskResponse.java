package com.rickjinny.mark.controller.p22_apidesign.t04_apiasyncsyncmode.bean;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SyncQueryUploadTaskResponse {
    // 任务id
    private final String taskId;
    // 原始文件下载url
    private String downloadUrl;
    // 缩略图下载url
    private String thumbnailDownloadUrl;
}
