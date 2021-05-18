package uk.ac.bradford;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Question4 {


    public static void main(String[] args) {
        Log.printLine("Starting Cloud Simulation");

        try {
            int num_user = 4;
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            Datacenter bradfordDatacenter = createDatacenter("Bradford_Datacenter");

            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            ArrayList<Vm> vmlist = createVms(brokerId);

            broker.submitVmList(vmlist);

            ArrayList<Cloudlet> cloudletList = createCloudlets(brokerId);

            // submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);

            // Sixth step: Starts the simulation
            CloudSim.startSimulation();

            CloudSim.stopSimulation();

            //Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);

            Log.printLine("Finished execution");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Cloudlet> createCloudlets(int brokerId){

        ArrayList<Cloudlet> cloudletList = new ArrayList<>();

        long length = 400000;
        long fileSize = 300;
        long outputSize = 300;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        for (int i = 0; i < 4; i++) {
            Cloudlet cloudlet = new Cloudlet(i, length, 2, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(i);

            // add the cloudlet to the list
            cloudletList.add(cloudlet);
        }

        return cloudletList;
    }
    /**
     * Creates the vms to be hosted on host machines
     * Each vm will had 512mb ram with processor count of 0 and 1000 mips
     * The power is set to 1000 with vm name for each vm being amongst the lines
     * of vm-<i>
     * @param brokerId
     * @return List of vms
     */
    private static ArrayList<Vm> createVms(int brokerId){
        ArrayList<Vm> vmlist = new ArrayList<Vm>();

        int mips = 1000;
        long size = 10000;
        int ram = 512;
        long bw = 1000;
        int pesNumber = 2;
        String vmm = "Xen";

        for (int i = 0; i < 4; i++) {
            Vm vm = new Vm(i, brokerId, mips, pesNumber, ram, bw, size, vmm + "-" + i, new CloudletSchedulerTimeShared());
            vmlist.add(vm);
        }

        return vmlist;
    }
    private static List<Pe> createProcessors(){
        List<Pe> peList = new ArrayList<Pe>();

        int mips = 1000;

        //define 8 processors for a single host machine
        for (int i = 0; i < 8; i++) {
            peList.add(new Pe(i, new PeProvisionerSimple(mips)));
        }

        return peList;

    }

    private static List<Host> createHostMachines(){
        List<Host> hostList = new ArrayList<Host>();

        List<Pe> peList = createProcessors();

        int hostId = 0;
        int ram = 8192;
        long storage = 1000000;
        int bw = 10000;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList,
                        new VmSchedulerTimeShared(peList)
                )
        );
        return hostList;
    }

    private static DatacenterCharacteristics defineDataCenterCharacteristics(List<Host> hostList){
        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        return characteristics;
    }
    private static Datacenter createDatacenter(String name) {


        List<Host> hostList = createHostMachines();

        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, defineDataCenterCharacteristics(hostList), new VmAllocationPolicySimple(hostList), new LinkedList<Storage>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }
    /**
     * Datacenter Brokers helps connect users to the cloud
     * by allocating a cloudlets to VMs. This helps to make sure all
     * the running systems are kept stable with an approriate balance.
     * @return
     */
    private static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + "Time" + indent
                + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId()
                        + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent
                        + dft.format(cloudlet.getActualCPUTime()) + indent
                        + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }
    }
}
