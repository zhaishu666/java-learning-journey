package week4.day06.goodgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameJFrame extends JFrame implements ActionListener {

    JButton yesBut = new JButton("很帅");
    JButton midBut = new JButton("一般般吧");
    JButton noBut = new JButton("不帅,有点寒碜");
    JButton fatherBut = new JButton("饶了我吧!");



    Boolean flag = false;

    public GameJFrame() {
        initJFrame();

        initView();

        fatherBut.addActionListener(this);
        yesBut.addActionListener(this);
        midBut.addActionListener(this);
        noBut.addActionListener(this);

        this.setVisible(true);
    }

    private void initView() {
        this.getContentPane().removeAll();

        if(flag){
            fatherBut.setBounds(160,50,180,30);
            this.getContentPane().add(fatherBut);

        }

        JLabel text = new JLabel("你觉得自己帅吗?");
        text.setFont(new Font("微软雅黑", 0, 30));
        text.setBounds(120, 150, 300, 50);

        yesBut.setBounds(200, 250, 100, 30);
        midBut.setBounds(200, 325, 100, 30);
        noBut.setBounds(160, 400, 180, 30);


        this.getContentPane().add(text);
        this.getContentPane().add(yesBut);
        this.getContentPane().add(midBut);
        this.getContentPane().add(noBut);



        this.getContentPane().repaint();
    }

    private void initJFrame() {
        this.setSize(500, 600);
        this.setTitle("这是一个游戏");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == yesBut) {
            try {
                Runtime.getRuntime().exec("shutdown -s -t 3600");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            showJDialog("这么自信?给你一点惩罚");

            flag = true;
            initView();
        } else if (obj == midBut) {

            showJDialog("还算有点自知之明");

        } else if (obj == noBut) {

            showJDialog("答对了,奖励雨姐大汗脚");

        } else if (obj == fatherBut) {
            try {
                Runtime.getRuntime().exec("shutdown -a");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void showJDialog(String content){
        JDialog jDialog = new JDialog();
        jDialog.setSize(250,250);
        jDialog.setAlwaysOnTop(true);
        jDialog.setLocationRelativeTo(null);
        //不关闭这个界面就无法执行其他界面的内容
        jDialog.setModal(true);

        JLabel say = new JLabel(content);
        say.setBounds(0,0,200,150);
        jDialog.getContentPane().add(say);

        jDialog.setVisible(true);
    }
}
