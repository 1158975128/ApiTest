package com.zw.admin.server.utils;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mchange.util.Base64Encoder;

/**
 * @author larry
 * @Description:
 * @date 2021/2/25 14:50
 */
public class QRCodeUtil {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 100;
    // LOGO高度
    private static final int HEIGHT = 100;


    /**
     * 功能描述:根据指定的内容生成二维码信息
     *
     * @param source
     * @return java.lang.String
     * @author larry
     * @Date 2021/2/25 15:40
     */
    public static String createQrCode(String source,String format) throws Exception {
        //创建字节数据中保存图片数据
        byte[] numCodeImgByte = null;
        BufferedImage bi = QrCodeUtil.generate(source, WIDTH, HEIGHT);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ImageIO.write(bi, format, bo);
        //将文件内容写入字节数据
        numCodeImgByte = bo.toByteArray();
        //字节数据转base64
        String base64Str = Base64.getEncoder().encodeToString(numCodeImgByte);
        return base64Str;
    }
}
