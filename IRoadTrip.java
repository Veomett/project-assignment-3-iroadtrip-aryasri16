import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;

public class IRoadTrip {
    String bord;
    String capDist;
    String stateName;

    public IRoadTrip (String [] args) {
        // Replace with your code
        bord = args[0];
        capDist = args[1];
        stateName = args[2];
    }


    public int getDistance (String country1, String country2) {
        // Replace with your code
        return -1;
    }


    public List<String> findPath (String country1, String country2) {
        // Replace with your code
        return null;
    }


    public void acceptUserInput() {
        // Replace with your code
        System.out.println("IRoadTrip - skeleton");
    }


    public static void main(String[] args) throws IOException{
        String [] args2 = {"borders.txt", "capdist.csv", "state_name.tsv"};
        System.out.println(args.length);
        IRoadTrip a3 = new IRoadTrip(args2);
        Hashmaps h = new Hashmaps(args2);
        a3.acceptUserInput();
        Graph g = new Graph(h.countryCodes.size());

    }

    public static class Hashmaps {
        HashMap<String, String> countryCodes;
        HashMap<String, ArrayList<String>> distanceFromInitial; // ex:
        HashMap<String, ArrayList<String>> borders;

        public Hashmaps(String [] files) throws IOException{
            borders = buildBorders(files[0]);
            distanceFromInitial = buildDistFromInitial(files[1]);
            countryCodes = buildCountryCodes(files[2]);
            fixStateName();
        }

        public void fixStateName() {
            /*String [] fixesForInStateName = {"Bahamas", "Bosnia-Herzegovina", "Congo, Democratic Republic of (Zaire)", "Congo",
            "East Timor", "Gambia", "German Federal Republic", "Italy/Sardinia", "Korea, People's Republic of", "Korea, Republic of",
            "Macedonia (Former Yugoslav Republic of)", "United States of America", "United Kingdom", "Rumania", "Cape Verde"};*/
            countryCodes.put("Bahamas, The", "BHM");
            countryCodes.put("Bosnia and Herzegovina", "BOS");
            countryCodes.put("Congo, Democratic Republic of the", "DRC");
            countryCodes.put("Democratic Republic of the Congo", "DRC");
            countryCodes.put("Congo, Republic of the", "CON");
            countryCodes.put("Republic of the Congo", "CON");
            countryCodes.put("Timor-Leste", "ETM");
            countryCodes.put("Gambia, The", "GAM");
            countryCodes.put("The Gambia", "GAM");
            countryCodes.put("Germany", "GFR");
            countryCodes.put("Italy", "ITA");
            countryCodes.put("Sardinia", "ITA");
            countryCodes.put("Korea, North", "PRK");
            countryCodes.put("North Korea", "PRK");
            countryCodes.put("Korea, South", "ROK");
            countryCodes.put("South Korea", "ROK");
            countryCodes.put("North Macedonia", "MAC");
            countryCodes.put("US", "USA");
            countryCodes.put("United States", "USA");
            countryCodes.put("UK", "UKG");
            countryCodes.put("Romania", "RUM");
            countryCodes.put("Cabo Verde", "CAP");
            countryCodes.put("Tanzania", "TAZ");
            countryCodes.put("Tanganyika", "TAZ");
            countryCodes.put("Czechia", "CZR");
            countryCodes.put("Eswatini", "SWA");
            countryCodes.put("Vietnam", "DRV");
            countryCodes.put("Suriname", "SUR");

            countryCodes.remove("South Sudan");
            countryCodes.remove("Kosovo");

            //tanzania
            //czechia
            //eswatini
            //put in italy and sardinia individually
            // vietnam
            // Suriname
            // tanganika
            // input the names from borders and add to countryCodes

            // actually just add the

            //remove south sudan and kosovo from countryCodes

        }

        public HashMap<String, String> buildCountryCodes(String fname) throws IOException {
            File f = new File(fname);
            BufferedReader b = new BufferedReader(new FileReader(f));
            HashMap<String, String> cc = new HashMap<>();
            String s = "";
            s = b.readLine();
            String [] parts;
            String prevCode = "";
            while((s = b.readLine()) != null) {
                parts = s.split("\t");
                if(parts[4].equals("2020-12-31")) {
                    cc.put(parts[2], parts[1]);
                }
                prevCode = parts[1];
            }
            b.close();
            return cc;
        }

        public HashMap<String, ArrayList<String>> buildDistFromInitial(String fname) throws IOException {
            File f = new File(fname);
            BufferedReader b = new BufferedReader(new FileReader(f));
            HashMap<String, ArrayList<String>> dfi = new HashMap<>();
            String [] parts;
            String s, curr, prevCode = "";
            ArrayList<String> a = new ArrayList<>();
            s = b.readLine();
            while((s = b.readLine()) != null) {
                parts = s.split(",");
                curr = parts[1];
                if(!(curr.equals(prevCode))) {
                    a = new ArrayList<>();
                }
                a.add(parts[3]);
                a.add(parts[4]);
                dfi.put(parts[1], a);
                prevCode = curr;
            }
            b.close();
            return dfi;
        }

        public HashMap<String, ArrayList<String>> buildBorders(String fname) throws IOException {
            File f = new File(fname);
            BufferedReader b = new BufferedReader(new FileReader(f));
            HashMap<String, ArrayList<String>> bb = new HashMap<>();
            ArrayList<String> a2 = new ArrayList<>();
            String [] cntryBorders;
            String cntryName, s = "";
            while((s = b.readLine()) != null) {
                cntryName = s.substring(0, s.indexOf('='));
                cntryName = cntryName.trim();
                s = s.substring(s.indexOf('=') + 1);
                s = s.trim();
                cntryBorders = s.split(";");
                for (String cntryBorder : cntryBorders) {
                    a2.add(cntryBorder);
                    bb.put(cntryName, a2);
                }
                a2 = new ArrayList<>();
            }
            b.close();
            return bb;
        }
    }
}

