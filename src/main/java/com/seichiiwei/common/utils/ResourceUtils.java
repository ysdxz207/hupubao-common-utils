package com.seichiiwei.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * @author Seichii.wei
 * @date 2019-07-11 11:08:20
 * SpringBoot环境资源目录读取文件工具
 * 可以使用@Autowired
 */
@Component
public class ResourceUtils {
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     *
     * @param pattern   如：classpath*:sql/*.sql
     * @return
     * @throws IOException
     */
    public Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }
}
