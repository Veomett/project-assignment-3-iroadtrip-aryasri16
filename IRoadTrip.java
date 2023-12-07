import java.util.*;
import java.io.*;


public class IRoadTrip {
    /*
     * These are my global variables for the class, countryCodes will take in state_names and use it to link a country
     * name to its country code. ReverseCC does the same but in reverse order. DistanceFromInitial takes in capdist and
     * basically will hold a country, an arraylist full of another countries (the key), and the value is the distance between their
     * capitals in km. Borders takes in borders, and will have a country name (key) and hashmap (value) that has all the
     * bordering countries to that country and the distance between them. I also have a graph hashmap that is very similar
     * to borders, but will hold capital distances instead of border distances. country1Name and country2Name are going to be
     * used for user input to hold the two countries the user inputs.
     */
    static HashMap<String, String> countryCodes;
    static HashMap<String, String> reverseCC;
    static HashMap<String, ArrayList<String>> distanceFromInitial;
    static HashMap<String, HashMap<String, Double>> borders;

    static HashMap<String, HashMap<String, Double>> graph;
    static String country1Name;
    static String country2Name;

    public IRoadTrip(String[] args) throws IOException {
        reverseCC = new HashMap<>();
        countryCodes = buildCountryCodes(args[2]);
        fixStateName(); // this is to add in all the countries that are diff between the files
        borders = buildBorders(args[0]);
        distanceFromInitial = buildDistFromInitial(args[1]);
        graph = creatingGraph();
    }

    /*
     * manually assigns country names to their rightful country code
     */
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
        // these codes below are countries that appear in borders but nowhere else
        countryCodes.put("Andorra", "AND");
        countryCodes.put("Liechtenstein", "LHN");
        countryCodes.put("West Bank", "WBA");
        countryCodes.put("Gaza Strip", "GZS");
        countryCodes.put("Monaco", "MCN");
        countryCodes.put("Macau", "MCU");
        countryCodes.put("French Guiana", "FGA");
    }

    /*
     * Creates the graph hashmap
     */
    public HashMap<String, HashMap<String, Double>> creatingGraph() {
        HashMap<String, HashMap<String, Double>> rV = new HashMap<>(); // create placeholder hashmap
        HashMap<String, Double> pl = new HashMap<>(); // placeholder for rV's values
        rV = borders; // set equal to borders since they're the same except for the value in pl
        for (String key : rV.keySet()) { // parse the outer hashmap
            pl = rV.get(key); // get the value
            for (String key2 : pl.keySet()) { // then parse that hashmap
                String put = getCapDist(key, key2); // get the capital dist
                if (!put.equals("")) { // if it's not empty
                    pl.put(key2, Double.parseDouble(put)); // then update the value with the key
                }
            }
        }
        return rV; // return the placeholder
    }

    /*
     * Gets the distance between 2 capitals, is a helper function for creatingGraph
     */
    public String getCapDist(String ccode1, String ccode2) {
        String dist = ""; // will hold the distance
        ArrayList<String> ar = distanceFromInitial.get(ccode1); // get the arraylist value from distanceFrom
        if (ar != null) { // if it exists
            for (int i = 0; i < ar.size(); i++) { // go ahead and parse the arraylist
                if (ar.get(i).equals(ccode2)) { // find the country we're adding
                    dist = ar.get(i + 1); // then add the dist between country1 and 2
                    break; // exit loop
                }
            }
        }
        return dist; // return the distance
    }

    /*
     * puts in values for countryCodes
     */
    public HashMap<String, String> buildCountryCodes(String fname) throws IOException {
        File f = new File(fname);
        BufferedReader b = new BufferedReader(new FileReader(f));
        HashMap<String, String> cc = new HashMap<>(); // will hold like this: [country name][id]
        String s = "";
        s = b.readLine();
        String[] parts;
        String prevCode = "";
        while ((s = b.readLine()) != null) { // while the file still has content to read
            parts = s.split("\t"); // split by tab
            if (parts[4].equals("2020-12-31")) { // don't include countries that aren't with us anymore
                cc.put(parts[2], parts[1]); // put it into the hashmap
                reverseCC.put(parts[1], parts[2]); // put it in reverse order into a diff one as well
            }
            prevCode = parts[1];
        }
        b.close();
        return cc;
    }

    /*
     * puts in values for distFromInitial
     */
    public HashMap<String, ArrayList<String>> buildDistFromInitial(String fname) throws IOException {
        File f = new File(fname);
        BufferedReader b = new BufferedReader(new FileReader(f));
        HashMap<String, ArrayList<String>> dfi = new HashMap<>();
        String[] parts;
        String s, curr, prevCode = "";
        ArrayList<String> a = new ArrayList<>();
        s = b.readLine();
        while ((s = b.readLine()) != null) {
            parts = s.split(","); // split by comma
            curr = parts[1]; // country id of country1
            if (!(curr.equals(prevCode))) { // if it's a new country, we reinitialize a
                a = new ArrayList<>();
            }
            a.add(parts[3]); // add the country2 code and the dist between capitals
            a.add(parts[4]);
            dfi.put(parts[1], a); // put it into the hashmap
            prevCode = curr; // update previous code
        }
        b.close();
        return dfi;
    }

    /*
     * Puts in values for the borders hashmap
     */
    public HashMap<String, HashMap<String, Double>> buildBorders(String fname) throws IOException {
        File f = new File(fname);
        BufferedReader b = new BufferedReader(new FileReader(f));
        HashMap<String, HashMap<String, Double>> bb = new HashMap<>(); // it's going to hold a country, then the countries it borders and their distances
        HashMap<String, Double> cntryAndDist = new HashMap<>(); // going to hold said bordering countries and their distances
        //ArrayList<String> a2 = new ArrayList<>();
        String[] cntryBorders; // holds the arr of borders
        String cntryName, s, placehldr = "";
        String secCntryCode = "";
        String distStr = "";
        double dista = 0;
        int i = 0;
        while ((s = b.readLine()) != null) {
            cntryName = s.substring(0, s.indexOf('=')); // so we get the name of country1
            cntryName = cntryName.trim(); // trim it
            placehldr = cntryName; // set a placeholder in case it doesnt have a corresponding code
            cntryName = countryCodes.get(cntryName); // get the code
            if (cntryName == null) { // if it doesnt have a code just put its normal name
                cntryName = placehldr;
            }
            s = s.substring(s.indexOf('=') + 1); // now we're going to get the bordering countries
            s = s.trim(); // trim
            cntryBorders = s.split(";"); // split by ; so every element is like {country name, dist}
            for (int j = 0; j < cntryBorders.length; j++) { // parse the arr
                String ph = cntryBorders[j]; // get the first country
                for (i = 0; i < ph.length(); i++) { // get the name of the country
                    if (!Character.isDigit(ph.charAt(i))) {
                        secCntryCode += ph.charAt(i);
                    } else {
                        break;
                    }
                }
                placehldr = secCntryCode.trim(); // then trim
                secCntryCode = countryCodes.get(secCntryCode.trim()); // get its code as well
                if (secCntryCode == null) { // if it doesnt have one just reset to og name
                    secCntryCode = placehldr;
                }
                if (ph.length() != 0) { // to get the km dist i used a substring to cut everything but the digits out
                    distStr = ph.substring(i, ph.length() - 3);
                } else {
                    distStr = "0";
                }
                if (distStr.contains(",")) { // then i removed the , in the 4 digit numbers by taking apart the string and concatenating again
                    String distStr2 = distStr.substring(0, distStr.indexOf(","));
                    String disStr3 = distStr.substring(distStr.indexOf(",") + 1);
                    distStr = distStr2 + disStr3;
                }
                dista = Double.parseDouble(distStr); // then covert to a double
                cntryAndDist.put(secCntryCode, dista); // and then set to the inner hashmap
                bb.put(cntryName, cntryAndDist); // then set that along with country1 code to the outer hashmap
                secCntryCode = ""; // reset second country code
            }
            cntryAndDist = new HashMap<>(); // initialize the hashmap to be new again
        }
        b.close();
        return bb;
    }

    /*
     * Gets the distance between country1 and country2
     */
    public static double getDistance(String country1, String country2) {
        double dist = -1; // already set dist to -1
        if (!(countryCodes.containsKey(country1))) { // this is so that if we catch any errors, we can just return it
            return dist;
        }
        if (!(countryCodes.containsKey(country2))) {
            return dist;
        }
        // get the country codes for both countries
        String country1Code = countryCodes.get(country1);
        String country2Code = countryCodes.get(country2);
        HashMap<String, Double> hm = borders.get(country1Code); // get the borders of country 1 from borders hashmap
        ArrayList<String> country1Dists = distanceFromInitial.get(country1Code); // get the border distance from distanceFromInitial
        if (country1Dists == null) { // if there are no borders then return -1
            return dist;
        }
        if (!hm.containsKey(country2Code)) { // if country2 isn't a bordering country also rerturn -1
            return dist;
        } else { // else if it is a bordering country, parse thru country1Dists to find it and return the capital dist
            for (int i = 0; i < country1Dists.size(); i++) {
                if (country2Code.equals(country1Dists.get(i))) { // I set up my arraylist of vals as {country name, distance}
                    dist = Double.parseDouble(country1Dists.get(i + 1));
                    break;
                }
            }
        }
        return dist;
    }

    /*
     * finds the shortest path between country1 and 2 and returns it in a list<string>
     */
    public static List<String> findPath(String country1, String country2) { // this was terrible omg
        List<String> rV = new ArrayList<>(); // Create empty Arraylist for the sole purpose of
        PriorityQueue<Node> minheap = new PriorityQueue<>(); // create priority queue to hold each of the bordering countries as we evaluate the best path
        HashMap<String, Double> map = new HashMap<>(); // holds all of the countries that we look at
        HashMap<String, String> routes = new HashMap<>(); // holds the path of countries we take
        Double km = 0.0; // will hold the dist
        Double possibleKM = 0.0; // will hold the possible dist, will compare with km
        Node curr; // holds current node
        String startCode = countryCodes.get(country1); // holds country1's country code
        double highestNum = Double.MAX_VALUE; // set this so that we can compare with
        String endCode = ""; // holds country2's code
        for (String cntry : graph.keySet()) { // parses through the graph keyset
            map.put(cntry, highestNum); // puts the highest num value into all keys
        }
        map.put(startCode, 0.0); // start with the first country with a cumulated distance of 0
        minheap.add(new Node(startCode, 0.0)); // add to the priority queue
        while (!minheap.isEmpty()) { // while we still have elements in minheap
            curr = minheap.peek(); // peek at the top and remove
            minheap.remove(curr);
            endCode = countryCodes.get(country2); // get the country code for second country
            if (curr.country.equals(endCode)) { // if we've reached the country we're looking for
                return reversePath(routes, endCode); // pass the hashmap of visited countries, it's reversed so I created a helper function to reverse it
            }
            HashMap<String, Double> currentCntry = graph.get(curr.country); // get the hashmap value from the country key
            for (String newCode : currentCntry.keySet()) { // then parse through it to get the distances we need to compare with what we have
                km = graph.get(curr.country).get(newCode); // get the distance of the country
                possibleKM = curr.dist + km; // and then add the distance to what we have
                if(map.containsKey(newCode)) { // this is so we don't evaluate any nulls
                    if (possibleKM < map.get(newCode)) { // if what we could potentially have is smaller
                        map.put(newCode, possibleKM); // then add that country to our map
                        routes.put(newCode, curr.country); // then add it to our route map
                        minheap.add(new Node(newCode, possibleKM)); // we then add it to the minheap
                    }
                }
            }
        }
        return rV; // if there's no connection between the two countries it will return an empty list
    }

    /*
     * compiles the given hashmap and returns the proper list for the routes
     */
    public static List<String> reversePath(Map<String, String> route, String country2) {
        List<String> countryRoute = new ArrayList<>(); // create a new list to hold the reverse
        String curr = country2;
        while (curr != null) { // just add onto the list until we reach the end
            countryRoute.add(curr); // add the value into the array list
            curr = route.get(curr); // then get the next val
        }
        return reverseHelper(countryRoute); // then call the reverse helper
    }
    /*
     * this is what actually reverses the string
     */
    public static List<String> reverseHelper(List<String> countryRoute) {
        ArrayList<String> rV = new ArrayList<>();
        for(int i = countryRoute.size() - 1; i >= 0; i--) { // it just goes backwards and adds it to the other arraylist so its in the right order
            rV.add(countryRoute.get(i));
        }
        return rV;
    }

    /*
     * accepts user input from main and will keep running until the user stops it
     */
    public static void acceptUserInput() {
        Scanner scan = new Scanner(System.in); // takes in user input
        boolean bool = false; // will keep the loop going
        String c1 = ""; // holds country 1 name
        String c2 = ""; // country 2 name

        while(!(c1.equals("EXIT") && c2.equals("EXIT"))) { // overall big while loop to keep it all going
            while (!bool) { // this one only asks about and collects for country 1
                System.out.print("Enter the name of the first country (type EXIT to quit): ");
                c1 = scan.nextLine();
                if (c1.equals("EXIT")) {
                    return;
                }
                String c1Code = countryCodes.get(c1);
                if (c1Code != null) {
                    country1Name = c1;
                    bool = true;
                } else { // this will keep repeating until a valid input
                    System.out.println("Invalid country name. Please enter a valid country name.");
                }
            }
            bool = false; // resent bool
            while (!bool) { // for the second country to populate those related values
                System.out.print("Enter the name of the second country (type EXIT to quit): ");
                c2 = scan.nextLine();
                if (c2.equals("EXIT")) {
                    return;
                }
                String c2Code = countryCodes.get(c2);
                if (c2Code != null) {
                    country2Name = c2;
                    bool = true;
                } else { // else will keep repeating until valid input
                    System.out.println("Invalid country name. Please enter a valid country name.");
                }
            }
            List<String> rV = findPath(country1Name, country2Name); // use the find path function to get the list of countries to go thru
            if(!rV.isEmpty()) {
                System.out.printf("Route from %s to %s:", country1Name, country2Name); // print the output
            }
            else { // if there is no route
                System.out.println("No route between both countries");
            }
            for (int i = 0; i < rV.size() - 1; i++) { // prints output of strings with printf
                System.out.printf("\n* %s --> %s (%d km.)", reverseCC.get(rV.get(i)), reverseCC.get(rV.get(i + 1)), (int)getDistance(reverseCC.get(rV.get(i)), reverseCC.get(rV.get(i + 1))));
            }
            System.out.println();
            bool = false;

        }
    }
    public static void main(String[] args) throws IOException { // main
        IRoadTrip a3 = new IRoadTrip(args); // initialize all the hash maps
        a3.acceptUserInput(); // start the program
    }

    static class Node implements Comparable<Node>{ // created a node class for my dijkstra's algorithm
        String country; // it holds the country name
        double dist; // the distance we have, this is used in findPath so we can compare and choose the best/smallest route
        public Node(String c, double d) { // constructor
            country = c;
            dist = d;
        }

        @Override
        public int compareTo(Node secCntry) { // I was inspired by Dr. V's code and got fancy by doing an overridden compareTo
            int rV = Double.compare(dist, secCntry.dist);
            return rV;
        }
    }
}

