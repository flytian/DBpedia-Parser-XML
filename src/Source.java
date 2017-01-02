import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Marius on 11/12/2016.
 */
public class Source {

    /**
     *
     * @param classes List of ontologies , retrieved from DBPedia
     * @param word Current word to be written
     * @param writer Writer object
     */
    private static void addToXml(ArrayList<String> classes, String word, Writer writer) {

        try  {

            if (classes.isEmpty()) {
                writer.write("\t<word>" + word + "</word>\n");
            } else {

                if (classes.size() > 1) {
                    writer.write("\t<word class = \"");
                    for (int i = 0; i < classes.size() - 1; i++) {
                        writer.write(classes.get(i) + " ,");
                    }

                    writer.write(classes.get(classes.size() - 1) + "\">" + " " + word + "</word>\n");
                }
                else {
                    writer.write("\t<word class = \"" + classes.get(0) + "\">" + " " + word + "</word>\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param writer Object , writing the beginning of the xml
     */
    private static void setUpXML(Writer writer) {

        try {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Ontology>\n");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot set up XML !");
        }
    }


    public static void main(String[] args) {
        Parser parser = new Parser();
        LinkedList<String> mWords;
        Writer writer;

        try {
            writer = new BufferedWriter(new OutputStreamWriter
                    (new FileOutputStream("./resources/my_xml.xml"), "utf-8"));

            setUpXML(writer);

            try (FileReader in = new FileReader("./resources/data.txt")) {
                BufferedReader br = new BufferedReader(in);

                String currentLine;
                while ((currentLine = br.readLine()) != null) {

                    if (!currentLine.isEmpty()) {
                        mWords = parser.getTokenizedWords(currentLine);
                        System.out.println(mWords);

                        for (String word : mWords) {

                            ArrayList<String> currentOntologies;
                            OntologyParser oParser = new OntologyParser(word);
                            currentOntologies = oParser.getOntologies();

                            addToXml(currentOntologies, word, writer);
                        }

                        mWords.clear();
                    }

                }

                writer.write("</Ontology>");
                writer.close();
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("File not found !");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("Io exception");
            }

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
