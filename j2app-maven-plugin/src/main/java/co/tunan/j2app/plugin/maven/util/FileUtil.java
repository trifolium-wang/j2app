package co.tunan.j2app.plugin.maven.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * Created by wangxudong on 2020/07/02.
 *
 * @version: 1.0
 * @modified :
 */
public class FileUtil {

    /**
     * 设置权限为 rwxr-xr-x
     */
    private static Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
    private static FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions
            .asFileAttribute(permissions);

    public static void copyFile(String fromFile, String toFile) throws IOException {
        Files.deleteIfExists(Paths.get(toFile));
        Files.createFile(Paths.get(toFile), fileAttributes);
        // 使用字节流复制，否则可能会有权限问题
        FileUtils.copyFile(new File(fromFile), new File(toFile));
    }

    public static void copyFile(InputStream inputStream, String toFile) throws IOException {
        Files.deleteIfExists(Paths.get(toFile));
        Files.createFile(Paths.get(toFile), fileAttributes);
        // 使用字节流复制，否则可能会有权限问题
        FileUtils.copyInputStreamToFile(inputStream, new File(toFile));
    }

}
