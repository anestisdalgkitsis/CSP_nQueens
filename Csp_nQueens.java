package csp_n.queens;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Random;

/*
 * @author anestisdalgkitsis, @586
 */
public class Csp_nQueens {
    
    // Terminal UI Fuctions
    
    static int UImenu() {

        boolean flag = false;
        
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n Please select an algorithm to solve N-Queens problem.\n\n 1.] Min-conflicts with restarts\n 2.] Min-conflicts with random walk\n 3.] Simulated Annealing\n");
            if(scanner.hasNextInt()){
                int option = scanner.nextInt();
                if (option == 1) {
                    System.out.println();
                    return 1;
                } else if (option == 2) {
                    System.out.println();
                    return 2;
                } else if (option == 3) {
                    System.out.println();
                    return 3;
                } else {
                    System.out.println("\nThat's not a valid option.\n\n");
                    flag = false;
                }
            } else {
                System.out.println();
                System.out.println("Type a number.\n\n");
                flag = false;
            }
        } while (flag == false);
        
        return -1;
    }
    
    static int UIgetn() {
        
        boolean flag = false;
        
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  |\n  |- Problem Scale: ");
            if(scanner.hasNextInt()){
                int option = scanner.nextInt();
                return option;
            } else {
                System.out.println(" [!] Type an integer.");
                flag = false;
            }
        } while (flag == false);
        
        return -1;
    }

    static int UIgetmoves() {
        
        boolean flag = false;
        
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  |\n  |- Maximum Moves: ");
            if(scanner.hasNextInt()){
                int option = scanner.nextInt();
                return option;
            } else {
                System.out.println(" [!] Type an integer.");
                flag = false;
            }
        } while (flag == false);
        
        return -1;
    }

    static int UIgetrestarts() {
        
        boolean flag = false;
        
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  |\n  |- Maximum Restarts: ");
            if(scanner.hasNextInt()){
                int option = scanner.nextInt();
                return option;
            } else {
                System.out.println(" [!] Type an integer.");
                flag = false;
            }
        } while (flag == false);
        
        return -1;
    }
    
    static double UIgetp() {
        
        boolean flag = false;
        
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  |\n  |- Random Move Selection Probability: ");
            if(scanner.hasNextDouble()){
                double option = scanner.nextDouble();
                if (option < 0) {
                    System.out.println(" [!] Type a positive float number between 0 and 1.");
                    flag = false;                    
                }
                if (option > 1) {
                    System.out.println(" [!] Type a float number between 0 and 1.");
                    flag = false;                    
                }                
                return option;
            } else {
                System.out.println(" [!] Type a float number between 0 and 1.");
                flag = false;
            }
        } while (flag == false);
        
        return -1;
    }    
    
    static int UIgettheta() {
        
        boolean flag = false;
        
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  |\n  |- Temperature reduction schedule: ");
            if(scanner.hasNextInt()){
                int option = scanner.nextInt();
                return option;
            } else {
                System.out.println(" [!] Type an integer.");
                flag = false;
            }
        } while (flag == false);
        
        return -1;
    }    
    
    // Utility Functions 
    
    static int UFgeneraterandomint(int min, int max) {
        
        int randomint = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomint;
    }
    
    static int UFfindconflicts (int[] qposbycolumn, int row, int column) { // Returns the number of Queens that conflict with.
        
        int count = 0;
        
        for (int i = 0; i < qposbycolumn.length; i++) {
            if (i == column) continue;
            int r = qposbycolumn[i];
            if (r == row || Math.abs(r - row) == Math.abs(i - column)) {
                count++;
            }
        }
        return count;
    }
    
    static void UFgeneratenqeenmap (int[] qposbycolumn) { // Generates a Queen map
        
        Random random = new Random();
        
        for (int i = 0, n = qposbycolumn.length; i < n; i++) {
            qposbycolumn[i] = i;
        }
        
        System.out.println(" [i] Queen Map Generated.");
        
        for (int i = 0, n = qposbycolumn.length; i < n; i++) {
            int j = random.nextInt(n);
            int rowToSwap = qposbycolumn[i];
            qposbycolumn[i] = qposbycolumn[j];
            qposbycolumn[j] = rowToSwap;
        }
        
        System.out.println(" [i] Queen Map Scrambled.");
    }
    
    static int UFgettotalqueenmapcost(int[] qposbycolumn) {
        
        int totalcost = 0;
        
        for (int i = 0, n = qposbycolumn.length; i < n; i++) {
            for (int j = 0; j < n; j++) {
                totalcost += UFfindconflicts(qposbycolumn, i, j);
            }
        }
        
        // Due to double counting
        totalcost = totalcost / 2;
        return totalcost;
    }
    
    // Algorithm Procedures
    
    static void APrunminconflictswithrestarts(int[] qposbycolumn, int maxmoves, int maxrestarts) {
        
        int moves = 0;
        int restarts = 0;
        Random random = new Random();

        // Candidate Queens to reposition
        ArrayList<Integer> candidates = new ArrayList<Integer>();

        while (true) {

            // Find Queens with conflicts
            int maxConflicts = 0;
            candidates.clear();
            for (int i = 0; i < qposbycolumn.length; i++) {
                int conflicts = UFfindconflicts(qposbycolumn, qposbycolumn[i], i);
                if (conflicts == maxConflicts) {
                    candidates.add(i);
                } else if (conflicts > maxConflicts) { 
                    maxConflicts = conflicts;
                    candidates.clear();
                    candidates.add(i);
                }
            }

            // All Queens have no conflicts. Problem is solved.
            if (maxConflicts == 0) {
                System.out.println(" [#] Finished.");
                System.out.println("  |- Total Moves = " + moves);
                System.out.println("  |- Total Restarts = " + restarts);
                System.out.println("  |- Total Conflicts = " + maxConflicts);
                return;
            }

            // Pick a random Queen from those that had conflicts
            int worstQueenColumn = candidates.get(random.nextInt(candidates.size()));

            // Move her to the place with the least conflicts.
            int minConflicts = qposbycolumn.length;
            candidates.clear();
            for (int r = 0; r < qposbycolumn.length; r++) {
                int conflicts = UFfindconflicts(qposbycolumn, r, worstQueenColumn);
                if (conflicts == minConflicts) {
                    candidates.add(r);
                } else if (conflicts < minConflicts) {
                    minConflicts = conflicts;
                    candidates.clear();
                    candidates.add(r);
                }
            }
            if (!candidates.isEmpty()) {
                qposbycolumn[worstQueenColumn] = candidates.get(random.nextInt(candidates.size()));
            }

            // Stop if the maximum amount of moves has been reached
            if (moves >= maxmoves) {
                System.out.println(" [!] Maximum amount of moves reached! No solution found in the range of " + maxmoves + " moves.");
                System.out.println(" [#] Finished.");
                return;
            }
            
            moves++;
            
            // Restart if the calculation is taking too much time or stop if the maximum amount of restarts has been reached
            if (moves == qposbycolumn.length * 2) {
                if (restarts >= maxrestarts) {
                    System.out.println(" [!] Maximum amount of restarts reached! No solution found in the range of " + maxrestarts + " restarts.");
                    System.out.println(" [#] Finished.");
                    return;
                }
                System.out.println(" [!] No solution found! Starting over.");
                UFgeneratenqeenmap(qposbycolumn);
                moves = 0;
                restarts++;
            } 
        }
    }
    
    static void APrunminconflictswithrandomwalk(int[] qposbycolumn, int maxmoves, double p) {
        
        int moves = 0;
        Random random = new Random();

        // Candidate Queens to reposition
        ArrayList<Integer> candidates = new ArrayList<Integer>();
        
        while(true) {
            
            // Find the Queen with mosts conflicts
            int maxConflicts = 0;
            candidates.clear();
            for (int i = 0; i < qposbycolumn.length; i++) {
                int conflicts = UFfindconflicts(qposbycolumn, qposbycolumn[i], i);
                if (conflicts == maxConflicts) {
                    candidates.add(i);
                } else if (conflicts > maxConflicts) { 
                    maxConflicts = conflicts;
                    candidates.clear();
                    candidates.add(i);
                }
            }

            // All Queens have no conflicts. Problem is solved.
            if (maxConflicts == 0) {
                System.out.println(" [#] Finished.");
                System.out.println("  |- Total Moves = " + moves);
                System.out.println("  |- Total Conflicts = " + maxConflicts);
                return;
            }
            
            // Pick a random Queen from those that had conflicts
            int worstQueenColumn = candidates.get(random.nextInt(candidates.size()));
                    
            // If probability p is verified, pick a random Queen from those that had conflicts except the one picked before.
            double guessp = random.nextDouble();
            if (guessp  == p) {
                int worstQueenColumn2;
                do {
                    worstQueenColumn2 = candidates.get(random.nextInt(candidates.size()));
                } while (worstQueenColumn2 != worstQueenColumn);
                worstQueenColumn = worstQueenColumn2;
            } else {
                // Move her to the place with the least conflicts.
                int minConflicts = qposbycolumn.length;
                candidates.clear();
                for (int r = 0; r < qposbycolumn.length; r++) {
                    int conflicts = UFfindconflicts(qposbycolumn, r, worstQueenColumn);
                    if (conflicts == minConflicts) {
                        candidates.add(r);
                    } else if (conflicts < minConflicts) {
                        minConflicts = conflicts;
                        candidates.clear();
                        candidates.add(r);
                    }
                }
            }
            if (!candidates.isEmpty()) {
                qposbycolumn[worstQueenColumn] = candidates.get(random.nextInt(candidates.size()));
            }
            
            // Stop if the maximum amount of moves has been reached.
            if (moves >= maxmoves) {
                System.out.println(" [!] Maximum amount of moves reached! No solution found in the range of " + maxmoves + " moves.");
                System.out.println(" [#] Finished.");
                return;
            }
            
            moves++;
            
            // Stop if the calculation is taking too much time
            if (moves == qposbycolumn.length * 2) {
                System.out.println(" [!] No solution found!");
                System.out.println(" [#] Finished.");
                return;
            }
        }
        
    }
    
    static void APsimulatedannealing(int[] qposbycolumn, int theta) {

        int moves = 0;
        int delta;
        int maxConflicts = 0;
        int keeptheta = theta;
        int[] previousqposbycolumn = new int[qposbycolumn.length];
        Random random = new Random();

        // Candidate Queens to reposition
        ArrayList<Integer> candidates = new ArrayList<Integer>();
        
        while (true) {    
            
            // Reset temperature for the next cycle
            theta = keeptheta;
            
            while ((theta > 0) && (UFgettotalqueenmapcost(qposbycolumn) > 0)) {
                
                // Keep a copy of the current Queen map, we may need to revert back to it again
                previousqposbycolumn = qposbycolumn.clone();
                // Calculates next step

                // Find the Queen with mosts conflicts
                maxConflicts = 0;
                candidates.clear();
                for (int i = 0; i < qposbycolumn.length; i++) {
                    int conflicts = UFfindconflicts(qposbycolumn, qposbycolumn[i], i);
                    if (conflicts == maxConflicts) {
                        candidates.add(i);
                    } else if (conflicts > maxConflicts) { 
                        maxConflicts = conflicts;
                        candidates.clear();
                        candidates.add(i);
                    }
                }
                
                // Pick a random Queen from those that had conflicts
                int worstQueenColumn = candidates.get(random.nextInt(candidates.size()));

                // Move her to the place with the least conflicts.
                int minConflicts = qposbycolumn.length;
                candidates.clear();
                for (int r = 0; r < qposbycolumn.length; r++) {
                    int conflicts = UFfindconflicts(qposbycolumn, r, worstQueenColumn);
                    if (conflicts == minConflicts) {
                        candidates.add(r);
                    } else if (conflicts < minConflicts) {
                        minConflicts = conflicts;
                        candidates.clear();
                        candidates.add(r);
                    }
                }
                if (!candidates.isEmpty()) {
                    qposbycolumn[worstQueenColumn] = candidates.get(random.nextInt(candidates.size()));
                }

                // Get the cost difference between this and the previous state
                delta = UFgettotalqueenmapcost(previousqposbycolumn) - UFgettotalqueenmapcost(qposbycolumn);

                // Compare to random propability
                double probability = Math.exp(delta / theta);
                double rand = Math.random();

                if (delta > 0) {
                    // we keep this change
                } else if (rand <= probability) {
                    // we keep this change
                } else {
                    // Revert back to previous state
                    System.out.println(" [i] Reverted to older state.");
                    qposbycolumn = previousqposbycolumn.clone();
                }

                moves++;
                theta--;
            }
            
            // All Queens have no conflicts. Problem is solved.
            if (maxConflicts == 0) {
                System.out.println(" [#] Finished.");
                System.out.println("  |- Total Moves = " + moves);
                System.out.println("  |- Total Conflicts = " + maxConflicts);
                return;
            }
        }
    }
    
    // Main Function
    
    public static void main(String[] args) {
        
        // Initalize
        
        Scanner scanner = new Scanner(System.in);
                
        int n;
        int moves;
        int restarts;
        double p;
        int theta; 
        
        int[] qposbycolumn;
        
        // Loop
        
        while(true) {
            
            int option = UImenu();
        
            if (option == 1) { // Min-conflicts with restarts
                System.out.println(" [i] Min-conflicts with restarts\n  |- Please define given algorithm parameters");
                // Get algorithm parameters
                n = UIgetn();
                moves = UIgetmoves();
                restarts = UIgetrestarts();
                // Setup algorithm
                System.out.println("\n Log: \n\n [>] Running");
                qposbycolumn = new int[n];
                UFgeneratenqeenmap(qposbycolumn);
                // Run algorithm
                APrunminconflictswithrestarts(qposbycolumn, moves, restarts);
            } else if (option == 2) { // Min-conflicts with random walk
                System.out.println(" [i] Min-conflicts with random walk\n  |- Please define given algorithm parameters");
                // Get algorithm parameters
                n = UIgetn();
                moves = UIgetmoves();
                p = UIgetp();
                // Setup algorithm
                System.out.println("\n Log: \n\n [>] Running");
                qposbycolumn = new int[n];
                UFgeneratenqeenmap(qposbycolumn);
                // Run algorithm
                APrunminconflictswithrandomwalk(qposbycolumn, moves, p);
            } else if (option == 3){ // Simulated Annealing
                System.out.println(" [i] Simulated Annealing\n  |- Please define given algorithm parameters");
                // Get algorithm parameters
                n = UIgetn();
                theta = UIgettheta();
                // Setup algorithm
                System.out.println("\n Log: \n\n [>] Running");
                qposbycolumn = new int[n];
                UFgeneratenqeenmap(qposbycolumn);
                // Run algorithm
                APsimulatedannealing(qposbycolumn, theta);
            } else {
                return;
            }
        }
    }
}
