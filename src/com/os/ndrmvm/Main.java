package com.os.ndrmvm;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        RM rm = new RM();
        VM vm = new VM();

        Scanner scanner = new Scanner(System.in);
        String mode = null;
        String userMode = "user";
        String supervisorMode = "supervisor";
        String exit = "exit";
        String command = null;
        String runFullVM = "runVM";
        String loadVMCode = "loadVM";
        String printRMMemory = "RMMemory";
        String filename;
        String printVMMemory = "VMMemory";
        String printVMregisters = "VMregisters";
        String printRMregisters = "RMregisters";
        String step = "step";

        while (!exit.equalsIgnoreCase(mode)) {
            System.out.println("Pasirinkite režimą: user arba supervisor");
            mode = scanner.nextLine();
            if (userMode.equalsIgnoreCase(mode)) {
                while(!exit.equalsIgnoreCase(command)) {
                    System.out.println("Įveskite komandą: 1)loadVM -> 2)runVM");
                    command = scanner.nextLine();
                    if (loadVMCode.equalsIgnoreCase(command)) {
                        rm.addVM(vm);
                        System.out.println("Įveskite failo pavadinimą:");
                        filename = scanner.nextLine();
                        File file = new File(/*"C:\\Users\\tgr\\Desktop\\OSND\\" +*/ filename);
                        if (file.exists()){
                            rm.setMO((byte) 0);
                            rm.loadVMCode(filename);
                            System.out.println("Failas užkrautas!");
                            
                        }
                        else
                            System.out.println("Failas neegzistuoja!");
                    }
                    else if (runFullVM.equalsIgnoreCase(command)) {
                        System.out.println("VM pradėjo darbą!");
                        rm.runFullVM();
                    }
                    else if (!exit.equalsIgnoreCase(command)) {
                        System.out.println("Įvesta bloga komanda!");
                    }
                }
                System.out.println("Išeinama iš user režimo!");
                command = null;
                mode = null;
            }
            else if (supervisorMode.equalsIgnoreCase(mode)) {
                System.out.println("Įveskite komandą: 1)loadVM -> 2)runVM");
                System.out.println("Įveskite komandą: RMMemory -> VMMemory -> VMregisters -> RMregisters -> step");
                while(!exit.equalsIgnoreCase(command)) {
                    command = scanner.nextLine();

                    if (loadVMCode.equalsIgnoreCase(command)) {
                        rm.addVM(vm);
                        System.out.println("Įveskite failo pavadinimą:");
                        filename = scanner.nextLine();

                        File file = new File(/*"C:\\Users\\tgr\\Desktop\\OSND\\" +*/ filename);
                        if (file.exists()){
                            rm.setMO((byte) 1);
                            rm.loadVMCode(filename);
                            System.out.println("Failas užkrautas!");
                            
                            rm.printNextCommand();
                            
                        }
                        else
                            System.out.println("Failas neegzistuoja!");

                    }
                    else if (runFullVM.equalsIgnoreCase(command)) {
                        System.out.println("VM pradėjo darbą!");
                        rm.runFullVM();
                    }
                    else if (step.equalsIgnoreCase(command)) {
                        
                        rm.runSingleStepVM();
                        rm.printNextCommand();
                        rm.printRMregisters();
                        rm.printVMregisters();
                    }
                    else if (printRMMemory.equalsIgnoreCase(command)) {
                        rm.printRMMemory();
                        System.out.println("Atspausdinta RM atmintis");
                    }
                    else if (printVMMemory.equalsIgnoreCase(command)) {
                        rm.printVMMemory();
                        System.out.println("Atspausdinta VM atmintis");
                    }
                    else if (printVMregisters.equalsIgnoreCase(command)) {
                        rm.printVMregisters();
                        System.out.println("Atspausdinti VM registrai");
                    }
                    else if (printRMregisters.equalsIgnoreCase(command)) {
                        rm.printRMregisters();
                        System.out.println("Atspausdinti RM registrai");
                    }
                    else if (!exit.equalsIgnoreCase(command)) {
                        System.out.println("Įvesta bloga komanda!");
                    }
                }
                System.out.println("Išeinama iš supervisor režimo!");
                command = null;
                mode = null;
            }

            else if (!exit.equalsIgnoreCase(command)) {
                System.out.println("Įvesta bloga komanda!");
            }

        }

    }

}
