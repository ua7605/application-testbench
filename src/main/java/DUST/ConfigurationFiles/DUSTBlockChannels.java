package DUST.ConfigurationFiles;

import java.util.ArrayList;

/**
 * This class is a Singleton class.
 * It is used to load a DUST configuration file in.
 *
 *
 * @author Vincent Charpentier
 * @version 1.0
 * @since   2021-01-9
 */
public class DUSTBlockChannels
{
    static DUSTBlockChannels obj = new DUSTBlockChannels();
    static String blockName;
    static ArrayList<String> channelNameList;


    private DUSTBlockChannels()
    {
        channelNameList = new ArrayList<>();
    }

    public static DUSTBlockChannels getInstance()
    {
        return obj;
    }

    protected void addChannelName(String channelName)
    {
        channelNameList.add(channelName);
    }

    public ArrayList<String> getChannelNameList()
    {
        return channelNameList;
    }

    protected void setBlockName(String blockName)
    {
        DUSTBlockChannels.blockName = blockName;
    }

    public String getBlockName()
    {
        if (blockName == null){
            System.out.println("The block has not been initialized!");
            return null;
        }else {
            return blockName;
        }
    }
}
