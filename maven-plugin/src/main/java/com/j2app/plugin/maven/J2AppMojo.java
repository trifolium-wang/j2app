package com.j2app.plugin.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @title:
 * @author: wangxudong
 * @date: 2018/11/28 12:57
 * @version: 1.0
 * @modified :
 */
@Mojo(name = "j2app", defaultPhase = LifecyclePhase.PACKAGE)
public class J2AppMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("阿斯顿发惊世毒妃士大夫阿什顿发 大是大非");
    }
}
