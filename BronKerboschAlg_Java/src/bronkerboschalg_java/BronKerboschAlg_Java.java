/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bronkerboschalg_java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author SWATHI YANDAPALLY
 */
public class BronKerboschAlg_Java {

    ArrayList<Node> Graph = new ArrayList<Node>();
    int userinput;

    public static void main(String[] args) throws IOException {
        int vertexCount;
        int edgesCount;

        try {
            long startTime = System.currentTimeMillis();
            BronKerboschAlg_Java maxcliques = new BronKerboschAlg_Java();

            String firstNumber;
            firstNumber = JOptionPane.showInputDialog("Sample datasets start vertex  either with 1 or 0. Please enter either 1 or 0 based on your dataset.");
            maxcliques.userinput = Integer.parseInt(firstNumber);
            switch (maxcliques.userinput) {
                case 1: {
                    break;
                }
                case 0: {

                    break;
                }
                default:
                    JOptionPane.showMessageDialog(null, "The input enetered was wrong", "Error", JOptionPane.PLAIN_MESSAGE);
                    System.exit(0);

            }
            JFileChooser fileopen = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("c files", "c");
            fileopen.addChoosableFileFilter(filter);

            int ret = fileopen.showDialog(null, "Open file");

            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();

                BufferedReader br = new BufferedReader(new FileReader(file));

                vertexCount = Integer.parseInt(br.readLine());
                switch (maxcliques.userinput) {
                    case 1: {
                        for (int i = 1; i <= vertexCount; i++) {
                            Node N = new Node();
                            N.setNode(i);
                            maxcliques.Graph.add(N);
                        }
                        ArrayList<Node> X = new ArrayList<Node>();
                        ArrayList<Node> R = new ArrayList<Node>();
                        ArrayList<Node> P = new ArrayList<Node>(maxcliques.Graph);
                        edgesCount = Integer.parseInt(br.readLine());
                        for (int i = 1; i <= edgesCount; i++) {
                            String[] strNeighbors = br.readLine().split(" ");
                            Node Node1 = maxcliques.Graph.get(Integer.parseInt(strNeighbors[0]) - 1);
                            Node Node2 = maxcliques.Graph.get(Integer.parseInt(strNeighbors[1]) - 1);
                            Node1.addNeighbors(Node2);
                        }
                        //Bron Kerbusch algorithm with Pivot

                        maxcliques.Bron_KerboschWithPivot(R, P, X);
                        //Bron Kerbusch algorithm without Pivot
                        maxcliques.Bron_KerboschWithNOPivot(R, P, X);
                        break;
                    }
                    case 0: {
                        for (int i = 0; i < vertexCount; i++) {
                            Node N = new Node();
                            N.setNode(i);
                            maxcliques.Graph.add(N);
                        }
                        ArrayList<Node> X = new ArrayList<Node>();
                        ArrayList<Node> R = new ArrayList<Node>();
                        ArrayList<Node> P = new ArrayList<Node>(maxcliques.Graph);
                        edgesCount = Integer.parseInt(br.readLine());
                        for (int i = 1; i <= edgesCount; i++) {
                            String[] strNeighbors = br.readLine().split(" ");
                            Node Node1 = maxcliques.Graph.get(Integer.parseInt(strNeighbors[0]));
                            Node Node2 = maxcliques.Graph.get(Integer.parseInt(strNeighbors[1]));
                            Node1.addNeighbors(Node2);
                        }

                        //Bron Kerbusch algorithm with Pivot
                        System.out.println("Bron Kerbusch Algorithm with Pivot:");
                        System.out.println();

                        maxcliques.Bron_KerboschWithPivot(R, P, X);
                        System.out.println();

                        //Bron Kerbusch algorithm without Pivot
//                        System.out.println("Bron Kerbusch Algorithm with out Pivot:");
//                        System.out.println();
//
//                        maxcliques.Bron_KerboschWithNOPivot(R, P, X);
                        break;
                    }
                    default:

                        System.exit(0);

                }
            }
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Totla time taken to find maximal cliques: "+totalTime+ " milliseconds");
        } catch (FileNotFoundException e) {
            // print error

        } finally {

        }

    }

    // Finds neighbors of Nodes 
    ArrayList<Node> getNeighbors(Node v) {
        int i = v.getNode();
        if (userinput == 1) {
            return Graph.get(i - 1).getNeighbors();
        } else if (userinput == 0) {
            return Graph.get(i).getNeighbors();

        }
        return null;
    }

    // P ⋂ N(v), X ⋂ N(v) 
    ArrayList<Node> intersect(ArrayList<Node> listA,
            ArrayList<Node> listB) {
        ArrayList intersect = new ArrayList<>(listA);
        intersect.retainAll(listB);
        return intersect;

    }

    // R ⋃ {v} 
    ArrayList<Node> union(ArrayList<Node> listA,
            ArrayList<Node> listB) {
        ArrayList<Node> listUnion = new ArrayList<>(listA);
        listUnion.addAll(listB);
        return listUnion;
    }

    // P \ N(u) 
    ArrayList<Node> removeNeighbors(ArrayList<Node> listP, Node v) {
        ArrayList<Node> listRemoveNeighbors = new ArrayList<>(listP);
        listRemoveNeighbors.removeAll(v.getNeighbors());
        return listRemoveNeighbors;
    }

    void Bron_KerboschWithPivot(ArrayList<Node> R, ArrayList<Node> P,
            ArrayList<Node> X) {

        System.out.println("\t BronKerbosch(R: " + AppendString(R) + ", " + "P: " + AppendString(P) + ", " + "X: " + AppendString(X) + ")");

        // if P and X are both empty:  report R as a maximal clique
        if ((P.isEmpty()) && (X.isEmpty())) {
            System.out.println((" Maximal Clique: "));
            for (Node v : R) {
                System.out.print((" " + (v.getNode())));
            }

            System.out.println();
            return;
        }

        ArrayList<Node> P1 = new ArrayList<Node>(P);

        //choose a pivot vertex u in P ⋃ X
        Node u;
        u = getPivotVertex(union(P, X));

        System.out.println("\t Pivot vertex: " + (u.getNode()));

        //P \ N(u)
        P = removeNeighbors(P, u);

        // for each vertex v in P \ N(u):
        for (Node v : P) {
            {
                //R ⋃ {v}
                R.add(v);
                //BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
                Bron_KerboschWithPivot(R, intersect(P1, getNeighbors(v)), intersect(X, getNeighbors(v)));
                R.remove(v);
                //P := P \ {v}
                P1.remove(v);
                //X := X ⋃ {v}
                X.add(v);
            }
        }
    }

    void Bron_KerboschWithNOPivot(ArrayList<Node> R, ArrayList<Node> P,
            ArrayList<Node> X) {

        System.out.println("\t BronKerbosch(R: " + AppendString(R) + ", " + "P: " + AppendString(P) + ", " + "X: " + AppendString(X) + ")");

        // if P and X are both empty:  report R as a maximal clique
        if ((P.isEmpty()) && (X.isEmpty())) {
            System.out.println((" Maximal Clique: "));
            for (Node v : R) {
                System.out.print((" " + (v.getNode())));
            }

            System.out.println();
            return;
        }

        ArrayList<Node> P1 = new ArrayList<Node>(P);

        // for each vertex v in P \ N(u):
        for (Node v : P) {
            {
                //R ⋃ {v}
                R.add(v);
                //BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
                Bron_KerboschWithNOPivot(R, intersect(P1, getNeighbors(v)), intersect(X, getNeighbors(v)));
                R.remove(v);
                //P := P \ {v}
                P1.remove(v);
                //X := X ⋃ {v}
                X.add(v);
            }
        }
    }

    Node getPivotVertex(ArrayList<Node> N) {
        Collections.sort(N);
        return N.get(N.size() - 1);

    }

    String AppendString(ArrayList<Node> p) {
        StringBuilder strBuild = new StringBuilder();

        strBuild.append("{");
        p.forEach((v) -> {
            strBuild.append("").append(v.getNode()).append(",");
        });
        if (strBuild.length() != 1) {
            strBuild.setLength(strBuild.length() - 1);
        }
        strBuild.append("}");
        return strBuild.toString();
    }
}
