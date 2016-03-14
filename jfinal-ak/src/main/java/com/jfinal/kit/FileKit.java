/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.kit;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * FileKit.
 */
public class FileKit {
	public static void delete(File file) {
		if (file != null && file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
			else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i=0; i<files.length; i++) {
					delete(files[i]);
				}
			}
			file.delete();
		}
	}

    public static void copyFile(String source, String target) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(new File(source)));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(new File(target)));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    public static void copyFile(File sourceFile, String savePath, String fileName) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            File pathFile = new File(savePath);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            // 新建文件输出流并对它进行缓冲
            final File file = new File(pathFile, fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            outBuff = new BufferedOutputStream(new FileOutputStream(file));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    public static void moveFile(File sourceFile, String savePath, String fileName) throws IOException {
        copyFile(sourceFile, savePath, fileName);
        sourceFile.delete();
    }

    public File getFile(String savePath, String fileName) {
        return null;
    }

    public static byte[] getFile(String filePath) throws IOException {
        File file = new File(filePath);
        file.exists();
        byte[] buf = new byte[1024];
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        in.close();
        return out.toByteArray();
    }

    public static String checkImgSize(byte[] img) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(img);
        BufferedImage buffer = ImageIO.read(in);
        int width = buffer.getWidth();
        int height = buffer.getHeight();
        return "{\"width\":" + width + ",\"height\":" + height + "}";
    }

    /**
     * 图片裁剪
     */
    public static byte[] cut(byte[] img, String formatName, int x, int y, int width, int height) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(img);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageInputStream iis;
        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(formatName);
        ImageReader reader = it.next();
        iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, formatName, out);
        return out.toByteArray();
    }

    public static void zoom(File srcfile, String suffix, int width, String savePath, String zoomName)
            throws IOException {
        BufferedImage result;
        if (!srcfile.exists()) {
            System.out.println("文件不存在");
        }
        BufferedImage im = ImageIO.read(srcfile);


            /* 新生成结果图片 */
        int height = im.getHeight(null) * width / im.getWidth(null);//按比例，将高度缩减
        result = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        result.getGraphics().drawImage(
                im.getScaledInstance(width, height,
                        Image.SCALE_SMOOTH), 0, 0, null);
        File zoomFile = new File(savePath, zoomName);
        if (!zoomFile.exists()) {
            zoomFile.getParentFile().mkdirs();
        }
        FileOutputStream out = new FileOutputStream(zoomFile);
        ImageIO.write(result, suffix, out);
        out.flush();
        out.close();
    }

    public static String getFileExt(String filename) {
        int index = filename.lastIndexOf('.');
        if (index > 0) {
            return filename.substring(index + 1);
        }
        return "";
    }
}
