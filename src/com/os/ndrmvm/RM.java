package com.os.ndrmvm;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RM {

    private static int PLR;
    private static byte PI;
    private static byte SI;
   // private static byte IOI;
    private static byte TI;
    private static byte MO;
    private static short IC;
    private static short SP;
    private static byte PID;
    private static byte CHB1, CHB2, CHB3;
      

    //realioj masinoj susimapinti

    public static Memory memory;
    
    public static String[][] supervisorMemory;
    private static int[] supervisorMemoryOffset;
    
    public static int[] supervisorStack;
    //public static int supervisorStackPoint;

    public static HDD hdd;
    public static Printer printer;
    public static FlashDrive flashDrive;

    private static Vector<VM> VMarray;
    
    
    static{
        memory = new Memory();
        hdd = new HDD();
        printer = new Printer();
        supervisorMemory = new String[8][];
        supervisorStack = new int[8];
        flashDrive = new FlashDrive();
        VMarray = new Vector<VM>();
    }

    public RM() {
        
        
        
        
       /* char[][] stackMem = new char[memory.BLOCK_SIZE][memory.WORD_SIZE];
        
        for(int i = 0; i<memory.BLOCK_SIZE; i++){
            for(int j = 0; j < memory.WORD_SIZE; j++){
                stackMem[i][j] = '*';
            }
            
        }
        
        memory.writeBlock(stackMem, 63);*/
        //SP = (63*16)-1;
        PID = -1;
        
        TI = 0;
        
        MO = 1;
        
        PI = 0;
        SI = 0;
//        IOI = 0;
        IC = 0;
        CHB1 = 0;
        CHB2 = 0;
        CHB3 = 0;

        supervisorMemoryOffset = new int[4];
       // firstComm = true;
        
    }

    public static boolean addVM(  VM vm ){
        
        boolean vmMemoryGen = false;
        vmMemoryGen = allocateMemoryVM();
        
        
        if(vmMemoryGen){
            VMarray.addElement(vm);
            PID++;
            
            IC = VMarray.get(PID).getIC();
            SP = VMarray.get(PID).getSP();
            
            //SP++;
            //putToRMStack(get4BytesFromDat(String.valueOf(PLR)));
            supervisorStack[PID] = PLR;
            
            return true;
        }
        
        
        
        return false;
    }
    
    public void runFullVM(){
       
        
        while(true){
            if(VMarray.get(PID).getEndWorkStatus() == false){
                //VMarray.get(PID).fullRun();
                
                
                
                VMarray.get(PID).doNext();
                IC = VMarray.get(PID).getIC();
                SP = VMarray.get(PID).getSP();
                
                
                if(MO == 1){
                    printNextCommand();
                }
             
            }
            else{
                if(checkVMWork() == false){
                    break;
                }
            }
            timerMech();
            
        }
    }
    
    public boolean runSingleStepVM(){
        if(VMarray.get(PID).getEndWorkStatus() == false){
                //VMarray.get(PID).fullRun();
               /* if(MO == 1){
                    printNextCommand();
                }*/
                //IC = VMarray.get(PID).getIC();
               
                VMarray.get(PID).doNext();
                IC = VMarray.get(PID).getIC();
                SP = VMarray.get(PID).getSP();
                
            }
            else{
                if(checkVMWork() == false){
                    return false;
                }
            }
            timerMech();
            
        return true;
    }
    
    public void printVMMemory( ){
        
        //int stackStart = 63*16;
        int allPlr;
        //int posPlr = 0;
        for(int i = 0; i < VMarray.size(); i++){
            //stackStart += i;
            System.out.println("VM: " + i);
            allPlr = makeInt(memory.getWord(63, i));
            for(int j = 0; j < 16; j++){
                
                if(j > 9){
                    System.out.print(j + ") ");
                }
                else{
                    System.out.print("0" + j + ") ");
                }
                
                
                for(int k = 0; k < 16; k++){
                    //System.out.println("TEST " +  makeInt(memory.getWord(63, i)));
                    System.out.print(String.valueOf(memory.getWord( makeInt(memory.getWord(allPlr, j)) , k)) + " ");   
                }
                 System.out.println();
            }
            
           System.out.println();
            
        }
        
         
        
    }
    
    public void printVMregisters(){
        
        for(int i = 0; i < VMarray.size(); i++){
            System.out.println("VM: "+ i + " IC:" +VMarray.get(i).getIC()+ " SP:" + VMarray.get(i).getSP() + " PID:" + VMarray.get(i).getPID());
        }
       // System.out.println();
    }
    
    
    
    public void timerMech(){
        if(TI >= 2){
            TI = 0;
            changeRunningVM();
        }
        else{
            TI++;
        }
    }
    
    public void changeRunningVM(){
        if(PID == (VMarray.size()-1)){
            //SP -= PID;
            //PLR = makeInt(memory.getWord(SP/16, SP%16));
            PLR = supervisorStack[0];
            PID = 0;
            IC = VMarray.get(PID).getIC();
            SP = VMarray.get(PID).getSP();
            
            return;
        }
        else if(PID < (VMarray.size()-1)){
            //SP++;
            //PLR = makeInt(memory.getWord(SP/16, SP%16));
            PID++;
            PLR = supervisorStack[PID];
            IC = VMarray.get(PID).getIC();
            SP = VMarray.get(PID).getSP();
        }
    }
    
    public boolean checkVMWork(){
        int amm = 0;
        for(int i = 0; i < VMarray.size(); i++){
            if(VMarray.get(i).getEndWorkStatus() == true ){
                amm++;
            }
        }
        
        if(amm == VMarray.size()){
            return false;
        }
        else{
            changeRunningVM();
            return true;
        }
        
    }
    
    public static void setSI(byte si){
        
        switch (si) {
            case 1:
                if(MO == 1){
                    System.out.println("SI:1 VM iskviete Rasymo interupt'a");
                }
                SI = si;
                break;
            case 2:
                if(MO == 1){
                    System.out.println("SI:2 VM iskviete Force Stop interupt'a");
                }
                SI = si;
                break;
            case 3:
                if(MO == 1){
                    System.out.println("SI:3 VM iskviete Salygini Stop interupt'a");
                }
                SI = si;
                break;
            case 4:
                if(MO == 1){
                    System.out.println("SI:4 VM iskviete FORK interupt'a");
                }
                SI = si;
            default:
                break;
        }
        
        
    }
    
    public static void setPI(byte pi){
        
        switch (pi) {
            case 1:
                //if(MO == 0){
                    System.out.println("PI:1 Blogas adresas, baigiamas darbas");
                //}
                PI = pi;
                break;
            case 2:
                //if(MO == 0){
                    System.out.println("PI:2 Sintakses klaida, baigiamas darbas");
                //}
                PI = pi;
                break;
            case 3:
                //if(MO == 0){
                    System.out.println("PI:3 Blogas operacijos kodas, baigiamas darbas");
                //}
                PI = pi;
                break;
            case 4:
                //if(MO == 0){
                    System.out.println("PI:4 Perpildymas, baigiamas darbas");
                //}
                PI = pi;
             case 5:
                //if(MO == 0){
                    System.out.println("PI:5 Dalyba is nulio, baigiamas darbas");
                //}
                PI = pi;
            case 6:
                //if(MO == 0){
                    System.out.println("PI:5 Isejimas is segmento ribu, baigiamas darbas");
                //}
                PI = pi;    
            default:
                break;
        }
        
        
    }
    
    public static void setCHB1( byte chb1 ){
        
         switch (chb1) {
            case 0:
                if(MO == 1){
                    System.out.println("CHB1:0 Skaitymo is flash kanalas atblokuotas");
                }
                CHB1 = chb1;
                break;
            case 1:
                if(MO == 1){
                    System.out.println("CHB1:1 Skaitymo is flash kanalas uzblokuotas");
                }
                CHB1 = chb1;
                break;
            default:
                break;
        }
        
        
    }
    
     public static void setCHB2( byte chb2 ){
        
         switch (chb2) {
            case 0:
                if(MO == 1){
                    System.out.println("CHB2:0 Rasymo i spausdintuva kanalas atblokuotas");
                }
                CHB2 = chb2;
                break;
            case 1:
                if(MO == 1){
                    System.out.println("CHB2:1 Rasymo i spausdintuva kanalas uzblokuotas");
                }
                CHB2 = chb2;
                break;
            default:
                break;
        }
        
        
    }
     
     public static void setCHB3( byte chb3 ){
        
         switch (chb3) {
            case 0:
                if(MO == 1){
                    System.out.println("CHB3:0 HDD kanalas atblokuotas");
                }
                CHB3 = chb3;
                break;
            case 1:
                if(MO == 1){
                    System.out.println("CHB3:1 HDD kanalas uzblokuotas");
                }
                CHB3 = chb3;
                break;
            default:
                break;
        }
        
        
    } 
    
    private static boolean allocateMemoryVM(){
        
        if(memory.checkAvailableSpace() == false ){
            return false;
            //!!!!!!!!!!!!!!! i spauzdintuva kad neustenka atminties? nzn ka daryt jei nera pakankamai atminties naujam VM
        }
        
        
        //Nagalima bloku VM'ui duoti nuosekliai turi buti random
        Random rand = new Random(); 
        
        int rndBlock = 0;
        
        char[][] allocBlock = new char[memory.BLOCK_SIZE][memory.WORD_SIZE];
        
        char[][] pageBlock = new char[memory.BLOCK_SIZE][memory.WORD_SIZE];
        char[] posInPage;//= new char[memory.BLOCK_SIZE];
        
       // posInPage[0] = '0';
       // posInPage[1] = '0';
       // posInPage[2] = '0';
       // posInPage[3] = '0';
        
        int pageIter = 0;
        
        
         for(int i = 0; i<memory.BLOCK_SIZE; i++){
            for(int j = 0; j < memory.WORD_SIZE; j++){
                allocBlock[i][j] = '~';
                pageBlock[i][j] = '~';
            }
            
        }
        
        
        for(int i = 0; i < memory.BLOCK_COUNT; i++){
            rndBlock = rand.nextInt(memory.BLOCK_COUNT-1);
            if(memory.isBlockEmpty(rndBlock)){
                PLR = rndBlock;
                memory.writeBlock(pageBlock, PLR);
                break;
            }
            
        }
        
        
        for(int i = 0; i < memory.BLOCK_COUNT; i++){
            rndBlock = rand.nextInt(memory.BLOCK_COUNT-1);
            if(memory.isBlockEmpty(rndBlock)){
               
                posInPage = new char[memory.BLOCK_SIZE];
                posInPage[0] = '0';
                posInPage[1] = '0';
                posInPage[2] = '0';
                
                if(rndBlock < 10){
                    //System.out.println((char)(rndBlock+48));
                    posInPage[3] = (char)(rndBlock + 48);
                }
                else{
                    //System.out.println((char)(rndBlock/10 + 48));
                    posInPage[2] = (char)(rndBlock/10 + 48);
                    posInPage[3] = (char)(rndBlock%10 + 48);
                }
                
                
                //System.out.println(String.valueOf(posInPage));
                memory.writeWord(posInPage,  PLR, pageIter);
                
                pageIter++;
                
                memory.writeBlock(allocBlock, rndBlock);
                
                if(pageIter == 16){
                    break;
                }
                
            }
            
        }
        
        
        
        return true;
        
    }
    
    
    public void printRMMemory(){
        //char[] buff;
        for(int i = 0; i < 64; i++){
            if(i > 9){
                System.out.print(i + ") ");
            }
            else{
                System.out.print("0" + i + " ");
            }
            
            
            for(int j = 0; j < 16; j++ ){
                //buff = memory.getWord(i, j);
               /* for(int k = 0; k < 4; k++){

                    System.out.print(buff[k]);
                }*/
                
                System.out.print(String.valueOf(memory.getWord(i, j)));
                System.out.print(" ");
            }
            System.out.println();
        }
        
        
        System.out.println();
        
    }
    
     public void printRMregisters(){
        
        
         System.out.println("RM: PLR:" + PLR + " PI:" + PI + " SI:" + SI + " TI:" + TI + " MO:" + MO + " IC:"+ IC +" SP:" + SP + " PID:" + PID + " CHB1:" + CHB1 + " CHB2:" + CHB2 + " CHB3:" + CHB3);
         
        // System.out.println();
    }
    
     
    public void loadFlash(String fileName){
        
        setCHB1((byte)1);
        try {
            flashDrive.loadProgram(fileName);
        } catch (IOException ex) {
            Logger.getLogger(RM.class.getName()).log(Level.SEVERE, null, ex);
        }
        supervisorMemory[0] = flashDrive.getReadMem();
        setCHB1((byte)0);
        
        
        for(int i = 0; i < supervisorMemory[0].length; i++ ){
            if(supervisorMemory[0][i].equals("#PRO")){
                supervisorMemoryOffset[0] = i;
                //System.out.println("offset " + i);
                break;
            }
            
            // System.out.println(supervisorMemory[0][i]);
        }
        
        
        
        
    }
    
    public static void printerPrint( String data ){
        setCHB2((byte)1);
        printer.print(data);
        setCHB2((byte)0);
    }
    
    
    public void loadToHDD(){
        
        setCHB3((byte)1);
        hdd.readToHdd(supervisorMemory[0]);
        setCHB3((byte)0);
    }
    
    public void loadVMCode(String fileName){
        
        //hdd.readToHdd(fileName);
        
        loadFlash(fileName);
        
        loadToHDD();
        
        char[][][] bufferMem = new char[16][memory.BLOCK_SIZE][memory.WORD_SIZE];
        bufferMem = hdd.checkCode();
        
        if(bufferMem != null){
        //Transfer VM Data Segment
            for(int i = 0; i < 7; i++ ){
                memory.replaceBlock(bufferMem[i], makeInt(memory.getWord(PLR, i)));
                //System.out.println(makeInt(memory.getWord(PLR, i)));
            }

             //Transfer VM Code Segment
            for(int i = 7; i < 16; i++ ){
                memory.replaceBlock(bufferMem[i], makeInt(memory.getWord(PLR, i)));
                //System.out.println(makeInt(memory.getWord(PLR, i)));
            }
        
        }
       // memory = hard.checkCode();
        
    }
 
    public static char[] getWordVMQuery(int x1, int x2){
        
        return memory.getWord(makeInt(memory.getWord(PLR, x1)), x2);
        
    }
    
    
    public static void putWordVMQuery(char[] word, int x1, int x2){
        
        //return memory.getWord(makeInt(memory.getWord(PLR, x1)), x2);
        memory.writeWord(word, makeInt(memory.getWord(PLR, x1)), x2);
    }
     
    public static void forkWMquery(short ic, short sp, byte pid){
        VM forkVM = new VM(ic,sp,pid);
        addVM(forkVM);
        copyVM((byte) (pid-1));
        supervisorMemory[pid] = supervisorMemory[pid-1];
        supervisorMemoryOffset[pid] = supervisorMemoryOffset[pid-1];
    }
    
    public void printNextCommand(){
        
        if(((VMarray.get(PID).getIC() + 1)-112 + supervisorMemoryOffset[PID]) < supervisorMemory[PID].length ){
            System.out.println("VM: "+ PID + " Sekanti komanda: " + supervisorMemory[PID][(VMarray.get(PID).getIC() + 1)-112 + supervisorMemoryOffset[PID]]);
            
            if(supervisorMemory[PID][(VMarray.get(PID).getIC() + 1)-112 + supervisorMemoryOffset[PID]].substring(0, 2).equals("PN")){
                supervisorMemoryOffset[PID]--;           
            }
            
        } 
        
    }
    
    private static void copyVM(byte pid){
        
        int prePLR = supervisorStack[pid]; //makeInt(memory.getWord((SP-1)/16, (SP-1)%16));
        
       // char[][][] buffer = new char [16][memory.BLOCK_SIZE][memory.WORD_SIZE];
               
        for(int i = 0; i < memory.BLOCK_SIZE; i++ ){
            memory.replaceBlock(memory.getBlock(makeInt(memory.getWord(prePLR, i))), makeInt(memory.getWord(PLR, i)));
            //buffer[i] = memory.getBlock(makeInt(memory.getWord(prePLR, i))).clone();
            //System.out.println(makeInt(memory.getWord(prePLR, i)));
            
        }
         
       // PID--;
        
        
    }
    
    /*private static void putToRMStack(char[] word){
        memory.writeWord(word, SP/16, SP%16);
    }
    
    private static char[] getFromRMStack(){
        return memory.getWord(SP/16, SP%16);
    }*/
    
    public static int makeInt(char[] word){
        int intWrd;
        int intWrdp1;
        int intWrdp2;
        int intWrdp3;
        int intWrdp4;
        
        
        
        if(word[0] == '0'){
           intWrdp1 = 0; 
        }
        else{
            intWrdp1 = (word[0] - 48) * 1000;
            
        }
        
        if(word[1] == '0'){
           intWrdp2 = 0; 
        }
        else{
            intWrdp2 = (word[1] - 48) * 100;
        }
        
        if(word[2] == '0'){
           intWrdp3 = 0; 
        }
        else{
            intWrdp3 = (word[2] - 48) * 10;
        }
        
        if(word[3] == '0'){
           intWrdp4 = 0; 
        }
        else{
            intWrdp4 = word[3] - 48;
        }

        
        intWrd = intWrdp1 + intWrdp2 + intWrdp3 + intWrdp4;
        
        return intWrd;
    }
    
    private static char[] get4BytesFromDat(String str){
        char[] buffer = new char[4];
        for(int i = 0; i<4; i++){
            buffer[i] = '0';
        }
        int j = 3;
        if(str.length()>4){
            System.out.println("Bad Syntax!");
        }
        
        for(int i = (str.length()-1); i>=0; i--){  
             if(j<0){
                System.out.println("Bad string!");
                j = 0;
            }
            buffer[j] = str.charAt(i);
            j--;
           
        } 
        
        return buffer;
    }
    
    public void setMO(byte mo){
        MO = mo;
    }
    
    /*public static void main(String[] args){ 
        RM rm = new RM();
        VM vm = new VM();
        //rm.addVM();
        
        rm.addVM(vm);
        
        //rm.printRMMemory();
        
        
        rm.loadVMCode("code3.txt");
        
       // rm.printRMMemory();
        
        
        rm.runFullVM();
        
       // rm.printRMMemory();
        
       // rm.printVMMemory();
        
       // rm.printVMregisters();
       // rm.printRMregisters();
    }*/

    
    

   
}
