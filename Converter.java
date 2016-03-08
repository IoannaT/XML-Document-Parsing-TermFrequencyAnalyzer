/**
 * Created by Ioanna on 23/11/2015.
 */
import java.io.*;

public class Converter {

    public static void main(String args[]) throws FileNotFoundException {
        BufferedReader buffer;
        FileWriter writer = null;
        String line = null;
		//each tag <TEXT> contains a document @text
        String text = "";
        File documents = new File("C:/doc-text");

        try {

            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(documents)));
            writer = new FileWriter(new File("docToXML.xml"));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
            writer.write("<DOCUMENTS>" + "\n");

            while ((line = buffer.readLine()) !=null) {
               //@counter increases once a NEW document has been read.
                int counter=0;
                //if the first line character is a number we get a new element <DOCUMENT>
                if ((Character.isDigit(line.charAt(0)))) {
                    System.out.println("ID : " + line);
                    writer.write("\t" + "<DOC id=\"" + line + "\">" + "\n");
                    counter++;
                    //new text string for the next <DOCUMENT>
                    text = "";
                }

				//if line contains "/" (-> split character), write the text that have been read so far.
                if (line.contains("/")){
                    System.out.println("TEXT: " + text);
                    writer.write("\t" + "\t" +"<TEXT>" + text + "</TEXT>" + "\n");
                    writer.write("\t" +"</DOC>" + "\n");
                }

                if (counter == 0) {
                    text = text + " " + line;
                }
            }
            writer.write("</DOCUMENTS>");
            //clear buffer
            writer.flush();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
