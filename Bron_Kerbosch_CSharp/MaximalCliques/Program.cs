using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MaximalCliques
{
    class MaximalCliques
    {
        //Define variables
        List<Node> UndirectedGraph = new List<Node>();  
        // <editor-fold defaultstate="collapsed" desc="Your Fold Comment">
         // </editor-fold>
        static void Main(string[] args)
        {
           
            //Empty List to store R,P and X
            List<Node> X = new List<Node>();
            List<Node> R = new List<Node>();
            List<Node> P = new List<Node>();
            int vertexCount;

            MaximalCliques maxcliques = new MaximalCliques();
            using (StreamReader strmReader = new StreamReader("C:\\data\\java\\test.txt"))
            {
                                
               try
                {
                    vertexCount = Convert.ToInt32(strmReader.ReadLine());
                    int edgesCount = Convert.ToInt32(strmReader.ReadLine());
                    
                 //Store the vertices in to graph
                   for (int i = 1; i <= vertexCount; i++)
                    {
                        Node N = new Node();
                        N.setNode(i);
                        maxcliques.UndirectedGraph.Add(N);
                    }
                   
                   //Compare neighbors and addt hem if one doesnt exist
                   for (int i = 1; i <= edgesCount; i++)
                    {
                        String[] strNeighbors = strmReader.ReadLine().Split(' ');                       
                        Node Node1 = maxcliques.UndirectedGraph[(Convert.ToInt32(strNeighbors[0])) - 1];
                        Node Node2 = maxcliques.UndirectedGraph[(Convert.ToInt32(strNeighbors[1])) - 1];
                        Node1.addNeighbors(Node2);

                    }
                }
                catch (Exception e)
                {
                    e.StackTrace.ToString();

                }
            }
             
            //Maximal Cliques with no pivot vertex   
            maxcliques.BronKerboschWithNoPivot(R, maxcliques.UndirectedGraph, X);

            //Maximal Cliques with no pivot vertex
            maxcliques.BronKerboschWithPivot(R, maxcliques.UndirectedGraph, X);
            Console.ReadKey(); 
        }


        // Finds neighbors of Nodes 
        List<Node> getNeighbors(Node v)
        {           
            return UndirectedGraph[v.getNode() - 1].getNeighbors();
        }

        // P ⋂ N(v), X ⋂ N(v) 
        List<Node> intersect(List<Node> listA, List<Node> listB)
        {
             return listA.Intersect(listB).ToList();
        }

        
        // P \ N(u) 
        List<Node> removeNeighbors(List<Node> listP, Node v)
        {
            List<Node> listRemove = new List<Node>(listP);
            listRemove = listRemove.Except(v.getNeighbors()).ToList();
            return listRemove;
        }

        
        void BronKerboschWithPivot(List<Node> R, List<Node> P,
                List<Node> X)
        {

            Console.WriteLine("\t BronKerbusch(R: " + getVertexSet(R) + ", " + "P: " + getVertexSet(P) + ", "
                    + "X: " + getVertexSet(X) + ")");
          
            // if P and X are both empty:  report R as a maximal clique
            if ((P.Count == 0) && (X.Count == 0))
            {
              
                Console.WriteLine((" Maximal Clique: "));
                foreach (var v in R)
                {
                    Console.Write((" " + (v.getNode())));
                }
                Console.WriteLine();
              
                return;
            }
            
            List<Node> P1 = new List<Node>(P);

            // P ⋃ X 
            List<Node> unionNode = new List<Node>(P);
            unionNode.AddRange(X);
            
            
            //choose a pivot vertex u in P ⋃ X
            unionNode.Sort();
            Node u = unionNode[(unionNode.Count - 1)];
           
            Console.WriteLine("Pivot vertex: " + (u.getNode()));

            //P \ N(u)
            P = P.Except(u.getNeighbors()).ToList();

            // for each vertex v in P \ N(u):
            foreach (var v in P)
            {
                //R ⋃ {v}
                R.Add(v);                
              
                //BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
                BronKerboschWithPivot(R, intersect(P1, getNeighbors(v)),
                        intersect(X, getNeighbors(v)));
               
                //Empty R for next iteration
                R.Remove(v);
               
                //P := P \ {v}                
                P1.Remove(v);
                 
                //X := X ⋃ {v}                
                X.Add(v);
            }
        }

         //Bron Kerbusch without pivot
        void BronKerboschWithNoPivot(List<Node> R, List<Node> P,
                List<Node> X) { 

           Console.WriteLine("\t BronKerbusch(R: " + getVertexSet(R) + ", " + "P: " + getVertexSet(P) + ", "
                              + "X: " + getVertexSet(X) + ")");

                    // if P and X are both empty:  report R as a maximal clique
                    if ((P.Count == 0) && (X.Count == 0))
                    {

                        Console.WriteLine((" Maximal Clique: "));
                        foreach (var v in R)
                        {
                            Console.Write((" " + (v.getNode())));
                        }
                        Console.WriteLine();

                        return;
                    }

                    List<Node> P1 = new List<Node>(P);                   
                    foreach (var v in P)
                    {
                        //R ⋃ {v}
                        R.Add(v);
                        //BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
                        BronKerboschWithNoPivot(R, intersect(P1, getNeighbors(v)),
                                intersect(X, getNeighbors(v)));
                        //Empty R for next iteration
                        R.Remove(v);
                        //P := P \ {v}
                        P1.Remove(v);
                        //X := X ⋃ {v}
                        X.Add(v);
        } 
    } 

        


        String getVertexSet(List<Node> P)
        {
            StringBuilder strBuild = new StringBuilder();

            strBuild.Append("{");
            foreach (var v in P)
            {
                strBuild.Append("" + (v.getNode()) + ",");
            }
            if (strBuild.Length != 1)
            {
                strBuild.Length = (strBuild.Length - 1);
            }
            strBuild.Append("}");
            return strBuild.ToString();
        }
    }

    class Node : IComparable<Node>
    {
        int node;
        int degree;
        List<Node> neighbors = new List<Node>();

        public int getNode()
        {
            return node;
        }

        public void setNode(int node)
        {
            this.node = node;
        }

        public int getDegree()
        {
            return degree;
        }

        public void setDegree(int degree)
        {
            this.degree = degree;
        }

        public List<Node> getNeighbors()
        {
            return neighbors;
        }

        public void addNeighbors(Node n)
        {
            neighbors.Add(n);
            if (!n.getNeighbors().Contains(n))
            {
                n.getNeighbors().Add(this);
                n.degree++;
            }
            this.degree++;

        }

        public void removeNeighbors(Node n)
        {
            this.neighbors.Remove(n);
            if (n.getNeighbors().Contains(n))
            {
                n.getNeighbors().Remove(this);
                n.degree--;
            }
            this.degree--;

        }

        //Override Icompare
        public int CompareTo(Node v)
        {
            if (this.degree < v.degree)
            {
                return -1;
            }
            if (this.degree > v.degree)
            {
                return 1;
            }
            return 0;
        }


    } 
}
