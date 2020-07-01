package com.wxd.j2app.plugin.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @title:
 * @author: wangxudong
 * @date: 2018/11/28 12:57
 * @version: 1.0
 * @modified :
 * @goal j2app
 */
@Mojo(name = "j2app", defaultPhase = LifecyclePhase.PACKAGE)
public class J2AppMojo extends AbstractMojo {

    @Parameter(property = "j2app.infoPlistFile")
    private String infoPlistFile = "Info.plist";

    @Parameter(property = "j2app.appName")
    private String appName = "J2app";

    @Parameter(property = "j2app.iconFile")
    private String iconFile = "icon.icns";

    @Parameter(property = "j2app.PkgInfo")
    private String pkgInfo = "PkgInfo";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("j2app packaging...");
        log.info("infoPlistFile:" + infoPlistFile);
        log.info("appName:" + appName);
        log.info("iconFile:" + iconFile);
        log.info("PkgInfo:" + pkgInfo);

//        Map<Object, Object> pluginContext = getPluginContext();
//
//        pluginContext.forEach((o, o2) -> {
//            log.info(o + ":" + o2);
//        });

    }
}
