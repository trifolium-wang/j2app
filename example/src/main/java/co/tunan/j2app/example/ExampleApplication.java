package co.tunan.j2app.example;

import javax.swing.*;
import java.awt.*;

/**
 * @author Trifolium
 */
public class ExampleApplication extends JFrame {

    public static void main(String[] args) {
        start();
    }

    public static void start() {

        System.out.println("hello j2app.");
        JFrame jf = new JFrame("Hello J2app!");
        Container container = jf.getContentPane();
        JLabel jl = new JLabel("HELLO J2APP");
        //使标签上的文字居中
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(jl);
        container.setBackground(Color.white);
        jf.setVisible(true);
        jf.setSize(400, 300);
        //设置窗体关闭方式
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
