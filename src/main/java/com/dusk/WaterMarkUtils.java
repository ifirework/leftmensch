package com.dusk;

import com.dusk.constants.WaterMarkConstant;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
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
    /**
     * 填充水印
     * @param filePath 文件全路径名
     * @param text 要填充的文字
     * @param color 填充文字颜色
     * @return
     */
    public static String markText(String filePath,String text,Color color){
        FileOutputStream out=null ;
        String str = "";
        if(StringUtils.isBlank(filePath)){
            return str;
        }

        if(StringUtils.isBlank(text)){
            text = WaterMarkConstant.DEFAULT_NAME;
        }

        if(color==null){
            color=WaterMarkConstant.FONT_COLOR;
        }


        try {
            File file = new File(filePath);
            Image image = ImageIO.read(file);

            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(image,0,0,width,height,null);
            Font font = new Font(text, WaterMarkConstant.FONT_STYLE, WaterMarkConstant.FONT_SIZE);
            graphics.setFont(font);
            graphics.setColor(color);

            double width1 = getFontSize(graphics,font,text);
            double height1 = getFontHeight(graphics,font);

            double diffWidth = width - width1 ;
            double diffHeight = height - height1/2 ;


            double x = WaterMarkConstant.X;
            double y = WaterMarkConstant.Y;

            if(x<diffWidth){
                x=diffWidth;
            }

            if(y<diffHeight){
                y=diffHeight;
            }
            //设置水印透明度
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,WaterMarkConstant.ALPHA));
            graphics.drawString(text,Float.parseFloat(x+""),Float.parseFloat(y+""));
            graphics.dispose();

            //将图片写入磁盘
            String path = file.getPath();
            String fileName = file.getName();
            String lastrStr = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            str = path.replaceAll(fileName, new Date().getTime() + lastrStr);
            out = new FileOutputStream(str);
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


    /**
     * 获取对应字体的文字的高度
     *
     * @param g2d
     * @param font
     * @return
     * @parm
     * @exception
     */
    public static double getFontHeight(Graphics2D g2d, Font font) {
        // 设置大字体
        FontRenderContext context = g2d.getFontRenderContext();
        // 获取字体的像素范围对象
        Rectangle2D stringBounds = font.getStringBounds("w", context);
        double fontWidth = stringBounds.getWidth();
        return fontWidth;
    }

    /**
     * 获取对应的文字所占有的长度
     *
     * @param g2d
     * @param font
     * @return
     * @parm
     * @exception
     */
    public static double getFontSize(Graphics2D g2d, Font font, String text) {
        // 设置大字体
        FontRenderContext context = g2d.getFontRenderContext();
        // 获取字体的像素范围对象
        Rectangle2D stringBounds = font.getStringBounds(text, context);
        double fontWidth = stringBounds.getWidth();
        return fontWidth;
    }

    public static void main(String[] args) {
        String str = WaterMarkUtils.markText("C:\\Users\\wang\\Pictures\\zzpic10566.jpg","yfdaf哈哈",Color.orange);
        System.out.println(str);
    }


}
