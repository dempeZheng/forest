package com.dempe.ketty.common.file;

import eu.medsea.mimeutil.MimeUtil;

import java.io.File;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/16
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class FileTypeUtil {


    public static void main(String[] args) {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        //File f = new File("C:\\Users\\Administrator\\Desktop\\doc\\audioFile_1450077720715");
        File f = new File("娱乐系统全视图_v1.0.vsd");
        Collection<?> mimeTypes = MimeUtil.getMimeTypes(f);
        System.out.println(mimeTypes);
        //  output : application/msword
    }
}
