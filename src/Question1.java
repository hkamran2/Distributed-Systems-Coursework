import java.util.Scanner;

import mpi.* ;

public class Question1 {
    public static void main(String[] args) throws MPIException{
        MPI.Init(args) ;

        int rank = MPI.COMM_WORLD.getRank() ;
        int[] buffer = new int[1];

        if(rank == 0){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the integer:");
            buffer[0] = scanner.nextInt();
        }

        MPI.COMM_WORLD.bcast(buffer, 1, MPI.INT, 0 );
        
        System.out.println("Rank : " + rank  + " Value : " + buffer[0]);
        
        MPI.Finalize();
    }
}
