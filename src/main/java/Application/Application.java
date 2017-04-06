package Application;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <h1>Csv to Json Converter</h1>
 *
 * param csv the source csv file
 * param the results file
 */
public class Application {


    public static void main(String[] args) {

        Logger logger = LogManager.getRootLogger();

        try {
            if (args.length == 2 && new File(args[0]).isFile() || args.length == 1 && new File(args[0]).isDirectory()) {
                File input = new File(args[0]);
                File output = new File(input.getName() + ".json");



                if(input.isDirectory()){
                    for(File f : input.listFiles()){
                        output = new File(f.getPath().replace("csv", "json"));
                        List<Map<?, ?>> data = readObjectsFromCsv(f);
                        writeAsJson(data, output);
                    }
                }


                if(!input.exists()){
                    logger.fatal("Input file does not exist.");
                    System.exit(-1);
                }

                List<Map<?, ?>> data = readObjectsFromCsv(input);
                writeAsJson(data, output);
                logger.info("conversation completed successfully.");

            } else {
                logger.error("Invalid number of arguments passed.");
                logger.info("The correct number of arguments is two, the first being the csv file, the second being the the output destination.");
            }
        } catch (Exception e) {
            e.getCause();

        }

    }

    public static List<Map<?, ?>> readObjectsFromCsv(File file) throws IOException {
        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(file);

        return mappingIterator.readAll();
    }

    public static void writeAsJson(List<Map<?, ?>> data, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, data);
    }


}
