 import java.util.*;

public class Solution {

    /*main that will let us enter in the number that is passed to the methods add() and deleter()
    * it will also calculate the average times taken from the methods to give the average time
    * checking to see if it is resized or not resized for both methods.*/
    public static void main(String[] args) {
        Scanner myScan = new Scanner(System.in);
        ResizingArray r = new ResizingArray();
        System.out.print("Enter a number to test ResizingArray class: ");
        int num = myScan.nextInt();
        for (int i = 0; i < num; i++) {
            r.add(i);
        }
        for (int i = r.n; i >= 0; i--) {
            r.delete();
       }
        //output for the program that calculates the average times to complete each method.
        System.out.println("Add time without resize: " + (r.totalAddTimeNoResize / r.timesAddedNoResize));
        System.out.println("Add time with resize: " + (r.totalAddTimeResize / r.timesAddedResize));
        System.out.println("Delete time without resize: " + (r.totalDeleteTimeNoResize / r.timesDeletedNoResize));
        System.out.println("Delete time with resize: " + (r.totalDeleteTimeResize / r.timesDeletedResize));
        System.out.println("Add time with and without resize: " + (r.totalAddTimeNoResize + r.totalAddTimeResize)
                / (r.timesAddedNoResize + r.timesAddedResize));
        System.out.println("Delete time with and without resize: " +
                (r.totalDeleteTimeResize + r.totalDeleteTimeNoResize)
                        / (r.timesDeletedResize + r.timesDeletedNoResize));
    }

    /*ResizingArray has three method resize add and delete it will adjust the size of
     *the array as needed and will calculate the times and number of iterations to the
     * main to calculate the average*/
    public  static class ResizingArray {
        public int[] arrayNum;
        public int n;
        public int timesAddedNoResize;
        public int timesAddedResize;
        public int timesDeletedNoResize;
        public int timesDeletedResize;
        public long startAdd;
        public long endAdd;
        public long startDelete;
        public long endDelete;
        public long totalAddTimeNoResize;
        public long totalDeleteTimeNoResize;
        public long totalAddTimeResize;
        public long totalDeleteTimeResize;

        public ResizingArray() {
            this.arrayNum = new int[10];
            this.n = 0;
            this.timesAddedNoResize = 0;
            this.timesAddedResize = 0;
            this.timesDeletedNoResize = 0;
            this.timesDeletedResize = 0;
            this.startAdd = 0;
            this.endAdd = 0;
            this.startDelete = 0;
            this.endDelete = 0;
            this.totalAddTimeNoResize = 0;
            this.totalDeleteTimeNoResize = 0;
            this.totalAddTimeResize = 0;
            this.totalDeleteTimeResize = 0;
        }

        /*resize the underlying array holding the elements that have been inputed. it is
         *called in add() and delete() method. it will help to resize the array by doubling
         * or shrinking in half*/
        private void resize(int capacity) {
            //assert capacity >= n;
            int[] temp = new int[capacity];
            for (int i = 0; i < n; i++) {
                temp[i] = arrayNum[i];
            }
            arrayNum = temp;
        }

        /*Adds the item to this array and keeps track if the array needs to be resized by
         *doubling the size of the array. it will do this when the the elements of the array
         *equal the length of the array which is of size 10. else it will keep
         *its size and just keep track of the time it takes to go through the method.*/
        public void add(int num) {
            boolean resized = false;

            //start time
            startAdd = System.nanoTime();

            //doubles the array if necessary
            if (num == arrayNum.length) {
                resize(2 * arrayNum.length);
                resized = true;
            }
            arrayNum[n++] = num;

            //ending time
            endAdd = System.nanoTime();
            if (resized == false) {
                totalAddTimeNoResize += (endAdd - startAdd);
                timesAddedNoResize++;
            }
            else {
                totalAddTimeResize +=  (endAdd - startAdd);
                timesAddedResize++;
            }
        }

        /*deletes the item to this array and keeps track if the array needs to be resized by
         *shrinking the size of the array if the elements drop below the array length of 10.
         *else it will keep its size and just keep track of the time it takes to go through
         * the method.*/
        public void delete() {
            boolean resized = false;
            n--;
            //start time
            startDelete = System.nanoTime();

            // shrink size of array if necessary
            if (n > 0 && n == arrayNum.length / 4) {
                resize(arrayNum.length / 2);
                resized = true;
            }
            //end time
            endDelete = System.nanoTime();
            if (resized == false) {
                totalDeleteTimeNoResize += (endDelete - startDelete);
                timesDeletedNoResize++;
            }
            else {
                totalDeleteTimeResize += (endDelete - startDelete);
                timesDeletedResize++;
            }
        }
    }
}