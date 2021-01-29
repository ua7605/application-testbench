package DUST.ConfigurationFiles;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * The DUSTCONF.JSON class will load the a DUSTCONF.JSON configuration file in.
 * The DUSTCONF.JSON file is a DUST configuration file that were the block name is specified and all the channels were an application
 * can send data.
 */
public class JSON
{

    public static void initializeDUSTChannelFile(String pathFile_toDUSTConfig) throws IOException, ParseException
    {
        System.out.println(pathFile_toDUSTConfig);
        JSONParser jsonParser = new JSONParser();

        FileReader reader = new FileReader(pathFile_toDUSTConfig);

        Object object = jsonParser.parse(reader);

        JSONObject jsonObject = (JSONObject) object;

        String blockname = (String) jsonObject.get("BlockName-of-subscribe-block");

        JSONArray channelarray = (JSONArray) jsonObject.get("list-of-channels");

        DUSTBlockChannels dustBlockChannels = DUSTBlockChannels.getInstance();

        dustBlockChannels.setBlockName(blockname);

        for (int i= 0; i< channelarray.size();i++)
        {
            JSONObject channelNameobj = (JSONObject) channelarray.get(i);

            dustBlockChannels.addChannelName((String)channelNameobj.get("channelName"+i));
        }

        for (String index: dustBlockChannels.getChannelNameList())
        {
            System.out.println(index);
        }
    }
}
