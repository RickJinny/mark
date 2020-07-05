package com.rickjinny.mark.controller.p22_apidesign.t04_apiasyncsyncmode.bean;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SyncQueryUploadTaskRequest {
    // 使用上传文件任务id，查询上传结果
    private final String taskId;
}
