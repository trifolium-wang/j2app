package co.tunan.j2app.plugin.maven;

import co.tunan.j2app.plugin.maven.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title:
 * @author: wangxudong
 * @date: 2018/11/28 12:57
 * @version: 1.0
 * @modified :
 */
@Mojo(name = "j2app", defaultPhase = LifecyclePhase.PACKAGE)
public class J2AppMojo extends AbstractMojo {
    private static final Pattern PARAMETER_PLACEHOLDER = Pattern.compile("(#\\{)([\\w]+)(\\})");

    private static final String appTemplateDir = "/__app_template" + File.separator;
    private static final String defaultInfoPlistFile = "Info.plist";

    private Log log;

    /**
     * 应用名称
     */
    @Parameter(property = "j2app.appName")
    private String appName = "J2app";

    @Parameter(property = "j2app.jarName")
    private String jarName = "";

    /**
     * 版本
     */
    @Parameter(property = "j2app.version")
    private String appVersion = null;

    /**
     * 版权
     */
    @Parameter(property = "j2app.copyright")
    private String appCopyright = "Copyright © " + DateFormatUtils.format(new Date(), "yyyy") + " j2app";

    /**
     * 图标
     */
    @Parameter(property = "j2app.icon")
    private String appIcon = "icon.icns";

    /**
     * 身份id
     */
    @Parameter(property = "j2app.identifier")
    private String appIdentifier = "";

    /**
     * 输出目录
     */
    @Parameter(property = "j2app.outDir")
    private String outDir = null;


    /**
     * 运行参数
     */
    @Parameter(property = "j2app.arguments")
    private String arguments = "";

    @Override
    public void execute() throws MojoExecutionException {
        log = getLog();

        log.info("j2app packaging...");

        MavenProject mavenProject = (MavenProject) getPluginContext().get("project");
        if (mavenProject == null) {
            throw new MojoExecutionException("Unable to get maven context content");
        }

        Map<String, String> param = getParam(mavenProject);

        try {
            // 创建app文件目录
            String appFile = createAppFile(mavenProject);

            // 创建app内默认文件
            initAppFile(appFile);

            // 替换plist中的参数
            replacePlist(appFile + File.separator + defaultInfoPlistFile, param);

            // 替换应用自定义配置文件
            replaceAppConfigFile(mavenProject, appFile);

            // 替换sh 文件参数
            replaceSh(appFile + File.separator + "MacOS" + File.separator + "j2app.sh", param);

            String appJarPath = appFile + File.separator + "MacOS" + File.separator + "app.jar";

            String artifactFile;
            if (StringUtils.isBlank(jarName)) {
                artifactFile = mavenProject.getArtifact().getFile().getAbsolutePath();
            } else {
                artifactFile = mavenProject.getBuild().getDirectory() + File.separator + jarName;
            }
            // 将jar包复制到lib下面
            FileUtil.copyFile(artifactFile, appJarPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void initAppFile(String appFile) throws IOException {

        Files.createDirectories(Paths.get(appFile + File.separator + "MacOS"));
        Files.createDirectories(Paths.get(appFile + File.separator + "Resources" + File.separator + "lib"));

        // 从模板中复制模板文件
        FileUtil.copyFile(this.getClass().getResourceAsStream(appTemplateDir + "Info.plist"),
                appFile + File.separator + "Info.plist");
        FileUtil.copyFile(this.getClass().getResourceAsStream(appTemplateDir + "PkgInfo"),
                appFile + File.separator + "PkgInfo");

        FileUtil.copyFile(this.getClass().getResourceAsStream(appTemplateDir + "MacOS" + File.separator + "j2app.sh"),
                appFile + File.separator + "MacOS" + File.separator + "j2app.sh");

        FileUtil.copyFile(this.getClass().getResourceAsStream(appTemplateDir + "Resources"
                        + File.separator + "icon.icns"),
                appFile + File.separator + "Resources" + File.separator + appIcon);
    }

    private Map<String, String> getParam(MavenProject mavenProject) {
        log.info("appName:" + appName);

        if (StringUtils.isBlank(appVersion)) {
            appVersion = mavenProject.getVersion();
        }
        log.info("appVersion:" + appVersion);
        log.info("appIcon:" + appIcon);
        log.info("appCopyright:" + appCopyright);

        if (StringUtils.isBlank(appIdentifier)) {
            appIdentifier = mavenProject.getGroupId() + "." + mavenProject.getArtifactId();
        }
        log.info("appIdentifier:" + appIdentifier);

        Map<String, String> param = new HashMap<>(5);
        param.put("appName", appName);
        param.put("appVersion", appVersion);
        param.put("appIcon", appIcon);
        param.put("appIdentifier", appIdentifier);
        param.put("appCopyright", appCopyright);

        param.put("dateTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        param.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        return param;
    }

    private String createAppFile(MavenProject mavenProject) throws IOException {
        // 创建输出目录文件

        String outputDir = mavenProject.getBuild().getDirectory();
        if (StringUtils.isNoneBlank(outDir)) {
            outputDir = mavenProject.getBasedir() + File.separator + outDir;
        }

        if (StringUtils.isBlank(outputDir)) {
            outputDir = mavenProject.getBasedir() + File.separator + "target";
        }

        File appFile = new File(outputDir + File.separator + appName + ".app" + File.separator + "Contents");
        Files.createDirectories(Paths.get(appFile.getPath()));

        return appFile.getPath();
    }

    private void replaceAppConfigFile(MavenProject mavenProject, String appFilePath) throws IOException {
        List<Resource> resourceList = mavenProject.getResources();
        // 替换文件
        for (Resource resource : resourceList) {
            String resourcePath = resource.getDirectory();

            String iconPath = resourcePath + File.separator + appIcon;
            String infoPlistFilePath = resourcePath + File.separator + defaultInfoPlistFile;

            File iconFileFile = new File(iconPath);
            if (iconFileFile.exists() && iconFileFile.isFile() && iconFileFile.canRead()) {
                FileUtil.copyFile(iconPath, appFilePath + File.separator + "Resources"
                        + File.separator + appIcon);
            }

            File infoPlistFileFile = new File(infoPlistFilePath);
            if (infoPlistFileFile.exists() && infoPlistFileFile.isFile() && infoPlistFileFile.canRead()) {
                FileUtil.copyFile(infoPlistFilePath, appFilePath + File.separator + infoPlistFilePath);
            }
        }
    }

    private void replaceSh(String shFile, Map<String, String> kvs) throws IOException {
        Path path = Paths.get(shFile);
        List<String> sh = Files.readAllLines(path, StandardCharsets.UTF_8);

        Matcher m = PARAMETER_PLACEHOLDER.matcher(StringUtils.join(sh, "\n"));
        StringBuffer sr = new StringBuffer();
        Map<String, String> reNameKvs = new HashMap<>(1);
        kvs.forEach((k, v) -> reNameKvs.put("#{" + k.trim() + "}", v));
        while (m.find()) {
            String group = m.group();
            String reName = reNameKvs.get(group);
            if (reName != null) {
                m.appendReplacement(sr, reName);
            } else {
                m.appendReplacement(sr, "");
            }
        }
        m.appendTail(sr);

        Files.write(path, sr.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void replacePlist(String plistFile, Map<String, String> kvs) throws IOException {
        Path path = Paths.get(plistFile);
        List<String> plist = Files.readAllLines(path, StandardCharsets.UTF_8);
        Matcher m = PARAMETER_PLACEHOLDER.matcher(StringUtils.join(plist, "\n"));
        StringBuffer sr = new StringBuffer();
        Map<String, String> reNameKvs = new HashMap<>(1);
        kvs.forEach((k, v) -> reNameKvs.put("#{" + k.trim() + "}", v));
        while (m.find()) {
            String group = m.group();
            String reName = reNameKvs.get(group);
            if (reName != null) {
                m.appendReplacement(sr, reName);
            } else {
                m.appendReplacement(sr, "");
            }
        }
        m.appendTail(sr);

        Files.write(path, sr.toString().getBytes(StandardCharsets.UTF_8));
    }

}
