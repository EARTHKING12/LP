import java.util.*;

public class PassTwoMacroProcessor {
    static class MNTEntry {
        String name;
        int mdtIndex;
        MNTEntry(String name, int mdtIndex) {
            this.name = name;
            this.mdtIndex = mdtIndex;
        }
    }

    static class MDTEntry {
        String line;
        MDTEntry(String line) {
            this.line = line;
        }
    }

    public static void main(String[] args) {
        // ---------- MACRO DEFINITION TABLE (MDT) ----------
        List<MDTEntry> MDT = new ArrayList<>();
        MDT.add(new MDTEntry("MOVER AREG, &ARG1"));
        MDT.add(new MDTEntry("ADD AREG, &ARG2"));
        MDT.add(new MDTEntry("MEND"));
        MDT.add(new MDTEntry("MOVER AREG, &ARG3"));
        MDT.add(new MDTEntry("SUB AREG, &ARG4"));
        MDT.add(new MDTEntry("MEND"));

        // ---------- MACRO NAME TABLE (MNT) ----------
        List<MNTEntry> MNT = new ArrayList<>();
        MNT.add(new MNTEntry("INCR", 0));
        MNT.add(new MNTEntry("DECR", 3));

        // ---------- INPUT CODE (Intermediate code) ----------
        String[] input = {
            "START",
            "INCR N1, N2",
            "DECR N3, N4",
            "END"
        };

        // ---------- OUTPUT ----------
        System.out.println("OUTPUT:");
        for (String line : input) {
            String[] parts = line.split("[ ,]+");
            String mnemonic = parts[0];

            // Check if it is a macro call
            boolean isMacro = false;
            for (MNTEntry mnt : MNT) {
                if (mnt.name.equals(mnemonic)) {
                    isMacro = true;
                    expandMacro(mnt, MDT, parts);
                    break;
                }
            }

            if (!isMacro) {
                System.out.println(line);
            }
        }
    }

    static void expandMacro(MNTEntry mnt, List<MDTEntry> MDT, String[] args) {
        int i = mnt.mdtIndex;
        List<String> arguments = new ArrayList<>();
        for (int j = 1; j < args.length; j++) {
            arguments.add(args[j]);
        }

        while (!MDT.get(i).line.equals("MEND")) {
            String expanded = MDT.get(i).line;
            for (int k = 0; k < arguments.size(); k++) {
                expanded = expanded.replace("&ARG" + (k + 1), arguments.get(k));
            }
            System.out.println(expanded);
            i++;
        }
    }
}
