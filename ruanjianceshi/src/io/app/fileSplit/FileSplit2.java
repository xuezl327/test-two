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
        super("chx��Ȩ���У�");
        initComponents();
    }

    private void initComponents() {
        //��ǩ
        label1 = new JLabel("        �ļ��и�ϲ�����1.0");



        font = new Font(null, Font.BOLD, 28);

        //���ñ���ͼƬ��������������������������
        label = new javax.swing.JLabel();//��ǩ
        ImageIcon im = new javax.swing.ImageIcon(
                "C:\\Users\\lenovo\\Pictures\\Camera Roll\\QQ��ͼ20180629165555.png");
        label.setIcon(im); 
        //��ӱ���ͼƬ
        label.setBounds(0, 0, im.getIconWidth(), im.getIconHeight());
        //����λ�úʹ�С
        jp = (JPanel) this.getContentPane();
        //�����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸�� 

        jp.setOpaque(false);
        //���ݴ���Ĭ�ϵĲ��ֹ�����Ϊnull
        jp.setLayout(null);


        //�ļ��иť
        btnFileCut = new JButton("�ļ��и�");
        btnFileCut.setSize(160, 100);
        //btnFileCut.setForeground(Color.RED);������ɫ
        btnFileCut.setBackground(Color.pink);//������ɫ
        btnFileCut.setLocation(im.getIconWidth()*1/10,im.getIconHeight()*13/20);
        btnFileCut.setFont(font);

        jp.add(btnFileCut);

        //�ļ��ϲ���ť
        btnFileJoin = new JButton("�ļ��ϲ�");
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


         // �ѱ���ͼƬ��ӵ��ֲ㴰�����ײ���Ϊ����   
        this.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        //Ϊǰ�洴���ı�ǩ ���ò����ȣ����ｫ������Ϊ����ײ㣬��ΪInteger.MIN_VALUE��int���͵���Сֵ��
        this.getLayeredPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //�ùرհ�ť������

        setBounds(new java.awt.Rectangle(200, 200,im.getIconWidth(), im.getIconHeight()));

        this.setResizable(false);//���ô��ڲ�������
    }

    //�ļ��и�
    protected void btnFileCutActionPerformed(ActionEvent e) throws IOException {
        //System.out.println("�ļ��и");

        JFileChooser jfc = new JFileChooser();
        int result = jfc.showOpenDialog(this);
        int num=0;
        File file = null;
        File desDir = null;
        if(result == JFileChooser.APPROVE_OPTION){
            file = jfc.getSelectedFile();
            //System.out.println(file.getName());//�ļ���
            //System.out.println(file.getParent());//����·��

            String strNum = JOptionPane.showInputDialog("�������ļ���Ҫ���ն���KBһ���ļ����ָ(Ĭ��Ϊ1024KB)", "1024");
            if(strNum==null){
                JOptionPane.showMessageDialog(this, "δ�����ָ��ļ��Ĵ�С���˴��ļ��ָ���ȡ����");
                //System.out.println("end");
                return;
            }else{
                try {
                    num = Integer.parseInt(strNum);
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(this, "�������ָ�ʽ���󣬴˴��ļ��ָ���ȡ����");
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
            //1��ȷԴ---Ӳ��,�ֽ� file
            FileInputStream fis = new FileInputStream(file);
            //2��ȷĿ��---Ӳ��,�ֽ� desDir
            FileOutputStream fos = null;
            if(!desDir.exists()){
                desDir.mkdir();
            }

            //3�����Ƿ���Ҫ��ǿ���ܾ����Ƿ�Ҫ���׽ӣ�Ȼ����ж�д
            BufferedInputStream bis = new BufferedInputStream(fis); 
            byte[] buf = new byte[1024];//ÿ�ζ�1KB
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
                    //ע�⣬���ʱ��һ��Ҫ�ȹ����������ٹ���������
                    //�������������еĲ�������ˢ����ȥ! �������Ĺر�˳�����෴!
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
                //jlb.setText("�Ѿ��и�["+kbing*numLen+"]KB...");
            }
            fis.close();
            bis.close();
            jlb.setText("�ļ�\""+file.getName()+"\"�Ѿ��и��꣡");
            this.validate();

        }
    }


    //�ļ��ϲ�
    protected void btnFileJoinActionPerformed(ActionEvent e) throws IOException {
        //System.out.println("�ļ��ϲ���");
        //��ʾ������
        JOptionPane.showMessageDialog(this, "�����ѡ��һ�����и����Ƭ���ļ���\t\n" +
                "ȷ�����б��и���ļ��������Ŀ¼�£��Ҳ����������ļ���\t\n" +
                "�ϲ�����ļ��ڵ�ǰĿ¼�£�");

        JFileChooser jfc = new JFileChooser();
        int result = jfc.showOpenDialog(this);
        File file = null;
        File desDir = null;
        String fileName = null;

        if(result==JFileChooser.APPROVE_OPTION){
            file = jfc.getCurrentDirectory();
            String str = file.getAbsolutePath();

            //ע�⣡��������������������������������������
            /*
             '\'��java����һ��ת���ַ���������Ҫ����������һ����
             ����System.out.println( "\\" ) ;ֻ��ӡ��һ��"\"��
             ����'\'Ҳ��������ʽ�е�ת���ַ���replaceAll �Ĳ�������������ʽ����
             ��Ҫ����������һ�������ԣ�\\\\��javaת����\\,\\�ֱ�������ʽת����\�� 
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


            //���������ϲ�
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
            //ע�⣬���ʱ��һ��Ҫ�ȹ����������ٹ���������
            //�������������еĲ�������ˢ����ȥ! �������Ĺر�˳�����෴!
            bof.close();
            fos.close();

            jlb.setText("�ļ�\""+fileName+"\"�Ѿ��ϲ��꣡");
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