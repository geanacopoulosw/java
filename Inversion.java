import java.util.Scanner;
import java.util.*;

public class Inversion {

    public static void main(String[] args) {
        Scanner myScan = new Scanner(System.in);

        HashMap<String, Integer> loader = new HashMap<>();
        HashMap<String, Boolean> isFive = new HashMap<>();
        int count = 0;
        Boolean isCritic1 = true;
        LinkedList<String> titles = new LinkedList<>();

        while (myScan.hasNextLine()) {
            String line = myScan.nextLine();
            switch (line) {
                case "Critic 1":
                    break;
                case "Critic 2":
                    isCritic1 = false;
                    break;
                default:
                    int number = new Integer(line.substring(0, 1));
                    String title = line.substring(2);
                    if (isCritic1) {
                        if (number == 5) {
                            isFive.put(title, true);
                            loader.put(title, count);
                            count++;
                        } else {
                            isFive.put(title, false);
                            loader.put(title, count);
                            count++;
                        }
                    } else {
                        if (number == 5) {
                            isFive.put(title, true);
                        }
                        titles.add(title);
                    }
                    break;
            }
        }
        LAC inversionAnswer = LAC.mergeSort(titles, isFive, loader);
        System.out.println(inversionAnswer.totalInversions);
    }

    //class that will store the linked list that is in order. it also handles the mergeSort as well as
    //the sortMerge methods that will hand the count of the inversions
    public static class LAC {
        public LinkedList<String> ordered;
        public int totalInversions;

        public LAC(LinkedList ordered, int totalInversions) {
            this.ordered = ordered;
            this.totalInversions = totalInversions;
        }


        //makes sure that the mergeSort is sorted as well as handles all the counting that needs to be
        //done for the inversions.
        public static LAC sortedMerge(LAC listIL, LAC listIR, HashMap<String, Boolean> isFive,
                                      HashMap<String, Integer> loader) {

            LinkedList<String> listL = listIL.ordered;
            LinkedList<String> listR = listIR.ordered;
            LinkedList<String> result = new LinkedList<>();
            int currentCount = listIL.totalInversions + listIR.totalInversions;

            while (listL.size() > 0 && listR.size() > 0) {
                String headOfListL = listL.peek();
                String headOfListR = listR.peek();
                int loaderListL = loader.get(headOfListL);
                int loaderListR = loader.get(headOfListR);
                Boolean isFiveListL = isFive.get(headOfListL);
                Boolean isFiveListR = isFive.get(headOfListR);

                if (loaderListL <= loaderListR) {
                    result.add(headOfListL);
                    listL.pop();
                } else {
                    currentCount += listL.size();
                    result.add(headOfListR);
                    listR.pop();
                    if (isFiveListL || isFiveListR) {
                        currentCount++;
                    }
                }
            }
            while (listL.size() > 0) {
                result.add(listL.peek());
                listL.poll();
            }
            while (listR.size() > 0) {
                result.add(listR.peek());
                listR.poll();
            }
            return new LAC(result, currentCount);
    }

        //mergeSort breaks the list down to left and right with a mid point. it will then break those sides down all
        // the way to the individual value, it will then sort that values to make sure that it is in order.
        public static LAC mergeSort(LinkedList list, HashMap<String, Boolean> isFive,
                                    HashMap<String, Integer> loader) {

            LAC sortedList;
            int middle;

            if (list.size() == 1) {
                return new LAC(list, 0);
            }
            else {
                //middle of the list
                middle = list.size() / 2;

                // mergeSort on left list
                List<String> leftTemp = list.subList(0, middle);
                LinkedList<String> left = new LinkedList<>(leftTemp);
                LAC inversionsOfLeft = (mergeSort(left, isFive, loader));

                // mergeSort on right list
                List<String> rightTemp = list.subList(middle, list.size());
                LinkedList<String> right = new LinkedList<>(rightTemp);
                LAC inversionsOfRight = (mergeSort(right, isFive, loader));

                // Merge the left and right lists
                sortedList = sortedMerge(inversionsOfLeft, inversionsOfRight, isFive, loader);
            }
            return sortedList;
        }
    }
}






