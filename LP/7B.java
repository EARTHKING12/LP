import java.util.*;

class 6B {

    // ---------- FIFO Page Replacement ----------
    static int fifo(int[] pages, int frames) {
        int[] memory = new int[frames];
        Arrays.fill(memory, -1);
        int pointer = 0, hit = 0, fault = 0;

        System.out.println("\n=== FIFO Page Replacement ===");

        for (int page : pages) {
            boolean found = false;
            for (int f : memory) {
                if (f == page) {
                    found = true;
                    hit++;
                    break;
                }
            }

            if (!found) {
                fault++;
                memory[pointer] = page;
                pointer = (pointer + 1) % frames;
            }

            System.out.print("After accessing " + page + ": ");
            for (int f : memory) {
                if (f != -1) System.out.print(f + " ");
                else System.out.print("- ");
            }
            System.out.println();
        }

        System.out.println("Total Hits: " + hit);
        System.out.println("Total Page Faults: " + fault);
        return fault;
    }

    // ---------- LRU Page Replacement ----------
    static int lru(int[] pages, int frames) {
        ArrayList<Integer> memory = new ArrayList<>(frames);
        int hit = 0, fault = 0;

        System.out.println("\n=== LRU Page Replacement ===");

        for (int page : pages) {
            if (memory.contains(page)) {
                hit++;
                memory.remove((Integer) page);
                memory.add(page);
            } else {
                fault++;
                if (memory.size() == frames)
                    memory.remove(0);
                memory.add(page);
            }

            System.out.print("After accessing " + page + ": ");
            for (int f : memory)
                System.out.print(f + " ");
            for (int i = memory.size(); i < frames; i++)
                System.out.print("- ");
            System.out.println();
        }

        System.out.println("Total Hits: " + hit);
        System.out.println("Total Page Faults: " + fault);
        return fault;
    }

    // ---------- Optimal Page Replacement ----------
    static int optimal(int[] pages, int frames) {
        int[] memory = new int[frames];
        Arrays.fill(memory, -1);
        int hit = 0, fault = 0;

        System.out.println("\n=== Optimal Page Replacement ===");

        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];
            boolean found = false;
            for (int f : memory) {
                if (f == page) {
                    found = true;
                    hit++;
                    break;
                }
            }

            if (!found) {
                fault++;
                int replaceIndex = -1;
                int farthest = i + 1;

                for (int j = 0; j < frames; j++) {
                    if (memory[j] == -1) {
                        replaceIndex = j;
                        break;
                    }

                    int k;
                    for (k = i + 1; k < pages.length; k++) {
                        if (memory[j] == pages[k]) break;
                    }

                    if (k == pages.length) {
                        replaceIndex = j;
                        break;
                    }

                    if (k > farthest) {
                        farthest = k;
                        replaceIndex = j;
                    }
                }

                if (replaceIndex == -1) replaceIndex = 0;
                memory[replaceIndex] = page;
            }

            System.out.print("After accessing " + page + ": ");
            for (int f : memory) {
                if (f != -1) System.out.print(f + " ");
                else System.out.print("- ");
            }
            System.out.println();
        }

        System.out.println("Total Hits: " + hit);
        System.out.println("Total Page Faults: " + fault);
        return fault;
    }

    // ---------- Main Program ----------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter number of pages in reference string: ");
        int n = sc.nextInt();

        int[] pages = new int[n];
        System.out.println("Enter reference string:");
        for (int i = 0; i < n; i++)
            pages[i] = sc.nextInt();

        System.out.println("\nSimulating all algorithms...\n");

        int fifoFaults = fifo(pages, frames);
        int lruFaults = lru(pages, frames);
        int optimalFaults = optimal(pages, frames);

        System.out.println("\n=== Summary Comparison ===");
        System.out.println("-------------------------------------");
        System.out.println("Algorithm\tPage Faults");
        System.out.println("-------------------------------------");
        System.out.println("FIFO\t\t" + fifoFaults);
        System.out.println("LRU\t\t" + lruFaults);
        System.out.println("Optimal\t\t" + optimalFaults);
        System.out.println("-------------------------------------");

        int minFaults = Math.min(fifoFaults, Math.min(lruFaults, optimalFaults));
        if (minFaults == optimalFaults)
            System.out.println("✅ Optimal gives the best performance!");
        else if (minFaults == lruFaults)
            System.out.println("✅ LRU gives the best performance!");
        else
            System.out.println("✅ FIFO gives the best performance!");

        sc.close();
    }
}
