package com.wxd.j2app.plugin.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @title:
 * @author: wangxudong
 * @date: 2018/11/28 13:32
 * @version: 1.0
 * @modified :
 */
@Mojo(name="help")
public class HelpMojo extends AbstractMojo{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("这里是帮助文档");
    }
}
