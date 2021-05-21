import mpi.* ;

public class Question3 {
    public static void main(String[] args) throws MPIException{
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        //validate the presence of two processes;
        if(size != 2) throw new IllegalArgumentException("Number of processes have to be 2");

        int[][] arr = new int[4][4];

        int sum = 0; 
        if(rank == 1){
            //first two rows for the first process
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr.length; j++) {
                    arr[i][j] = rank;
                }
            }

            sum += rank * 4;
        }

        if(rank == 2){
            //last two arrays for the second process
            for (int i = 2; i < arr.length; i++) {
                for (int j = 0; j < arr.length; j++) {
                    arr[i][j] = rank;
                }
            }

            sum += rank * 4;
        }

        if (rank == 1)
        {
            MPI.COMM_WORLD.send(sum, 1, MPI.INT, 0, 0);
            System.err.println("Sending sum " + sum);
        }
        else
        {
            int[] res = new int[1];
            MPI.COMM_WORLD.recv(sum, 1, MPI.INT, 1, 0);
            System.out.println("Recieved " + res[0]);

        MPI.Finalize();
    }
}
