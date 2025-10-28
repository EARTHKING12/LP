import java.util.Arrays;
import java.util.Scanner;

public class 6B {  // Changed class name to Main
    static int[] memoryBlocks;
    static int[] processSize;
    static int lastAllocatedIndex = 0; // used by Next Fit

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of memory blocks: ");
        int m = sc.nextInt();
        memoryBlocks = new int[m];
        System.out.println("Enter sizes of memory blocks (space separated):");
        for (int i = 0; i < m; i++) memoryBlocks[i] = sc.nextInt();

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        processSize = new int[n];
        System.out.println("Enter sizes of processes (space separated):");
        for (int i = 0; i < n; i++) processSize[i] = sc.nextInt();

        // Run all allocation strategies
        firstFit();
        nextFit();
        bestFit();
        worstFit();

        sc.close();
    }

    // FIRST FIT
    static void firstFit() {
        int[] allocation = new int[processSize.length];
        Arrays.fill(allocation, -1);
        int[] blocks = Arrays.copyOf(memoryBlocks, memoryBlocks.length);

        for (int i = 0; i < processSize.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[j] >= processSize[i]) {
                    allocation[i] = j;
                    blocks[j] -= processSize[i];
                    break;
                }
            }
        }
        printAllocation(allocation, blocks, "First Fit");
    }

    // NEXT FIT
    static void nextFit() {
        int[] allocation = new int[processSize.length];
        Arrays.fill(allocation, -1);
        int[] blocks = Arrays.copyOf(memoryBlocks, memoryBlocks.length);
        int j = lastAllocatedIndex;

        for (int i = 0; i < processSize.length; i++) {
            int scanned = 0;
            while (scanned < blocks.length) {
                if (blocks[j] >= processSize[i]) {
                    allocation[i] = j;
                    blocks[j] -= processSize[i];
                    lastAllocatedIndex = j;
                    break;
                }
                j = (j + 1) % blocks.length;
                scanned++;
            }
        }
        printAllocation(allocation, blocks, "Next Fit");
    }

    // BEST FIT
    static void bestFit() {
        int[] allocation = new int[processSize.length];
        Arrays.fill(allocation, -1);
        int[] blocks = Arrays.copyOf(memoryBlocks, memoryBlocks.length);

        for (int i = 0; i < processSize.length; i++) {
            int bestIdx = -1;
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[j] >= processSize[i]) {
                    if (bestIdx == -1 || blocks[j] < blocks[bestIdx]) {
                        bestIdx = j;
                    }
                }
            }
            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                blocks[bestIdx] -= processSize[i];
            }
        }
        printAllocation(allocation, blocks, "Best Fit");
    }

    // WORST FIT
    static void worstFit() {
        int[] allocation = new int[processSize.length];
        Arrays.fill(allocation, -1);
        int[] blocks = Arrays.copyOf(memoryBlocks, memoryBlocks.length);

        for (int i = 0; i < processSize.length; i++) {
            int worstIdx = -1;
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[j] >= processSize[i]) {
                    if (worstIdx == -1 || blocks[j] > blocks[worstIdx]) {
                        worstIdx = j;
                    }
                }
            }
            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                blocks[worstIdx] -= processSize[i];
            }
        }
        printAllocation(allocation, blocks, "Worst Fit");
    }

    // PRINT RESULT
    static void printAllocation(int[] allocation, int[] remainingBlocks, String method) {
        System.out.println("\n--- " + method + " Allocation ---");
        System.out.println("Process#\tSize\tBlock# (1-based)\tStatus");
        for (int i = 0; i < processSize.length; i++) {
            System.out.print((i + 1) + "\t\t" + processSize[i] + "\t");
            if (allocation[i] != -1) {
                System.out.println((allocation[i] + 1) + "\t\tAllocated");
            } else {
                System.out.println("-\t\tNot Allocated");
            }
        }
        System.out.println("\nRemaining sizes of memory blocks:");
        for (int i = 0; i < remainingBlocks.length; i++) {
            System.out.println("Block " + (i + 1) + ": " + remainingBlocks[i]);
        }
    }
}
