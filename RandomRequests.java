// RandomRequests:  Generate random inputs for interval scheduling.

import java.util.Random;
import java.util.HashSet;
import java.util.Iterator;

public class RandomRequests {
    public static void main(String[] args) {
        Random rng = new Random();
        int MAX_MINUTE = 24*60;
        int lastMinutePrinted = 0;

        HashSet<Request> existingRequests = new HashSet<Request>();

        int requestsToPrint = new Integer(args[0]);
        for (int i = 0; i < requestsToPrint; i++) {
            // Simulate time passing as requests come in
            int thisMinute = i * MAX_MINUTE / requestsToPrint;
            if (thisMinute > MAX_MINUTE - 60) {
                // Keep requests from getting dumb:  no rescheduling in last hour
                thisMinute = MAX_MINUTE - 60;
            }
            if (thisMinute > lastMinutePrinted) {
                System.out.println(minutesToTime(thisMinute));
                lastMinutePrinted = thisMinute;

                // Remove all irrelevant requests (start time now or earlier)
                Iterator<Request> iter = existingRequests.iterator();
                while (iter.hasNext()) {
                    Request r = iter.next();
                    if (r.startMinute <= thisMinute) {
                        iter.remove();
                    }
                }
            }

            // Requests are now always for the future
            int startMinute = thisMinute + 1 + rng.nextInt(MAX_MINUTE-thisMinute-2);
            int endMinute = startMinute+1 + rng.nextInt(MAX_MINUTE-startMinute-1);
            Request newRequest = new Request(startMinute, endMinute);

            // One in five is a cancellation, in addition to collisions being
            // cancellations
            if (existingRequests.contains(newRequest)) {
                // Cancel an existing request on collision.
                existingRequests.remove(newRequest);
                System.out.println("cancel " + newRequest);
            } else if (existingRequests.size() > 0 && rng.nextInt(5) == 0) {
                // Just remove the first arbitrary thing.
                Request firstThing = existingRequests.iterator().next();
                System.out.println("cancel " + firstThing);
                existingRequests.remove(firstThing);
            } else {
                existingRequests.add(newRequest);
                System.out.println(newRequest);
            }
        }
        System.out.println("23:59");
    }

    public static String minutesToTime(int minutes) {
        if ((minutes%60) < 10) {
            return (minutes/60) + ":0" + (minutes%60);
        }
        return (minutes/60) + ":" + (minutes%60);
    }

    public static class Request {
        public int startMinute;
        public int endMinute;
        
        public Request(int s, int e) {
            startMinute = s;
            endMinute = e;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Request)) return false;
            Request other = (Request) o;
            return startMinute == other.startMinute && endMinute == other.endMinute;
        }

        public String toString() {
            return minutesToTime(startMinute) + "," + 
                   minutesToTime(endMinute);
        }
    }
}
