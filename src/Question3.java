import mpi.* ;

public class Question3 {
    public static void main(String[] args) {
        MPI.Init(args);

        int size = MPI.COMM_WORLD.getSize();
        //validate the presence of two processes;
        if(size != 2) throw new IllegalArgumentException("Number of processes have to be 2");

        

        MPI.Finalize();
    }
}
