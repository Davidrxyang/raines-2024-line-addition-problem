/*
 * java class storing public transit construction costs from Eno Transit Cost Database
 * 
 * only data from the United States is stored
 * 
 * csv scanner generated with help frm chatGPT
 * 
 * https://chatgpt.com/share/d39fe75e-f57e-4112-a193-8e85bb0c5466
 * 
 */

package NetworkEvaluation;

import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;


public class CostData {
    
    // store data in parrallel arrays
    ArrayList<String> states; // state (abbreviation)
    ArrayList<String> cities; // specific city
    ArrayList<Double> costs; // final cost per mile adjusted for inflation
    String filename;

    public CostData() {
        states = new ArrayList<String>();
        cities = new ArrayList<String>();
        costs = new ArrayList<Double>();

        try {
            filename = "src/NetworkEvaluation/cost.csv";
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            scanner.nextLine(); // skip header

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data[1].equals("US")) {
                    states.add(data[2]);
                    cities.add(data[3]);
                    costs.add(Double.parseDouble(data[38]));
                }
            }
            scanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CostData data = new CostData();
        System.out.println(data.filename);

        for (int i = 0; i < data.states.size(); i++) {
            System.out.println(data.states.get(i) + " " + data.cities.get(i) + " " + data.costs.get(i));
        }
    }

}
