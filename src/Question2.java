import mpi.* ;

public class Question2 {
    /**
     * Solution adapted from lecture slides and from the following resouce:
     * https://www.mcs.anl.gov/research/projects/mpi/tutorial/mpiexmpl/src/pi/C/main.html
     * @param args
     * @throws MPIException
     */
    public static void main(String[] args) throws MPIException{
        MPI.Init(args);

        
        int r = MPI.COMM_WORLD.getRank(), s = MPI.COMM_WORLD.getSize(), n = 100;
        
        double h = 1.0 / (double) n, portionSum = 0.0;
         
        
        for (int i = r + 1; i <= n; i += s) {
            double x = h * ((double) i - 0.5);
            portionSum += (4.0 / (1.0 + x * x));
        }

        double inputBuffer[] = {h * portionSum}, outputBuffer[] = new double[1];

        MPI.COMM_WORLD.reduce(inputBuffer, outputBuffer, 1, MPI.DOUBLE, MPI.SUM, 0);

        //if root process
        if(r == 0) {
            System.out.println("The value of PI is: " + outputBuffer[0]);
        }

        MPI.Finalize();
    }
}
