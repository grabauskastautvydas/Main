package com.os.ndrmvm;

public class VM {
    
    private short IC;
    private short SP;
    private byte PID;
       
    private final String ADD = "01";
    private final String SUB = "02";
    private final String MUL = "03";
    private final String DIV = "04";
    private final String CMP = "05";
    private final String LD = "06";
    private final String PT = "07";
    private final String PN = "08";
    private final String PC = "09";
    private final String JE = "0A";
    private final String JL = "0B";
    private final String JG = "0C";
    private final String JM = "0D";
    private final String FORK = "0E";
    private final String ISP = "0F";
    //private final String KIL = "10";
    private final String FSTP = "11";
    private final String STOP = "12";
    private final String PRTN = "13";
    private final String PRTC = "14";
    private final String PRNL = "15";
    private final String P = "F";
    private final String END = "!!!!";
    
    private boolean endOfWork;
    
    public VM(){
        SP = 223;
        IC = 112;
        PID = 0;
        
        endOfWork = false;
        //initStack();
        
    }
    public VM(short ic, short sp, byte pid){
        this.IC = ic;
        this.SP = sp;
        this.PID = pid;
        
        endOfWork = false;    
        
    }
    
    public short getIC(){
        return IC;
    }
    
    public short getSP(){
        return SP;
    }
    
    public byte getPID(){
        return PID;
    }
    
    public boolean doNext(){
        int ic1 = IC/16;
        int ic2 = IC%16;
        executeCom(RM.getWordVMQuery(ic1, ic2));
        
        return endOfWork;
        
    }
    
    public boolean getEndWorkStatus(){
        return endOfWork;
    }
    
    public void fullRun(){
        while(!endOfWork){
           int ic1 = IC/16;
           int ic2 = IC%16;
            executeCom(RM.getWordVMQuery(ic1, ic2));

        }
    }
    
    public void executeCom(char[] com){
        String comand = new String(com);
        
        if(comand.substring(0, 2).equals(ADD)){
            IC++;
            comADD();
        }
        else if(comand.substring(0, 2).equals(SUB)){
            IC++;
            comSUB();
        }
        else if(comand.substring(0, 2).equals(MUL)){
            IC++;
            comMUL();
        }
        else if(comand.substring(0, 2).equals(DIV)){
            IC++;
            comDIV();
        }
        else if(comand.substring(0, 2).equals(CMP)){
            IC++;
            comCMP();
        }
        else if(comand.substring(0, 2).equals(LD)){
            IC++;
            int x1 = Integer.parseInt(comand.substring(2, 3));
            int x2 = Integer.parseInt(comand.substring(3, 4));
            
            if(x1 > 6 || x1 < 0 || x2 > 15 || x2 < 0){
                RM.setPI((byte)1);
                endOfWork = true;
                return;
            }
            
            comLD(x1,x2);
        }
        else if(comand.substring(0, 2).equals(PT)){
            IC++;
            int x1 = Integer.parseInt(comand.substring(2, 3));
            int x2 = Integer.parseInt(comand.substring(3, 4));
            
            if(x1 > 6 || x1 < 0 || x2 > 15 || x2 < 0){
                RM.setPI((byte)1);
                endOfWork = true;
                return;
            }
            
            
            comPT(x1,x2);
        }
        else if(comand.substring(0, 2).equals(PN)){
            IC++;
            
            //int ic1 = IC/16;
            //int ic2 = IC%16;
            
            String num = new String(getWordFromMem(IC/16, IC%16));
            IC++;
            
            int x1 = Integer.parseInt(num);
            //System.out.println(x1);
            comPN(x1);
        }
        else if(comand.substring(0, 2).equals(PC)){
            IC++;
            comPC(comand.substring(3, 4));
        }
        else if(comand.substring(0, 2).equals(JE)){
            IC++;
            
            int x1 = Integer.parseInt(comand.substring(2, 3));
            int x2 = Integer.parseInt(comand.substring(3, 4));
            
            if(x1 > 13 || x1 < 7 || x2 > 15 || x2 < 0){
                RM.setPI((byte)1);
                endOfWork = true;
                return;
            }
            
            comJE(x1,x2);
        }
        else if(comand.substring(0, 2).equals(JL)){
            IC++;
            int x1 = Integer.parseInt(comand.substring(2, 3));
            int x2 = Integer.parseInt(comand.substring(3, 4));
            
            if(x1 > 13 || x1 < 7 || x2 > 15 || x2 < 0){
                RM.setPI((byte)1);
                endOfWork = true;
                return;
            }
            
            
            comJL(x1,x2);
        }
        else if(comand.substring(0, 2).equals(JG)){
            IC++;
            int x1 = Integer.parseInt(comand.substring(2, 3));
            int x2 = Integer.parseInt(comand.substring(3, 4));
            
            if(x1 > 13 || x1 < 7 || x2 > 15 || x2 < 0){
                RM.setPI((byte)1);
                endOfWork = true;
                return;
            }            
            
            comJG(x1,x2);
        }
        else if(comand.substring(0, 2).equals(JM)){
            IC++;
            int x1 = Integer.parseInt(comand.substring(2, 3));
            int x2 = Integer.parseInt(comand.substring(3, 4));
            
            if(x1 > 13 || x1 < 7 || x2 > 15 || x2 < 0){
                RM.setPI((byte)1);
                endOfWork = true;
                return;
            }            
            
            comJM(x1,x2);
        }
        else if(comand.substring(0, 2).equals(FORK)){
            IC++;
            RM.setSI((byte)4);
            comFORK();
        }
        else if(comand.substring(0, 2).equals(ISP)){
            IC++;
            comISP();
        }
        /*else if(comand.substring(0, 2).equals(KIL)){
            IC++;
            int x1 = Integer.parseInt(comand.substring(3, 4));
            comKIL(x1);
        }*/
        else if(comand.substring(0, 2).equals(FSTP)){
            IC++;
            RM.setSI((byte)2);
            comFSTP();
        }
        else if(comand.substring(0, 2).equals(STOP)){
            IC++;
            RM.setSI((byte)3);
            comSTOP();
        }
        else if(comand.substring(0, 2).equals(PRTN)){
            IC++;
            RM.setSI((byte)1);
            comPRTN();
        }
        else if(comand.substring(0, 2).equals(PRTC)){
            IC++;
            RM.setSI((byte)1);
            comPRTC();
        }
        else if(comand.substring(0, 2).equals(PRNL)){
            IC++;
            RM.setSI((byte)1);
            comPRNL();
        }
        else if(comand.substring(0, 2).equals(P)){
            IC++;
            RM.setSI((byte)1);
            int x = Integer.parseInt(comand.substring(1, 2));
            int y = Integer.parseInt(comand.substring(2, 3));
            int z = Integer.parseInt(comand.substring(3, 4));
            comP(x,y,z);
        }
        else if(comand.substring(0, 4).equals(END)){
            IC++;
            comEND_OF_CODE();
        }
        else{
            RM.setPI((byte)3);
            endOfWork = true;
            return;
        }
        
    }
    
    private void comADD(){
        int st1 = Integer.parseInt(new String(getFromStack()));
        int st2 = Integer.parseInt(new String(getFromStackATCusSP(SP-1)));
        //System.out.println(st1+ " + "+st2);
        int sum = st1 + st2;
        
        if(sum > 9999 || sum < -999){
            endOfWork = true;
            RM.setPI((byte)4);
            return;
        }
        
        SP--;
        //putToStackAT(SP, String.valueOf(sum).toCharArray());
        putToStackAT(SP,String.format("%04d", sum).toCharArray());
        
    }
    private void comSUB(){
        int st1 = Integer.parseInt(new String(getFromStack()));
        int st2 = Integer.parseInt(new String(getFromStackATCusSP(SP-1)));
        int sub = st1 - st2; 
        
        if(sub > 9999 || sub < -999){
            endOfWork = true;
            RM.setPI((byte)4);
            return;
        }
        
        
        SP--;
        //putToStackAT(SP,String.valueOf(sub).toCharArray());
        putToStackAT(SP,String.format("%04d", sub).toCharArray());
    }
    private void comMUL(){
        int st1 = Integer.parseInt(new String(getFromStack()));
        int st2 = Integer.parseInt(new String(getFromStackATCusSP(SP-1)));
        int mul = st1 * st2; 
        
        if(mul > 9999 || mul < -999){
            endOfWork = true;
            RM.setPI((byte)4);
            return;
        }
        
        SP--;
        //putToStackAT(SP,String.valueOf(mul).toCharArray());
        putToStackAT(SP,String.format("%04d", mul).toCharArray());
    }
    private void comDIV(){
        int st1 = Integer.parseInt(new String(getFromStack()));
        int st2 = Integer.parseInt(new String(getFromStackATCusSP(SP-1)));
       
        if(st1 == 0){
            endOfWork = true;
             RM.setPI((byte)5);
            return;
        }
        
        int div = st2/st1;
        SP--;
        //putToStackAT(SP,String.valueOf(div).toCharArray());
        putToStackAT(SP,String.format("%04d", div).toCharArray());
    }
    private void comCMP(){
        //String w1 = new String(getFromStack());
        //String w2 = new String(getFromStackATCusSP(SP-1));
        int st1 = Integer.parseInt(new String(getFromStack()));
        int st2 = Integer.parseInt(new String(getFromStackATCusSP(SP-1)));
        
        if(st1 == st2){
            putToStack(String.format("%04d", 1).toCharArray());
        }
        else if(st1 > st2){
            putToStack(String.format("%04d", 2).toCharArray());
        } 
        else if(st1 < st2){
            putToStack(String.format("%04d", 0).toCharArray());
        }
        
        
    }
    private void comLD(int x1, int x2){      
        putToStack(getWordFromMem(x1,x2));
    }
    private void comPT(int x1, int x2){
        putWorToMem(x1, x2, getFromStack());    
    }
    private void comPN(int x){
        putToStack(String.format("%04d", x).toCharArray());
    }
    private void comPC(String chr){
        putToStack(("000"+chr).toCharArray());
    }
    private void comJE(int x1, int x2){
        int st1 = Integer.parseInt(new String(getFromStack()));
        if(st1 == 1){
            IC = (short)(16*x1+x2);
        }
        
    }
    private void comJL(int x1, int x2){
        int st1 = Integer.parseInt(new String(getFromStack()));
        if(st1 == 0){
            IC = (short)(16*x1+x2);
        }
    }
    private void comJG(int x1, int x2){
        int st1 = Integer.parseInt(new String(getFromStack()));
        if(st1 == 2){
            IC = (short)(16*x1+x2);
        }
    }
    private void comJM(int x1, int x2){
        IC = (short)(16*x1+x2);
    }
    private void comFORK(){
        putToStack(String.format("%04d", PID).toCharArray());
        RM.forkWMquery(IC, SP, (byte) (PID+1));
    }
    private void comISP(){
        if(PID == Integer.parseInt(new String(getFromStack()))){
            putToStack(String.format("%04d", 1).toCharArray());
        }
        else{
            putToStack(String.format("%04d", 0).toCharArray());
        }
    }
    /*private void comKIL(int x){
        
    }*/
    private void comFSTP(){
        endOfWork = true;
    }
    private void comSTOP(){
       if(Integer.parseInt(new String(getFromStack())) == 1 ){
          endOfWork = true;   
       }
    }
    private void comPRTN(){
        int st1 = Integer.parseInt(new String(getFromStack()));
        //System.out.println(st1); /////!!!!!!!!!!!!!!!!!!!!!!!!!!!!Turi buti printeris is RM CH
        
        RM.printerPrint(String.valueOf(st1));
        
    }
    private void comPRTC(){
        //System.out.println(new String(getFromStack()));/////!!!!!!!!!!!!!!!!!!!!!!!!!!!!Turi buti printeris is RM CH
        RM.printerPrint(new String(getFromStack()));
        
    }
    private void comPRNL(){
        //System.out.println();/////!!!!!!!!!!!!!!!!!!!!!!!!!!!!Turi buti printeris is RM CH
        RM.printerPrint("");
    }
    private void comP(int x, int y, int z){
       for(int i = y; i<=z; i++){ /////!!!!!!!!!!!!!!!!!!!!!!!!!!!!Turi buti printeris is RM CH
           //System.out.println(new String(getWordFromMem(x,i)));
           RM.printerPrint(new String(getWordFromMem(x,i)));
       }
    } 
    private void comEND_OF_CODE(){
        endOfWork = true;
    }
    
    private char[] getWordFromMem(int x1, int x2){
        //return memory[x1][x2];
        
        return RM.getWordVMQuery(x1, x2);
        
    }
    
    private void putWorToMem(int x1, int x2, char[] word){
        
        //memory[x1][x2] = word;
        RM.putWordVMQuery(word, x1, x2);
        
    }
    
    private void putToStack(char[] word){
        SP++;
        if(SP >255){
            SP = 224;
        }
        
        //System.out.println(word);
        putWorToMem(SP/16,SP%16, word);
    }
     private void putToStackAT(int sp, char[] word){
        if(sp >255){
           //blogas adresavimas 
           return;
        }
        putWorToMem(sp/16,sp%16, word);
    }
    
    private char[] getFromStack(){
        return getWordFromMem(SP/16, SP%16);
    }
    private char[] getFromStackATCusSP(int sp){
        return getWordFromMem(sp/16, sp%16);
    }
    /*
    public static void main(String[] args){ 
        /*char[][][] memory = new char[16][16][4];
        HDD hard = new HDD();    
        String fileName = "code1.txt";
        hard.readToHdd(fileName);
        memory = hard.checkCode();
        
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++ ){
                for(int k = 0; k < 4; k++){
                    System.out.print(memory[i][j][k]);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
     */
        /*
        VM vm = new VM();
        vm.testVM();
        
        
    }
       */

    
    
    
    
}
