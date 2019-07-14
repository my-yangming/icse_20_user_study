package cc.mrbird.demo.filter;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author MrBird
 */
public class MyTypeFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
        // 获�?�当�?正在扫�??的类的注解信�?�
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        // 获�?�当�?正在扫�??的类的类信�?�
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        // 获�?�当�?正在扫�??的类的路径等信�?�
        Resource resource = metadataReader.getResource();

        String className = classMetadata.getClassName();
        return StringUtils.hasText("er");
    }
}
