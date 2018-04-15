package com.dusk;

import com.dusk.constants.WaterMarkConstant;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import sun.security.util.LegacyAlgorithmConstraints;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by wang on 2018/4/14.
 * 水印工具
 */
public class WaterMarkUtils {

    public static String markText(String filePath){
        FileOutputStream out=null ;
        String str = "";
        try {
            File file = new File(filePath);
            Image image = ImageIO.read(file);

            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(image,0,0,width,height,null);

            graphics.setFont(new Font(WaterMarkConstant.DEFAULT_NAME,WaterMarkConstant.FONT_STYLE,WaterMarkConstant.FONT_SIZE));
            graphics.setColor(WaterMarkConstant.FONT_COLOR);

            int width1 = WaterMarkConstant.FONT_SIZE*getLength(WaterMarkConstant.DEFAULT_NAME);
            int height1 = WaterMarkConstant.FONT_SIZE;

            int diffWidth = width - width1 ;
            int diffHeight = height - height1/2 ;


            int x = WaterMarkConstant.X;
            int y = WaterMarkConstant.Y;

            if(x<diffWidth){
                x=diffWidth;
            }

            if(y<diffHeight){
                y=diffHeight;
            }
            //设置水印透明度
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,WaterMarkConstant.ALPHA));
            graphics.drawString(WaterMarkConstant.DEFAULT_NAME,x,y);
            graphics.dispose();

            //将图片写入磁盘
            String path = file.getPath();
            String fileName = file.getName();
            String lastrStr = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            String s = path.replaceAll(fileName, new Date().getTime() + lastrStr);
            out = new FileOutputStream(s);
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(out);
            jpegEncoder.encode(bufferedImage);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return  str ;
    }

    private static int getLength(String text){



        int length = text.length();

        for(int i=0;i<text.length();i++){
            String element = String.valueOf(text.charAt(i));
            if(element.getBytes().length>1){//中文
                length++;
            }
        }

        length = length%2==0?length/2:length/2+1;
        return length;
    }


    public static void main(String[] args) {
        String str = WaterMarkUtils.markText("C:\\Users\\wang\\Pictures\\152164196923.jpg");
        System.out.println(str);
    }


}
