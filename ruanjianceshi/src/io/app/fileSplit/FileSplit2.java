package io.app.fileSplit;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FileSplit2 extends JFrame {

    private JPanel jp ;
    private JLabel jlb;
    private JButton btnFileCut ;
    private JButton btnFileJoin ;
    private JLabel label;
    private JLabel label1;
    private Font font;

    public FileSplit2() {
        super("chx版权所有！");
        initComponents();
    }

    private void initComponents() {
        //标签
        label1 = new JLabel("        文件切割合并工具1.0");



        font = new Font(null, Font.BOLD, 28);

        //设置背景图片！！！！！！！！！！！！！
        label = new javax.swing.JLabel();//标签
        ImageIcon im = new javax.swing.ImageIcon(
                "C:\\Users\\lenovo\\Pictures\\Camera Roll\\QQ截图20180629165555.png");
        label.setIcon(im); 
        //添加背景图片
        label.setBounds(0, 0, im.getIconWidth(), im.getIconHeight());
        //设置位置和大小
        jp = (JPanel) this.getContentPane();
        //把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明 

        jp.setOpaque(false);
        //内容窗格默认的布局管理器为null
        jp.setLayout(null);


        //文件切割按钮
        btnFileCut = new JButton("文件切割");
        btnFileCut.setSize(160, 100);
        //btnFileCut.setForeground(Color.RED);字体颜色
        btnFileCut.setBackground(Color.pink);//背景颜色
        btnFileCut.setLocation(im.getIconWidth()*1/10,im.getIconHeight()*13/20);
        btnFileCut.setFont(font);

        jp.add(btnFileCut);

        //文件合并按钮
        btnFileJoin = new JButton("文件合并");
        btnFileJoin.setFont(font);
        btnFileJoin.setBackground(Color.pink);
        btnFileJoin.setBounds(im.getIconWidth()*13/20,im.getIconHeight()*13/20,160, 100);
        jp.add(btnFileJoin);

        btnFileCut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnFileCutActionPerformed(e);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });

        btnFileJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnFileJoinActionPerformed(e);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });


        label1.setBounds(20, 20, im.getIconWidth(), 50);
        label1.setFont(new Font(null,Font.BOLD, 40));
        label1.setForeground(Color.red);
        jp.add(label1);


        jlb = new JLabel();
        jlb.setBounds(btnFileCut.getX()+100, btnFileCut.getY()-400, 400 , 350);
        jlb.setFont(new Font(null, Font.BOLD, 25));
        jp.add(jlb);


         // 把背景图片添加到分层窗格的最底层作为背景   
        this.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        //为前面创建的标签 设置层的深度，这里将其设置为了最底层，因为Integer.MIN_VALUE是int类型的最小值了
        this.getLayeredPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //让关闭按钮起作用

        setBounds(new java.awt.Rectangle(200, 200,im.getIconWidth(), im.getIconHeight()));

        this.setResizable(false);//设置窗口不能缩放
    }

    //文件切割
    protected void btnFileCutActionPerformed(ActionEvent e) throws IOException {
        //System.out.println("文件切割！");

        JFileChooser jfc = new JFileChooser();
        int result = jfc.showOpenDialog(this);
        int num=0;
        File file = null;
        File desDir = null;
        if(result == JFileChooser.APPROVE_OPTION){
            file = jfc.getSelectedFile();
            //System.out.println(file.getName());//文件名
            //System.out.println(file.getParent());//绝对路径

            String strNum = JOptionPane.showInputDialog("请输入文件需要按照多少KB一个文件来分割！(默认为1024KB)", "1024");
            if(strNum==null){
                JOptionPane.showMessageDialog(this, "未给定分割文件的大小，此次文件分割已取消！");
                //System.out.println("end");
                return;
            }else{
                try {
                    num = Integer.parseInt(strNum);
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(this, "给定数字格式错误，此次文件分割已取消！");
                    return;
                }
            }

            String strs[] =file.getName().split("\\.");
            String str="";
            for(int i=0;i<strs.length-1;i++){
                //System.out.println(strs[i]);
                str= str +strs[i];
            }
            str = str+"SplitFile"+strs[strs.length-1];
            //System.out.println(str);
            desDir = new File(file.getParent(), str);
            //1明确源---硬盘,字节 file
            FileInputStream fis = new FileInputStream(file);
            //2明确目的---硬盘,字节 desDir
            FileOutputStream fos = null;
            if(!desDir.exists()){
                desDir.mkdir();
            }

            //3根据是否需要加强功能决定是否要再套接，然后进行读写
            BufferedInputStream bis = new BufferedInputStream(fis); 
            byte[] buf = new byte[1024];//每次读1KB
            int len = 0;
            int numLen = 0;
            int count=1;
            boolean flag = true;
            BufferedOutputStream bos = null;
            long kbing = 1;

            String fileName = null;
            while(true){
                if(flag){
                    fileName = strs[0]+(count++)+".chx";
                    fos = new FileOutputStream(desDir+"\\"+fileName);
                    bos = new BufferedOutputStream(fos);
                    flag=false;
                }

                if(numLen>=num){
                    //注意，输出时，一定要先关外层的流，再关里层的流。
                    //否则，外层输出流中的残留数据刷不出去! 输入流的关闭顺序则相反!
                    bos.close();
                    fos.close();
                    numLen=0;
                    flag=true;
                    continue;
                }

                len = bis.read(buf);

                if(len==-1){
                    bos.close();
                    fos.close();
                    break;
                }

                bos.write(buf, 0, len);
                numLen++;
                //jlb.setText("已经切割["+kbing*numLen+"]KB...");
            }
            fis.close();
            bis.close();
            jlb.setText("文件\""+file.getName()+"\"已经切割完！");
            this.validate();

        }
    }


    //文件合并
    protected void btnFileJoinActionPerformed(ActionEvent e) throws IOException {
        //System.out.println("文件合并！");
        //提示！！！
        JOptionPane.showMessageDialog(this, "请随便选择一个被切割成碎片的文件！\t\n" +
                "确保所有被切割的文件都在这层目录下，且不包含其他文件。\t\n" +
                "合并后的文件在当前目录下！");

        JFileChooser jfc = new JFileChooser();
        int result = jfc.showOpenDialog(this);
        File file = null;
        File desDir = null;
        String fileName = null;

        if(result==JFileChooser.APPROVE_OPTION){
            file = jfc.getCurrentDirectory();
            String str = file.getAbsolutePath();

            //注意！！！！！！！！！！！！！！！！！！！！
            /*
             '\'在java中是一个转义字符，所以需要用两个代表一个。
             例如System.out.println( "\\" ) ;只打印出一个"\"。
             但是'\'也是正则表达式中的转义字符（replaceAll 的参数就是正则表达式），
             需要用两个代表一个。所以：\\\\被java转换成\\,\\又被正则表达式转换成\。 
             */
            str = str.replaceAll("\\\\", "#chx#@");
            //System.out.println(str);
            String strs[] = str.split("#chx#@");
//          for(int i=0;i<strs.length;i++){
//              System.out.println(strs[i]);
//          }
            fileName = strs[strs.length-1];
            //System.out.println(fileName);
            strs = fileName.split("SplitFile");
            fileName = strs[0]+"."+strs[1];
            //System.out.println(fileName);


            //用序列流合并
            ArrayList<FileInputStream> ar = new ArrayList<FileInputStream>();
            for(int i=1;i<=file.listFiles().length;i++){
                ar.add(new FileInputStream(file+"\\"+strs[0]+i+".chx"));
            }
            Enumeration<FileInputStream> en = Collections.enumeration(ar);
            SequenceInputStream sis = new SequenceInputStream(en);


            FileOutputStream fos = new FileOutputStream(file+"\\"+fileName);
            BufferedOutputStream bof = new BufferedOutputStream(fos);

            byte buf[] = new byte[1024];
            int len = 0;
            while((len=sis.read(buf))!=-1){
                bof.write(buf, 0, len);
            }
            //注意，输出时，一定要先关外层的流，再关里层的流。
            //否则，外层输出流中的残留数据刷不出去! 输入流的关闭顺序则相反!
            bof.close();
            fos.close();

            jlb.setText("文件\""+fileName+"\"已经合并完！");
            this.validate();
        }

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileSplit2().setVisible(true);
            }
        });
    }

}