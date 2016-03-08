/**
 * Created by Ioanna on 24/11/2015.
 */
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.*;

public class TermsFrequency {

    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        File terms = new File("C:/term-vocab");
        BufferedReader bufferTerms;
        ArrayList<String> idsTerms = new ArrayList<String>();
        String lineT;
        //<frequency, term>
        Map<String, Integer> frequencyById = new HashMap<String, Integer>();
        //term with a specific id
        String term,id;

        //TOKENIZE TERMS
        try {
            bufferTerms = new BufferedReader(new InputStreamReader(new FileInputStream(terms)));
            while (!((lineT = bufferTerms.readLine().trim()).isEmpty())) {
				//split the string into tokens and convert them to lower case
                String[] token = lineT.toLowerCase().split("\\s+");
                id = token[0];
                term = token[1];
                idsTerms.add(term);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //READING XML AND CALCULATE FREQUENCY
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("docToXML.xml");
            Element rootElement = doc.getDocumentElement();
            NodeList nodes = rootElement.getChildNodes();
            ArrayList<String> documents = new ArrayList<String>();

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                // make sure it's an element node.
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node; //type cast to element
                    //check for attribute id
                    if (element.hasAttribute("id")) {
                        String docID =  element.getAttribute("id");
                    }
                    String text = element.getElementsByTagName("TEXT").item(0).getTextContent();
                    documents.add(text);
                }
            }
			
            for (String str : documents) {
                //split each text into tokens
                List<String> list = Arrays.asList(str.toLowerCase().split(" "));
                //use a set for the unique words
                Set<String> set = new HashSet<String>(list);

                /*
                Here we could use a Stemmer for a proper solution
                 */

                for(String key : idsTerms){
                 //calculate frequency for each document term
                    int frequency =  Collections.frequency(set, key);
                    //System.out.println("FREQUENCY: " +frequency+ " TERM: " + key) ;
                    frequencyById.put(key, frequency);
                }
            }
            printMap(frequencyById);
            ccountTermsByFrequency(frequencyById);

        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

	
    //print each term, frequency pair with descending order of frequency
    private static void printMap(Map<String, Integer> map){TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>();
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        //Cnvert sorted list to map
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Iterator<Map.Entry<String, Integer>> iter = list.iterator(); iter.hasNext();) {
            Map.Entry<String, Integer> entry = iter.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        //Print terms
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Term : " + entry.getKey() + " " + " Frequency : " + entry.getValue());
        }
    }

    //terms with frequency >=2
    private static void ccountTermsByFrequency(Map<String, Integer> frequencyById) {
        int count=0;
        for (Map.Entry<String, Integer> entry : frequencyById.entrySet()) {
            if (entry.getValue() >= 2) {
                count++;
            }
        }
        System.out.println("=============================================");
        System.out.println("Total number of Terms with frequency >=2 : " + count);
    }
}
