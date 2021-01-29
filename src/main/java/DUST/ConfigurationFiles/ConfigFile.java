package DUST.ConfigurationFiles;

import com.moandjiezana.toml.Toml;

import java.io.File;

public class ConfigFile extends Toml
{

    private static ConfigFile dustConfigurationFile;


    public static void initDustConfigurationFile(String filePathDustConfigFile)
    {
        dustConfigurationFile = new ConfigFile(filePathDustConfigFile);
    }

    public static ConfigFile getDustConfigurationFileInstance()
    {
        if (dustConfigurationFile == null){
            throw new RuntimeException("Dust Configuration file not initialized");
        }
        return dustConfigurationFile;
    }

    protected ConfigFile(String filePath){
        super();
        read(new File(filePath));
    }
}
