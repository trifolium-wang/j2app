package com.wxd.j2app.plugin.maven;

import com.wxd.j2app.plugin.maven.util.FileUtil;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * @title:
 * @author: wangxudong
 * @date: 2018/11/28 12:57
 * @version: 1.0
 * @modified :
 */
@Mojo(name = "j2app", defaultPhase = LifecyclePhase.PACKAGE)
public class J2AppMojo extends AbstractMojo {

    private Log log;

    private static final String appTemplatePath = "/appTemplate";
    private static final String defaultinfoPlistFile = "Info.plist";
    private static final String defaulticonFile = "icon.icns";
    private static final String defaultpkgInfoFile = "PkgInfo";

    /**
     * 应用名称
     */
    @Parameter(property = "j2app.appName")
    private String appName = "J2app";

    /**
     * 运行参数
     */
    @Parameter(property = "j2app.arguments")
    private String arguments = "";

    /**
     * 输出目录
     */
    @Parameter(property = "j2app.outdir")
    private String outdir = "/target";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        log = getLog();
        log.info("j2app packaging...");

        log.info("appName:" + appName);

        MavenProject mavenProject = (MavenProject) getPluginContext().get("project");

        if (mavenProject == null) {
            throw new MojoFailureException("无法获取到maven上下文内容");
        }

        //List<File> resources = getCopyResource(mavenProject);


        // 创建输出目录文件
        String targetDir = mavenProject.getBasedir().getAbsolutePath() + outdir;
        File targetFile = new File(targetDir);
        if (!targetFile.exists()) {
            targetFile.mkdir();
        }

        targetDir = targetDir + File.separator + appName + ".app";
        File appFile = new File(targetDir);
        if (appFile.exists()) {
            appFile.delete();
        }
        appFile.mkdir();


        // 创建必要的文件
        try {
            new File(targetDir + File.separator + "Frameworks").mkdir();
            new File(targetDir + File.separator + "MacOS").mkdir();
            new File(targetDir + File.separator + "MacOS/J2app").createNewFile();

            new File(targetDir + File.separator + "Resources").mkdir();
            new File(targetDir + File.separator + "Resources/include").mkdir();
            new File(targetDir + File.separator + "Resources/lib").mkdir();
            new File(targetDir + File.separator + "Resources/icon.icns").createNewFile();
            new File(targetDir + File.separator + "Resources/j2app.sh").createNewFile();

            new File(targetDir + File.separator + "Info.plist").createNewFile();
            new File(targetDir + File.separator + "PkgInfo").createNewFile();
        }catch (IOException e){

            throw new MojoExecutionException(e.getMessage(), e);
        }

        // 写入文件内容
//        URL defaultResourcePath = this.getClass().getResourceAsStream(appTemplatePath);
//        try {
//            FileUtil.copyDir(defaultResourcePath.getFile(), targetDir);
//        } catch (IOException e) {
//            throw new MojoExecutionException(e.getMessage(), e);
//        }


        // jar包
        //mavenProject.getArtifact().getFile();


    }

//    /**
//     * 获取要复制的资源
//     *
//     * @param mavenProject
//     * @return
//     */
//    private List<File> getCopyResource(MavenProject mavenProject) {
//        List<Resource> resourceList = mavenProject.getResources();
//
//        String defaultResourcePath = this.getClass().getResource(appTemplatePath).getPath();
//        log.info(defaultResourcePath);
//
//        File iconFileFile = null;
//        File pkgInfoFileFile = null;
//        File infoPlistFileFile = null;
//
//        for (Resource resource : resourceList) {
//            String resourcePath = resource.getDirectory();
//
//            String iconPath = resourcePath + File.separator + defaulticonFile;
//            String pkgInfoPath = resourcePath + File.separator + defaultpkgInfoFile;
//            String infoPlistFilePath = resourcePath + File.separator + defaultinfoPlistFile;
//
//            iconFileFile = new File(iconPath);
//            if (!iconFileFile.exists() || !iconFileFile.isFile() || !iconFileFile.canRead()) {
//                iconFileFile = null;
//            }
//
//            pkgInfoFileFile = new File(pkgInfoPath);
//            if (!pkgInfoFileFile.exists() || !pkgInfoFileFile.isFile() || !pkgInfoFileFile.canRead()) {
//                pkgInfoFileFile = null;
//            }
//
//            infoPlistFileFile = new File(infoPlistFilePath);
//            if (!infoPlistFileFile.exists() || !infoPlistFileFile.isFile() || !infoPlistFileFile.canRead()) {
//                infoPlistFileFile = null;
//            }
//        }
//
//        if (iconFileFile == null) {
//            iconFileFile = new File(defaultResourcePath+ File.separator + "Resource"
//                    + File.separator + defaulticonFile);
//        }
//        if (pkgInfoFileFile == null) {
//            pkgInfoFileFile = new File(defaultResourcePath+ File.separator + defaultpkgInfoFile);
//        }
//        if (infoPlistFileFile == null) {
//            infoPlistFileFile = new File(defaultResourcePath+ File.separator + defaultinfoPlistFile);
//        }
//
//        return Arrays.asList(iconFileFile, pkgInfoFileFile, infoPlistFileFile);
//    }

}
