import DUST.DUSTController;
import org.json.simple.parser.ParseException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class mainJar {

    public static void main(String[] args) throws IOException, ParseException, JAXBException, InterruptedException {
        String tomlfile = args[0];
        System.out.println(tomlfile);

        DUSTController dustAgent = DUSTController.createADUSTAgent(tomlfile);

        dustAgent.startTestbench();
    }
}
