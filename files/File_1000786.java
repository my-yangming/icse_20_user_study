package org.nutz.mvc.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.filepool.FilePool;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 处�?�Html5�?�?上传
 * @author wendal(wendal1985@gmail.com)
 * @since 1.b.44
 */
public class Html5Uploading implements Uploading {
    
    private static final Log log = Logs.get();

    public Map<String, Object> parse(HttpServletRequest req,
            UploadingContext context) throws UploadException,
            UploadOutOfSizeException, UploadUnsupportedFileNameException,
            UploadUnsupportedFileTypeException {
        
        int bufferSize = context.getBufferSize();
        int size = req.getContentLength();
        int maxSize = context.getMaxFileSize();
        FilePool tmps = context.getFilePool();
        
        FieldMeta meta = new FieldMeta("name=\"filedata\"; filename=\"nutz.jpg\""); //默认字段�??,文件�??
        
        //检查大�?
        if (maxSize > 0 && size > context.getMaxFileSize()) {
            throw new UploadOutOfSizeException(meta);
        }
        
        //获�?�文件�??
        String disposition = req.getHeader("Content-Disposition");
        if (disposition != null && disposition.startsWith("attachment;")) {
            meta = new FieldMeta(disposition.substring("attachment;".length()).trim());
        } else {
            if (log.isInfoEnabled())
                log.info("Content-Disposition no found, using default fieldname=filedata, filename=nutz.jpg");
        }
        
        if(log.isDebugEnabled())
            log.debugf("Upload File info: FilePath=[%s],fieldName=[%s]",meta.getFileLocalPath(),meta.getName());
        
        // 检查是�?�通过文件�??过滤
        if (!context.isNameAccepted(meta.getFileLocalName())) {
            throw new UploadUnsupportedFileNameException(meta);
        }
        // 检查是�?�通过文件类型过滤 TODO �?�?�检查�?�?
        //if (!context.isContentTypeAccepted(meta.getContentType())) {
        //    throw new UploadUnsupportedFileTypeException(meta);
        //}
        
        //开始读�?�数�?�
        File tmp = tmps.createFile(meta.getFileExtension());
        OutputStream ops = null;
        try {
            ops = new BufferedOutputStream(    new FileOutputStream(tmp),bufferSize * 2);
            Streams.writeAndClose(ops, req.getInputStream());
            
            //检查文件大�?
            if (tmp.length() != size)
                throw new UploadOutOfSizeException(meta);
            if (maxSize > 0 && tmp.length() > maxSize)
                throw new UploadOutOfSizeException(meta);
            
            
            NutMap params = Uploads.createParamsMap(req);
            
            //检查空文件
            if (tmp.length() == 0 && context.isIgnoreNull()) {
                if (log.isDebugEnabled())
                    log.debug("emtry file , drop it ~~");
                tmp.delete();
            } else {
                params.put(meta.getName(), new TempFile(meta, tmp));
            }
            return params;
        } catch (FileNotFoundException e) {
            // �?�?�能�?�生?!
            throw Lang.wrapThrow(e);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        } finally {
            Streams.safeClose(ops);
        }
    }

}
