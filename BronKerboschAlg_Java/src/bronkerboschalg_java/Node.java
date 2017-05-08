/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bronkerboschalg_java;

import java.util.ArrayList;

/**
 *
 * @author SWATHI YANDAPALLY
 */
public class Node implements Comparable<Node>{
    int node; 
        int degree; 
        ArrayList<Node> neighbors;

    Node() {
        this.neighbors = new ArrayList<Node>();
    }

        public int getNode()
        {
            return node;
        }

        public void setNode(int node)
        {
            this.node = node;
        } 

        public int getDegree() {
            return degree; 
        } 

        public void setDegree(int degree) {
            this.degree = degree; 
        } 

        public ArrayList<Node> getNeighbors() {
            return neighbors; 
        } 
      
         
        public void addNeighbors(Node n)
        {
           this.neighbors.add(n); 
        if (!n.getNeighbors().contains(n)) {     
           n.getNeighbors().add(this); 
                n.degree++; 
                       }
            this.degree++; 

        } 

        public void removeNbr(Node n) {
            this.neighbors.remove(n); 
            if (n.getNeighbors().contains(n)) { 
                n.getNeighbors().remove(this); 
                n.degree--; 
            } 
            this.degree--; 
        }

    @Override
        public int compareTo(Node v)
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
