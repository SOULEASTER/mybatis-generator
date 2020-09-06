package org.example.generator.plugin;

import org.mybatis.generator.api.PluginAdapter;

import java.util.List;
import java.util.Properties;

/**
 *
 * @author 魏荣杰
 * @date 2020/9/6 11:49
 * @since
 * @version
 */
public class CustomGeneratorPlugin extends PluginAdapter {

    private Properties properties;

    public CustomGeneratorPlugin(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }




}
