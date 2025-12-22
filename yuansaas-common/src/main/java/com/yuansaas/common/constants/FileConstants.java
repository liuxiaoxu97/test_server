package com.yuansaas.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 文件相关常量
 *
 * @author HTB 2025/7/25 11:07
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileConstants {

    // 文件扩展名
    public static final String EXTENSION_JSON = ".json";
    public static final String EXTENSION_XML = ".xml";
    public static final String EXTENSION_PROPERTIES = ".properties";
    public static final String EXTENSION_YAML = ".yaml";
    public static final String EXTENSION_YML = ".yml";
    public static final String EXTENSION_CSV = ".csv";
    public static final String EXTENSION_XLS = ".xls";
    public static final String EXTENSION_XLSX = ".xlsx";
    public static final String EXTENSION_PDF = ".pdf";
    public static final String EXTENSION_DOC = ".doc";
    public static final String EXTENSION_DOCX = ".docx";
    public static final String EXTENSION_PPT = ".ppt";
    public static final String EXTENSION_PPTX = ".pptx";
    public static final String EXTENSION_TXT = ".txt";
    public static final String EXTENSION_LOG = ".log";
    public static final String EXTENSION_ZIP = ".zip";
    public static final String EXTENSION_JAR = ".jar";
    public static final String EXTENSION_WAR = ".war";
    public static final String EXTENSION_JPG = ".jpg";
    public static final String EXTENSION_JPEG = ".jpeg";
    public static final String EXTENSION_PNG = ".png";
    public static final String EXTENSION_GIF = ".gif";
    public static final String EXTENSION_BMP = ".bmp";
    public static final String EXTENSION_SVG = ".svg";

    // 文件大小
    public static final long KB = 1024;
    public static final long MB = KB * 1024;
    public static final long GB = MB * 1024;
    public static final long TB = GB * 1024;

    public static final long MAX_FILE_SIZE_5MB = 5 * MB;
    public static final long MAX_FILE_SIZE_10MB = 10 * MB;
    public static final long MAX_FILE_SIZE_20MB = 20 * MB;
    public static final long MAX_FILE_SIZE_50MB = 50 * MB;
    public static final long MAX_FILE_SIZE_100MB = 100 * MB;
}
