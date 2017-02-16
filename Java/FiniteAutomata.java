import java.util.*;
import java.io.*;

class FiniteAutomata {


    /*  So this class if fairly self explanatory.
        It's a State class where States act very much
        like nodes; they have a value (their name), and
        a mapping of various paths to other States (where
        the paths (keys) are characters from the DFA and
        destinations (values) are other States).
    */
    static class State {

        private String name;
        private HashMap<String, State> states;

        public State(String n) {
            this.name = n;
            this.states = new HashMap<String, State>();
        }

        public State(String n, HashMap<String, State> s) {
            this.name = n;
            this.states = s;
        }

        public String getName() { return this.name; }

        public HashMap<String, State> getStates() { return this.states; }

        public void setStates(HashMap<String, State> s) { this.states = s; }

        public void addState(String s, State st) { this.states.put(s,st); }

        public String toString() {
            String s = "[";
            for (String key : this.states.keySet()) {
                s += "[" + key + ", " + this.states.get(key).getName() + "]";
            }
            s += "]";
            return this.name + " : " + s;
            // return this.name;
        }

        public boolean equals(State s) {
            return this.name.equals(s.getName());
        }

    }

    /*  So, a DFA class. Since each state has a mapping
        of input -> State, the DFA does not have it's own
        map. Instead it compiles each state's individual
        maps.

        parseString() takes in a string and, starting
        at the start state, breaks the string down character
        by character and follows the path along the DFA as it
        is established among the states' maps. If the last
        state it ends at is an accept state, then it returns
        true. Everything else should output false.

        toString() is a mess of a function but it works (as long
        as the state names are 2 chars long).
    */
    static class DefinitiveFA {

        State[] q;
        char[] alph;
        State start;
        State[] accept;

        public DefinitiveFA(State[] states, char[] alphabet, State s, State[] a) {
            this.q = states;
            this.alph = alphabet;
            this.start = s;
            this.accept = a;
        }

        public State[] getStates() {
            return this.q;
        }

        public State[] getAccept() {
            return this.accept;
        }

        public boolean parseString(String s) {
            if (s.length() == 0) { return false; };
            char currChar;
            State currState = this.start;
            for (int i = 0; i < s.length(); i++) {
                currChar = s.charAt(i);
                if (this.checkChar(currChar)) {
                    try {
                        State nextState = currState.getStates().get(Character.toString(currChar));
                        if (Arrays.asList(this.q).contains(nextState)) {
                            currState = nextState;
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (Arrays.asList(this.accept).contains(currState)) { return true; } else { return false; }
        }

        public boolean checkChar(char c) {
            for (char ch : this.alph) {
                if (c == ch) { return true; }
            }
            return false;
        }

        public boolean checkState(State s) {
            for (State st : this.q) {
                if (st.equals(s)) { return true; }
            }
            return false;
        }

        public String toString() {
            String s = "Q: {";
            for (State st : this.q) {
                s += st.getName() + ", ";
            }
            s = s.substring(0,s.length() - 2) + "}\n∑: {";
            for (char c : this.alph) {
                s += c + ", ";
            }
            s = s.substring(0,s.length() - 2) + "}\nq0: " + this.start.getName() + "\nA: {";
            for (State a : this.accept) {
                s += a.getName() + ", ";
            }
            s = s.substring(0,s.length() - 2) + "}\n𝛿: {\n        ";

            int gridCount = 0;

            for (char c : this.alph) {
                s += "|  " + c + " ";
                gridCount += 1;
            }
            s += "\n    ----";

            for (int i = 0; i < gridCount; i++) {
                s += "+----";
            }

            for (State st : this.q) {
                s += "\n     " + st.getName() + " ";
                HashMap<String, State> map = st.getStates();
                for (char c : this.alph) {
                    s += "| " + map.get(Character.toString(c)).getName() + " ";
                }
                s += "\n    ----";

                for (int i = 0; i < gridCount; i++) {
                    s += "+----";
                }
            }

            s += "\n   }";




            /*

                |  0 |  1
            ----+----+----
             q0 | q0 | q1
            ----+----+----
             q1 | q2 | q0
            ----+----+----
             q2 | q1 | q2

            */
             return s;
         }

     }

     /* It takes input!
     */
     public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream printer = new PrintStream(System.out);

        printer.print("Enter names of the states separated with spaces: ");
        String[] names = in.nextLine().split(" ");

        printer.print("Enter the alphabet separated with spaces: ");
        String[] alphabet = in.nextLine().split(" ");        

        String[][] transitions = new String[names.length][];

        for (int i = 0; i < names.length; i++) {
            printer.print("Enter state transitions for " + names[i] + " (i.e. [0,q2] [1,q1] ...) split by spaces: ");
            transitions[i] = in.nextLine().split(" ");
        }

        printer.print("Enter name of the start state: ");
        String startState = in.nextLine();

        printer.print("Enter names of the accept states separated with spaces: ");
        String[] acceptStates = in.nextLine().split(" ");




        State[] q = new State[names.length];
        char[] a = new char[alphabet.length];
        State s = null;
        State[] acc = new State[acceptStates.length];

        String[] tran = new String[2];
        String nextKey;
        State nextState = null;

        for (int i = 0; i < q.length; i++) {
            q[i] = new State(names[i]);
            if (startState.equals(names[i])) {
                s = q[i];
            }
        }

        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < transitions[i].length; j++) {
                tran = transitions[i][j].replace("[","").replace("]","").split(",");
                nextKey = tran[0];
                for (State st : q) {
                    if (st != null && tran[1].equals(st.getName())) {
                        nextState = st;
                    }
                }
                q[i].addState(nextKey, nextState);
            }
        }

        for (int i = 0; i < a.length; i++) {
            a[i] = alphabet[i].charAt(0);
        }

        
        for (int i = 0; i < acc.length; i++) {
            for (int j = 0; j < acceptStates.length; j++) {
                for (int k = 0; k < q.length; k++) {
                    if (acceptStates[j].equals(q[k].getName())) {
                        acc[i] = q[k];
                        k = q.length;
                        j = acceptStates.length;
                    }
                }
            }
        }

        DefinitiveFA defNA = new DefinitiveFA(q, a, s, acc);

        printer.println("\n\n\n" + defNA + "\n\n\n");

        printer.print("Enter string to parse (QUIT to exit): ");

        String input;
        while (in.hasNextLine()) {
            input = in.next();
            if (!input.equals("QUIT")) {
                System.out.println("\nParsing " + input + ": " + (defNA.parseString(input.toString()) ? "YES" : "NO") + "\n");
                printer.print("Enter string to parse (QUIT to exit): ");
            } else {
                break;
            }
        }

        printer.println("        CCCCCCCCCCCCCEEEEEEEEEEEEEEEEEEEEEELLLLLLLLLLL             EEEEEEEEEEEEEEEEEEEEEEBBBBBBBBBBBBBBBBB   RRRRRRRRRRRRRRRRR                  AAA         TTTTTTTTTTTTTTTTTTTTTTTEEEEEEEEEEEEEEEEEEEEEE\n     CCC::::::::::::CE::::::::::::::::::::EL:::::::::L             E::::::::::::::::::::EB::::::::::::::::B  R::::::::::::::::R                A:::A        T:::::::::::::::::::::TE::::::::::::::::::::E\n   CC:::::::::::::::CE::::::::::::::::::::EL:::::::::L             E::::::::::::::::::::EB::::::BBBBBB:::::B R::::::RRRRRR:::::R              A:::::A       T:::::::::::::::::::::TE::::::::::::::::::::E\n  C:::::CCCCCCCC::::CEE::::::EEEEEEEEE::::ELL:::::::LL             EE::::::EEEEEEEEE::::EBB:::::B     B:::::BRR:::::R     R:::::R            A:::::::A      T:::::TT:::::::TT:::::TEE::::::EEEEEEEEE::::E\n C:::::C       CCCCCC  E:::::E       EEEEEE  L:::::L                 E:::::E       EEEEEE  B::::B     B:::::B  R::::R     R:::::R           A:::::::::A     TTTTTT  T:::::T  TTTTTT  E:::::E       EEEEEE\nC:::::C                E:::::E               L:::::L                 E:::::E               B::::B     B:::::B  R::::R     R:::::R          A:::::A:::::A            T:::::T          E:::::E             \nC:::::C                E::::::EEEEEEEEEE     L:::::L                 E::::::EEEEEEEEEE     B::::BBBBBB:::::B   R::::RRRRRR:::::R          A:::::A A:::::A           T:::::T          E::::::EEEEEEEEEE   \nC:::::C                E:::::::::::::::E     L:::::L                 E:::::::::::::::E     B:::::::::::::BB    R:::::::::::::RR          A:::::A   A:::::A          T:::::T          E:::::::::::::::E   \nC:::::C                E:::::::::::::::E     L:::::L                 E:::::::::::::::E     B::::BBBBBB:::::B   R::::RRRRRR:::::R        A:::::A     A:::::A         T:::::T          E:::::::::::::::E   \nC:::::C                E::::::EEEEEEEEEE     L:::::L                 E::::::EEEEEEEEEE     B::::B     B:::::B  R::::R     R:::::R      A:::::AAAAAAAAA:::::A        T:::::T          E::::::EEEEEEEEEE   \nC:::::C                E:::::E               L:::::L                 E:::::E               B::::B     B:::::B  R::::R     R:::::R     A:::::::::::::::::::::A       T:::::T          E:::::E             \n C:::::C       CCCCCC  E:::::E       EEEEEE  L:::::L         LLLLLL  E:::::E       EEEEEE  B::::B     B:::::B  R::::R     R:::::R    A:::::AAAAAAAAAAAAA:::::A      T:::::T          E:::::E       EEEEEE\n  C:::::CCCCCCCC::::CEE::::::EEEEEEEE:::::ELL:::::::LLLLLLLLL:::::LEE::::::EEEEEEEE:::::EBB:::::BBBBBB::::::BRR:::::R     R:::::R   A:::::A             A:::::A   TT:::::::TT      EE::::::EEEEEEEE:::::E\n   CC:::::::::::::::CE::::::::::::::::::::EL::::::::::::::::::::::LE::::::::::::::::::::EB:::::::::::::::::B R::::::R     R:::::R  A:::::A               A:::::A  T:::::::::T      E::::::::::::::::::::E\n     CCC::::::::::::CE::::::::::::::::::::EL::::::::::::::::::::::LE::::::::::::::::::::EB::::::::::::::::B  R::::::R     R:::::R A:::::A                 A:::::A T:::::::::T      E::::::::::::::::::::E\n        CCCCCCCCCCCCCEEEEEEEEEEEEEEEEEEEEEELLLLLLLLLLLLLLLLLLLLLLLLEEEEEEEEEEEEEEEEEEEEEEBBBBBBBBBBBBBBBBB   RRRRRRRR     RRRRRRRAAAAAAA                   AAAAAAATTTTTTTTTTT      EEEEEEEEEEEEEEEEEEEEEE");


        // HashMap<String, State> s0Map = new HashMap<String, State>();
        // HashMap<String, State> s1Map = new HashMap<String, State>();
        // HashMap<String, State> s2Map = new HashMap<String, State>();

        // State s0 = new State("q0", s0Map, "");
        // State s1 = new State("q1", s1Map, "");
        // State s2 = new State("q2", s2Map, "");

        // s0.addState("0", s0);
        // s0.addState("1", s1);

        // s1.addState("0", s2);
        // s1.addState("1", s0);

        // s2.addState("0", s1);
        // s2.addState("1", s2);

        // State[] q = {s0,s1,s2};
        // char[] alphabet = {'0', '1'};
        // State[] a = {s0};


        // DefinitiveFA dfa = new DefinitiveFA(q, alphabet, s0, a);

        // System.out.println("Parse _: " + dfa.parseString(""));
        // System.out.println("Parse 0: " + dfa.parseString("0"));
        // System.out.println("Parse 1: " + dfa.parseString("1"));
        // System.out.println("Parse 1000: " + dfa.parseString("1000"));
        // System.out.println("Parse 100: " + dfa.parseString("100"));
        // System.out.println("Parse 110: " + dfa.parseString("110"));
        // System.out.println("Parse 11: " + dfa.parseString("11"));
        // System.out.println("Parse 1001: " + dfa.parseString("1001"));
        // System.out.println("Parse 101110111000: " + dfa.parseString("101110111000"));
        // System.out.println("Parse 101110111001: " + dfa.parseString("101110111001"));
        // System.out.println("Parse 101110111011: " + dfa.parseString("101110111011"));

        // System.out.println("\n\n\n\n\n" + dfa);
    }

}