package com.ca.directory.jxplorer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;

@SpringBootApplication
public class JXplorerApplication extends JFrame {

    public static void main(String... args) throws Exception {

        new SpringApplicationBuilder(JXplorerApplication.class).headless(false).run(args);
    }

}
