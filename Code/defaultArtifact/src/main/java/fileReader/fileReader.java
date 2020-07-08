package fileReader;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class fileReader {
    protected File file;
    protected Scanner scn;
    protected Logger log;

    public fileReader(File file) {
        this.file = file;
        log = Logger.getInstance();
        scn = null;
    }

    public Graph readFromFile() {
        String nameF = file.getName();
        if(!nameF.matches(".+\\.txt$")){
            log.logEvent(log.prepare("Read file error: wrong file extension"));
            return null;
        }
        try{
            scn = new Scanner(file);
        }catch (IOException e){
            //mes for log
            log.logEvent(log.prepare("Error open file! "+e.getMessage()));
            return null;
        }
        Graph gr = new Graph();
        Map<Character, Vertex> allVertex = new HashMap<>();

        Character verStart;
        Character verEnd;

        Vertex tmpStart = new Vertex();
        Vertex tmpEnd = new Vertex();
        int weight = 0;
        int countEdge = 0;

        String patternForVertex = "[a-zA-Z]";

        if(scn.hasNextInt()){
            countEdge = scn.nextInt();
        }else{
            log.logEvent(log.prepare("\nRead file error: wrong count of edge (not a number)"));
            return null;
        }
        if(scn.hasNext(patternForVertex)){
            verStart = scn.next().charAt(0);
            tmpStart = new Vertex(verStart.toString());
            tmpStart.isStart = true;
            allVertex.put(verStart, tmpStart);
            gr.addVertex(tmpStart);
            log.logEvent(log.prepare("Start vertex for algorithm: "+verStart));

        }
        else{
            log.logEvent(log.prepare("Read file error: wrong start vertex name (not [a-z])"));
            return null;
        }


        for(int i = 0; i<countEdge; i++){
            //reading start v
            if(scn.hasNext(patternForVertex)){
                verStart =  scn.next().charAt(0);
            }else{
                log.logEvent(log.prepare("Read file error: read edge - wrong start vertex name (not [a-z]) in line №"+(i+3)));
                return null;
            }
            //reading end v
            if(scn.hasNext(patternForVertex)){
                verEnd = scn.next().charAt(0);
            }
            else{
                log.logEvent(log.prepare("Read file error:  read edge - wrong end vertex name (not [a-z]) in line №"+(i+3)));
                return null;
            }
            //reading weight
            if(scn.hasNextInt()){
                weight = scn.nextInt();
            }
            else{
                log.logEvent(log.prepare("Read file error:  read edge - wrong  weight (not a number) in line №"+(i+3)));
                return null;
            }
            //checking for next line
            if(!scn.hasNextLine()){
                if((i+1) < countEdge){
                    log.logEvent(log.prepare("Read file error: not enough data"));
                    return null;
                }
            }else{
                //check for junk in end line
                if(scn.nextLine().length() != 0){
                    log.logEvent(log.prepare("Read file error:  read edge - Wrong end line №"+(i+3)));
                    return null;
                }
            }
            //end reading 1 line. Add to graph process:
            if(allVertex.containsKey(verStart)){
                tmpStart = allVertex.get(verStart);
            }else{
                tmpStart = new Vertex(verStart.toString());
                allVertex.put(verStart, tmpStart);
                gr.addVertex(tmpStart);
            }

            if(allVertex.containsKey(verEnd)){
                tmpEnd = allVertex.get(verEnd);
            }else{
                tmpEnd = new Vertex(verEnd.toString());
                allVertex.put(verEnd, tmpEnd);
                gr.addVertex(tmpEnd);
            }
            gr.addEdge(new Edge(weight, tmpStart, tmpEnd));
            log.logEvent(log.prepare("Add new edge:\n"+verStart+" "+verEnd+" "+weight+"\n"));
        }

        if(scn.hasNextLine()){
            log.logEvent(log.prepare("Warning read file: the number of edges is less than the data in the file, only the right amount is read \n"));
        }

        scn.close();
        return gr;
    }

}
