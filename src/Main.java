import java.util.ArrayList;
import java.util.Scanner;
/*
// allocation matrix
0 1 0
2 0 0
3 0 2
2 1 1
0 0 2

// max matrix
7 5 3
3 2 2
9 0 2
2 2 2
4 3 3

// available array
3 3 2
 */
public class Main {
    static int[] available; //the available amount of each resource
    static int[] backupAvailable;// this is used to store the initial state of the available array
    static int[][] maximum; //the maximum demand of each process
    static int[][] allocation;
    static int[][] need; //the amount currently allocated to each process
    static boolean[] taken;
    static int procCnt , resCnt;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args){
        System.out.println("Enter the number of processes");
        procCnt = scanner.nextInt();
        System.out.println("Enter the number of resources");
        resCnt = scanner.nextInt();

        allocation = new int[procCnt][resCnt];
        maximum = new int[procCnt][resCnt];
        need = new int[procCnt][resCnt];
        available = new int[resCnt];

        System.out.println("Enter the allocation matrix");
        for(int i=0 ; i<procCnt ; i++){
            for(int j=0 ; j<resCnt ; j++){
                allocation[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Enter the maximum matrix");
        for(int i=0 ; i<procCnt ; i++){
            for(int j=0 ; j<resCnt ; j++){
                maximum[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Enter the available array");
        for(int i=0 ; i<resCnt ; i++){
            available[i] = scanner.nextInt();
        }
        runBankerAlgo();

        while(true) {
            System.out.println("Enter:RQ <process#> <r1> <r2> <r3>");
            System.out.println("or:Exit");
            String comand = scanner.next();
            if(comand.equals("RQ")){
                int processId;
                while(true) {
                    processId = scanner.nextInt();
                    if (processId >= procCnt) {
                        System.out.println("wrong processId");
                    }else{
                        break;
                    }
                }
                for(int i=0 ; i<resCnt ; i++){
                    allocation[processId][i]+=scanner.nextInt();
                }
                runBankerAlgo();
            }else{
                break;
            }
        }
    }
    private static void runBankerAlgo(){
        System.out.println("the need matrix is");
        for(int i=0 ; i<procCnt ; i++){
            for(int j=0 ; j<resCnt ; j++){
                need[i][j] = maximum[i][j] - allocation[i][j];
                System.out.print(need[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
        // backup the available array first
        backupAvailable = new int[resCnt];
        copyArray(backupAvailable,available);
        // taken is like a visited array to indicate that this process has already
        // been executed
        taken = new boolean[procCnt];
        ArrayList<Integer> resultSeq = new ArrayList<>();
        boolean foundSafeSequence=true;
        for(int i=0 ; i<procCnt ; i++){
            boolean foundSomeGoodProcess=false;
            for(int j = 0 ; j<procCnt ; j++){
                if(taken[j])continue;
                if(checkForExecution(j)){
                    resultSeq.add(j);
                    taken[j]=true;
                    System.out.print("process "+j+" is executed and now the available array is ");
                    for(int k=0 ; k<resCnt;k++ ){
                        available[k]+=allocation[j][k];
                        System.out.print(available[k]+" ");
                    }
                    System.out.println();
                    foundSomeGoodProcess=true;
                    break;
                }
            }
            if(!foundSomeGoodProcess){
                foundSafeSequence=false;
            }
        }
        if(!foundSafeSequence){
            copyArray(available,backupAvailable);
            System.out.println("there is no safe sequence");
            return;
        }
        System.out.print("the sequence is ");
        for(int x:resultSeq){
            System.out.print(x+" ");
        }
        System.out.println();
        // restoring the initial available array for upcoming requests
        copyArray(available,backupAvailable);
    }
    private static void copyArray(int[] arr1,int[] arr2){
        for(int i=0 ; i<arr1.length ; i++){
            arr1[i]=arr2[i];
        }
    }
    // this method takes a process id to decide whether it can be executed or not
    private static boolean checkForExecution(int processId){
        boolean ok=true;
        for(int i=0 ; i<resCnt ; i++){
            if(available[i]<need[processId][i]){
                ok=false;
                break;
            }
        }
        return ok;
    }
}
