import DUST.DUSTController;
import org.json.simple.parser.ParseException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException, ParseException, JAXBException, InterruptedException
    {
        DUSTController dustAgent = DUSTController.createADUSTAgent("config.toml");

        dustAgent.startTestbench();
    }
}
