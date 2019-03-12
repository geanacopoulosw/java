import javax.xml.transform.Result;
import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashMap;


public class SchedRequest2 {

    static int MAX_MINUTE = 24 * 60;

    public static void main(String[] args) {
        Scanner myScan = new Scanner(System.in);
        ArrayList<String> process = new ArrayList<>();
        while (myScan.hasNextLine()) {
            String str = myScan.nextLine();
            process.add(str);
        }
        ArrayList<Request> result = Request.processJob(process);
        for (Request req : result){
            System.out.println(req.toString());
        }
    }

    // Here's a handy class for requests that implements some tricky bits for
    // you, including a compareTo method (so that it's a Comparable, so that
    // it can be sorted with Collections.sort()), a hashCode method
    // (so that identical time ranges are treated as identical keys in
    // a hashMap), and an overlaps() method (which students have often
    // gotten wrong in the past by omitting cases).  Parsing, equals(), and
    // toString() are also handled for you.
    public static class Request implements Comparable<Object> {
        private int startMinute;
        private int endMinute;

        // Constructor that takes the request format specified in the
        // assignment (startTime,endTime using 24-hr clock)
        public Request(String inputLine) {
            String[] inputParts = inputLine.split(",");
            startMinute = toMinutes(inputParts[0]);
            endMinute = toMinutes(inputParts[1]);
        }

        // Convert time to an integer number of minutes; mostly
        // for internal use by the class
        private static int toMinutes(String time) {
            String[] timeParts = time.split(":");
            int hour = Integer.valueOf(timeParts[0]);
            int minute = Integer.valueOf(timeParts[1]);
            return hour * 60 + minute;
        }

        // Don't feel like you need to use these accessors, but they're
        // here in case I decide to change the internal representation
        // someday
        public int getStartMinute() {
            return startMinute;
        }

        public int getEndMinute() {
            return endMinute;
        }

        // Did you toString() gets called automatically when your object
        // is put in a situation that expects a String?
        public String toString() {
            return timeToString(startMinute) + "," + timeToString(endMinute);
        }

        // Mostly for use by toString() - format number of minutes as 24hr time
        private static String timeToString(int minutes) {
            if ((minutes % 60) < 10) {
                return (minutes / 60) + ":0" + (minutes % 60);
            }
            return (minutes / 60) + ":" + (minutes % 60);
        }

        // Check whether two Requests overlap in time.
        public boolean overlaps(Request r) {
            // Four kinds of overlap...
            // r starts during this request:
            if (r.getStartMinute() >= getStartMinute() &&
                    r.getStartMinute() < getEndMinute()) {
                return true;
            }
            // r ends during this request:
            if (r.getEndMinute() > getStartMinute() &&
                    r.getEndMinute() < getEndMinute()) {
                return true;
            }
            // r contains this request:
            if (r.getStartMinute() <= getStartMinute() &&
                    r.getEndMinute() >= getEndMinute()) {
                return true;
            }
            // this request contains r:
            if (r.getStartMinute() >= getStartMinute() &&
                    r.getEndMinute() <= getEndMinute()) {
                return true;
            }
            return false;
        }

        // Allows use of Collections.sort() on this object
        // (implements Comparable interface)
        public int compareTo(Object o) {
            if (!(o instanceof Request)) {
                throw new ClassCastException();
            }
            Request r = (Request) o;
            if (r.getEndMinute() > getEndMinute()) {
                return -1;
            } else if (r.getEndMinute() < getEndMinute()) {
                return 1;
            } else if (r.getStartMinute() < getStartMinute()) {
                // Prefer later start times, so sort these first
                return -1;
            } else if (r.getStartMinute() > getStartMinute()) {
                return 1;
            } else {
                return 0;
            }
        }

        // The hash function for the hashMap, without which our scheme
        // of counting requests with the same range would not work.
        // You don't need to call this yourself; it's used every time
        // get(), contains(), or something similar is called
        public int hashCode() {
            return MAX_MINUTE * startMinute + endMinute;
        }

        // Determine whether two objects are equal.  If we're not in a hashing
        // context, other generics will use this to implement functions like
        // contains() or remove().
        public boolean equals(Object o) {
            if (!(o instanceof Request)) {
                return false;
            }
            Request that = (Request) o;
            return (this.startMinute == that.startMinute && this.endMinute == that.endMinute);
        }

        // processJob checks the jobs that are coming in and dictates what to do with that job.
		// if it is "cancel" then you have to remove it from the jobs processing
		// if its not not cancelled you have to add it to the list of processing and sort it.
		// it will return the finished list which means it went through to check to see it was
		// cancelled or not
        public static ArrayList<Request> processJob(ArrayList<String> request) {
            PriorityQueue<Request> processing = new PriorityQueue<>();
            ArrayList<Request> finished = new ArrayList<>();
            HashMap<Request, Integer> all = new HashMap<>();
            int startTime = 0;
            int finishedTime = 0;
            for (String str : request) {
                if (str.contains("cancel")) {
                    Request cancel = new Request(str.substring(7));
                    if (processing.contains(cancel)) {
                        processing.remove(cancel);
                        all.put(cancel, 0);
                    }
                } else if (str.length() <= 5) {
                    int minutes = toMinutes(str);
                    finishedTime = minutes;
                    makeARequest(minutes, startTime, processing, finished, all);
                    startTime = finishedTime;
                } else {
                    Request newRequest = new Request(str);
                    processing.add(newRequest);
                    all.put(newRequest, 1);
                }
            }
            return finished;
        }

        // makeARequest takes a list of requests and checks to see if there are any overlapping my comparing the finish
        // time from the first job that is done and makes sure there is no overlapping of the next job. if there
        // is going to be an overlap then you will not add it to the schedule. This is creating the best schedule
        // possible
        public static void makeARequest(int finishedTime, int startTime, PriorityQueue<Request> processing,
                                        ArrayList<Request> finished, HashMap<Request, Integer> all) {

            if (processing.size() == 0) {
            } else {
                Request next = processing.peek();
                Integer checker = all.get(next);
                if (next.startMinute <= finishedTime) {
                    if (next.startMinute > startTime) {
                        if (finished.size() == 0) {
                            finished.add(next);
                            processing.poll();
                            makeARequest(finishedTime, startTime, processing, finished, all);
                        }
                        else {
                            if (!(next.overlaps(finished.get(finished.size() - 1)))) {
                                finished.add(next);
                                processing.poll();
                                makeARequest(finishedTime, startTime, processing, finished, all);
                            }
                            else {
                                processing.poll();
                                makeARequest(finishedTime, startTime, processing, finished, all);
                            }
                        }
                    }
                    else {
                        processing.remove(next);
                        makeARequest(finishedTime, startTime, processing, finished, all);
                    }
                }
            }
        }
    }
}
