package com.auxiliary.myapplication.util;
import java.io.Closeable;
import java.io.IOException;
public class IOUtils {
    /**
     * Close closeable object
     * 关闭可以关闭的对象
     *
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
