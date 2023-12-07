import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.util.*;
import java.io.*;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.min;

public class IRoadTrip {
    HashMap<String, String> countryCodes;
    HashMap<String, ArrayList<String>> distanceFromInitial;
    HashMap<String, HashMap<String, Double>> borders;

    HashMap<String, HashMap<String, Double>> graph;

    public IRoadTrip(String[] args) throws IOException {
        countryCodes = buildCountryCodes(args[2]);
        fixStateName();
        borders = buildBorders(args[0]);
        distanceFromInitial = buildDistFromInitial(args[1]);
        graph = creatingGraph();
    }

    public void fixStateName() {
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
        countryCodes.put("Zimbabwe", "ZIM");
        countryCodes.put("Yemen", "YEM");
        countryCodes.put("Iran", "IRN");
        countryCodes.put("Cambodia", "CAM");
        countryCodes.put("Sri Lanka", "SRI");
        countryCodes.put("Burkina Faso", "BFO");
        countryCodes.put("Cote d'Ivoire", "CDI");
        countryCodes.put("Belarus", "BLR");
        countryCodes.put("Russia", "RUS");
        countryCodes.put("Russia (Kaliningrad Oblast)", "RUS");
        countryCodes.put("Poland (Kaliningrad Oblast)", "POL");
        countryCodes.put("Lithuania (Kaliningrad Oblast)", "LIT");
        countryCodes.put("Burma", "MYA");
        countryCodes.put("Turkey (Turkiye)", "TUR");
        countryCodes.put("Turkey", "TUR");
        countryCodes.put("Kyrgyzstan", "KYR");
        // cant find
        countryCodes.put("Andorra", "AND");
        countryCodes.put("Liechtenstein", "LHN");
        countryCodes.put("West Bank", "WBA");
        countryCodes.put("Gaza Strip", "GZS");
        countryCodes.put("Monaco", "MCN");
        // countryCodes.put("Macau", "MCU");
        //countryCodes.put("French Guiana", "FGA");
        //countryCodes.remove("South Sudan");
        //countryCodes.remove("Kosovo");
    }

    public HashMap<String, HashMap<String, Double>> creatingGraph() {
        // objective is to change the double in pl to capdist distance
        HashMap<String, HashMap<String, Double>> rV = new HashMap<>();
        HashMap<String, Double> pl = new HashMap<>();
        rV = borders;
        for (String key : rV.keySet()) {
            pl = rV.get(key);
            for (String key2 : pl.keySet()) {
                String put = getCapDist(key, key2);
                if (!put.equals("")) {
                    pl.put(key2, Double.parseDouble(put));
                }
            }
        }
        return rV;
    }

    public String getCapDist(String ccode1, String ccode2) {
        String dist = "";
        ArrayList<String> ar = distanceFromInitial.get(ccode1);
        if (ar != null) {
            for (int i = 0; i < ar.size(); i++) {
                if (ar.get(i).equals(ccode2)) {
                    dist = ar.get(i + 1);
                    break;
                }
            }
        }
        return dist;
    }

    public HashMap<String, String> buildCountryCodes(String fname) throws IOException {
        File f = new File(fname);
        BufferedReader b = new BufferedReader(new FileReader(f));
        HashMap<String, String> cc = new HashMap<>();
        String s = "";
        s = b.readLine();
        String[] parts;
        String prevCode = "";
        while ((s = b.readLine()) != null) {
            parts = s.split("\t");
            if (parts[4].equals("2020-12-31")) {
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
        String[] parts;
        String s, curr, prevCode = "";
        ArrayList<String> a = new ArrayList<>();
        s = b.readLine();
        while ((s = b.readLine()) != null) {
            parts = s.split(",");
            curr = parts[1];
            if (!(curr.equals(prevCode))) {
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

    public HashMap<String, HashMap<String, Double>> buildBorders(String fname) throws IOException {
        File f = new File(fname);
        BufferedReader b = new BufferedReader(new FileReader(f));
        HashMap<String, HashMap<String, Double>> bb = new HashMap<>();
        HashMap<String, Double> cntryAndDist = new HashMap<>();
        //ArrayList<String> a2 = new ArrayList<>();
        String[] cntryBorders;
        String cntryName, s, placehldr = "";
        String secCntryCode = "";
        String distStr = "";
        double dista = 0;
        int i = 0;
        while ((s = b.readLine()) != null) {
            cntryName = s.substring(0, s.indexOf('='));
            cntryName = cntryName.trim();
            placehldr = cntryName;
            cntryName = countryCodes.get(cntryName);
            if (cntryName == null) {
                cntryName = placehldr;
            }
            s = s.substring(s.indexOf('=') + 1);
            s = s.trim();
            cntryBorders = s.split(";");
            for (int j = 0; j < cntryBorders.length; j++) {
                String ph = cntryBorders[j];
                for (i = 0; i < ph.length(); i++) {
                    if (!Character.isDigit(ph.charAt(i))) {
                        secCntryCode += ph.charAt(i);
                    } else {
                        break;
                    }
                }
                placehldr = secCntryCode.trim();
                secCntryCode = countryCodes.get(secCntryCode.trim());
                if (secCntryCode == null) {
                    secCntryCode = placehldr;
                }
                if (ph.length() != 0) {
                    distStr = ph.substring(i, ph.length() - 3);
                } else {
                    distStr = "0";
                }
                if (distStr.contains(",")) {
                    String distStr2 = distStr.substring(0, distStr.indexOf(","));
                    String disStr3 = distStr.substring(distStr.indexOf(",") + 1);
                    distStr = distStr2 + disStr3;
                }
                dista = Double.parseDouble(distStr);
                cntryAndDist.put(secCntryCode, dista);
                bb.put(cntryName, cntryAndDist);
                secCntryCode = "";
            }
            cntryAndDist = new HashMap<>();
        }
        b.close();
        return bb;
    }


    public double getDistance(String country1, String country2) {
        double dist = -1;
        if (!(countryCodes.containsKey(country1))) {
            return dist;
        }
        if (!(countryCodes.containsKey(country2))) {
            return dist;
        }
        String country1Code = countryCodes.get(country1);
        String country2Code = countryCodes.get(country2);
        HashMap<String, Double> hm = borders.get(country1Code);
        ArrayList<String> country1Dists = distanceFromInitial.get(country1Code);
        if (country1Dists == null) {
            return dist;
        }
        if (!hm.containsKey(country2Code)) {
            return dist;
        } else {
            for (int i = 0; i < country1Dists.size(); i++) {
                if (country2Code.equals(country1Dists.get(i))) {
                    dist = Double.parseDouble(country1Dists.get(i + 1));
                    break;
                }
            }
        }
        return dist;
    }


    public List<String> findPath(String country1, String country2) {
        List<String> rV = new ArrayList<>();
        PriorityQueue<Node> minheap = new PriorityQueue<>();
        HashMap<String, Double> map = new HashMap<>();
        HashMap<String, String> routes = new HashMap<>();
        Double km = 0.0;
        Double possibleKM = 0.0;
        Node curr;
        String startCode = countryCodes.get(country1);
        double highestNum = Double.MAX_VALUE;
        String endCode = "";
        for (String cntry : graph.keySet()) {
            map.put(cntry, highestNum);
        }
        map.put(startCode, 0.0);
        minheap.add(new Node(startCode, 0.0));
        while (!minheap.isEmpty()) {
            curr = minheap.peek();
            minheap.remove(curr);
            endCode = countryCodes.get(country2);
            if (curr.country.equals(endCode)) {
                return reversePath(routes, endCode);
            }
            HashMap<String, Double> currentCntry = graph.get(curr.country);
            for (String newCode : currentCntry.keySet()) {
                km = graph.get(curr.country).get(newCode);
                possibleKM = curr.dist + km;
                if(map.containsKey(newCode)) {
                    if (possibleKM < map.get(newCode)) {
                        map.put(newCode, possibleKM);
                        routes.put(newCode, curr.country);
                        minheap.offer(new Node(newCode, possibleKM));
                    }
                }
            }
        }
        return rV;
    }

    public List<String> reversePath(Map<String, String> route, String country2) {
        List<String> countryRoute = new ArrayList<>();
        String curr = country2;
        while (curr != null) {
            countryRoute.add(curr);
            curr = route.get(curr);
        }
        return reverseHelper(countryRoute);
    }

    public List<String> reverseHelper(List<String> countryRoute) {
        ArrayList<String> rV = new ArrayList<>();
        for(int i = countryRoute.size() - 1; i >= 0; i--) {
            rV.add(countryRoute.get(i));
        }
        return rV;
    }

    public void acceptUserInput() {

        // Replace with your code
        System.out.println("IRoadTrip - skeleton");
    }


    public static void main(String[] args) throws IOException {
        String[] args2 = {"borders.txt", "capdist.csv", "state_name.tsv"};
        IRoadTrip a3 = new IRoadTrip(args2);
        //Hashmaps h = new Hashmaps(args2);
        List<String> am = a3.findPath("Yemen", "Jordan");
        a3.acceptUserInput();

    }

    class Node implements Comparable<Node>{
        String country;
        double dist;
        public Node(String c, double d) {
            country = c;
            dist = d;
        }

        @Override
        public int compareTo(Node secCntry) {
            int rV = Double.compare(dist, secCntry.dist);
            return rV;
        }
    }
}

