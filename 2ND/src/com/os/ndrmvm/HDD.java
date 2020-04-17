package com.os.ndrmvm;


public class HDD {
    //byte[][][] memory = new byte[16][16][4];
    
    private String[] hddMemory; //= new byte[256];
    private int index = 0;
    private com.os.ndrmvm.FlashDrive flash;
    
    char[][][] memory;
    int block = 0;
    int word = 0;
    int wordByte = 0;
    
    public HDD(){
        memory = new char[16][16][4];
        
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++ ){
                for(int k = 0; k < 4; k++){
                    memory[i][j][k] = '~';
                }
            }
        }
        
    }
    
    public void readToHdd( String[] mem ){
       /* flash = new com.os.ndrmvm.FlashDrive();
        //String fileName = "code1.txt";
                
        try {
            flash.loadProgram(fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.append("Problem");
        }*/
        
        hddMemory = mem;
        
        /*for(int i = 0; i<hddMemory.length; i++){
            System.out.print(hddMemory[i]);
        }*/
        
        /*while(!stack){
            if(data){
                
            }
            if(code){
                
            }
    
        }*/
       // System.out.println(get4BytesFromSTR("F52A"));

    }
    
    
    public char[][][] checkCode(){        
        if(hddMemory == null){
            System.out.println("Load something to hdd first");
            return null;
        }
        
        
        
        
        boolean data = false;
        boolean code = false;
        //boolean stack = false;
        
        //System.out.println(getBytesInChar(4));
        //System.out.println(getBytesInChar(3));
       // System.out.println(hddMemory[1].substring(0, 2));
        if(hddMemory[0].equals("#DAT")){
            data = true;
        }
        else{
            System.out.println("Bad Syntax!");
            return null;
        }
        if(hddMemory[1].equals("#PRO") ){
            data = false;
            code = true;
        }
        else{
            int i = 1;
            while( data ){
                if(i > 111){
                    //System.out.println("Data segmentas uzima per daug vietos!");
                    RM.setPI((byte)6);
                    return null;
                }
                if(hddMemory[i].substring(0, 2).equals("DN")){
                    //System.out.println("Adding!");
                    addWordToDataSegMem(get4BytesFromDat(hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].substring(0, 2).equals("DC")){
                    //System.out.println("Adding!");
                    addWordToDataSegMem(get4BytesFromDat(hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].equals("#PRO")){
                    data = false;
                    code = true;
                }
                else{
                    //System.out.println("Bad Syntax!");
                    RM.setPI((byte)2);
                    return null;
                }
                
                i++;
            }
            
            block = 7;
            word = 0;
            while(code){
                if(i > 223){
                    //System.out.println("Codo segmentas uzima per daug vietos!");
                    RM.setPI((byte)6);
                    return null;
                }
                if(hddMemory[i].substring(0, 3).equals("ADD")){
                    //System.out.println("Adding! add");
                    addWordToCodeSegMem(get4BytesFromSTR("01"));
                }
                else if(hddMemory[i].substring(0, 3).equals("SUB")){
                    //System.out.println("Adding! sub");
                    addWordToCodeSegMem(get4BytesFromSTR("02"));
                }
                else if(hddMemory[i].substring(0, 3).equals("MUL")){
                    //System.out.println("Adding! mul");
                    addWordToCodeSegMem(get4BytesFromSTR("03"));
                }
                else if(hddMemory[i].substring(0, 3).equals("DIV")){
                    //System.out.println("Adding! div");
                    addWordToCodeSegMem(get4BytesFromSTR("04"));
                }
                else if(hddMemory[i].substring(0, 3).equals("CMP")){
                    //System.out.println("Adding! cmp");
                    addWordToCodeSegMem(get4BytesFromSTR("05"));
                }
                else if(hddMemory[i].substring(0, 2).equals("LD")){
                    //System.out.println("Adding! ld");
                    addWordToCodeSegMem(get4BytesFromSTR("06"+hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].substring(0, 2).equals("PT")){
                    //System.out.println("Adding! pt");
                    addWordToCodeSegMem(get4BytesFromSTR("07"+hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].substring(0, 2).equals("PN")){
                    //System.out.println("Adding! pn");
                    addWordToCodeSegMem(get4BytesFromSTR("0800"));
                    addWordToCodeSegMem(get4BytesFromDat(hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].substring(0, 2).equals("PC")){
                    //System.out.println("Adding! pc");
                    addWordToCodeSegMem(get4BytesFromSTR("09"+hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].substring(0, 2).equals("JE")){
                    //System.out.println("Adding! je");
                    addWordToCodeSegMem(get4BytesFromSTR("0A"+hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].substring(0, 2).equals("JL")){
                    //System.out.println("Adding! jl");
                    addWordToCodeSegMem(get4BytesFromSTR("0B"+hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].substring(0, 2).equals("JG")){
                    //System.out.println("Adding! jg");
                    addWordToCodeSegMem(get4BytesFromSTR("0C"+hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].substring(0, 2).equals("JM")){
                    //System.out.println("Adding! jm");
                    addWordToCodeSegMem(get4BytesFromSTR("0D"+hddMemory[i].substring(2)));
                }
                else if(hddMemory[i].equals("FORK")){
                    //System.out.println("Adding! fork");
                    addWordToCodeSegMem(get4BytesFromSTR("0E00"));
                }
                else if(hddMemory[i].substring(0, 3).equals("ISP")){
                    //System.out.println("Adding! ips");
                    addWordToCodeSegMem(get4BytesFromSTR("0F00"));
                }
                /*else if(hddMemory[i].substring(0, 3).equals("KIL")){
                    System.out.println("Adding! kill");
                    addWordToCodeSegMem(get4BytesFromSTR("10"+hddMemory[i].substring(3)));
                }*/
                else if(hddMemory[i].substring(0, 4).equals("FSTP")){
                    //System.out.println("Adding! fstp");
                    addWordToCodeSegMem(get4BytesFromSTR("1100"));
                }
                else if(hddMemory[i].substring(0, 4).equals("STOP")){
                   //System.out.println("Adding! stop");
                    addWordToCodeSegMem(get4BytesFromSTR("1200"));
                }
                else if(hddMemory[i].substring(0, 4).equals("PRTN")){
                   // System.out.println("Adding! prtn");
                    addWordToCodeSegMem(get4BytesFromSTR("1300"));
                }
                else if(hddMemory[i].substring(0, 4).equals("PRTC")){
                    //System.out.println("Adding prtc!");
                    addWordToCodeSegMem(get4BytesFromSTR("1400"));
                }
                else if(hddMemory[i].substring(0, 4).equals("PRNL")){
                    //System.out.println("Adding! prnl");
                    addWordToCodeSegMem(get4BytesFromSTR("1500"));
                }
                else if(hddMemory[i].charAt(0) == 'P' ){
                    //System.out.println("Adding! p");
                    addWordToCodeSegMem(get4BytesFromSTR("F"+hddMemory[i].substring(1)));
                }
                else if(hddMemory[i].equals("#END")){
                    addWordToCodeSegMem(get4BytesFromSTR("!!!!"));
                    code = false;
                }
                
                else{
                    //System.out.println("Bad Syntax!");
                    RM.setPI((byte)2);
                    return null;
                }
                
                i++;
            }
        }
        
        
        return memory;
    }
    
    private void addWordToDataSegMem(char[] wrd){
        if(word > 16){
            block +=1;
        }
        if(block>6){
            //System.out.println("Data segment takes up too much space!");
             RM.setPI((byte)6);
             return;
        }
        memory[block][word] = wrd;
        
        //System.out.println(memory[block][word]);
        
        word+=1;
        
    }
     private void addWordToCodeSegMem(char[] wrd){
        if(word > 16){
            block +=1;
        }
        if(block>13){
            RM.setPI((byte)6);
            return;
            //System.out.println("Code segment takes up too much space!");
        }
        memory[block][word] = wrd;
        
        //System.out.println(memory[block][word]);
        
        word+=1;
        
    }
     
    
    private char[] get4BytesFromSTR(String str){
        char[] buffer = new char[4];
        for(int i = 0; i<4; i++){
            buffer[i] = '0';
        }
        //int j = 3;
        
        if(str.length()>4){
            //System.out.println("Bad Syntax!");
             RM.setPI((byte)2);
            return null;
        }
        
        if(str.charAt(0)== 'F'){
            buffer[0] = str.charAt(0);
            buffer[1] = str.charAt(1);
            buffer[2] = str.charAt(2);
            buffer[3] = str.charAt(3);
            return buffer;
        }
        
        /*if(str.charAt(0)== '0' && str.charAt(1) == '9'){
            buffer[0] = str.charAt(0);
            buffer[1] = str.charAt(1);
            char sim1 = (char) ((str.charAt(2)/10)+48);
            char sim2 = (char) ((str.charAt(2)%10)+48);
            buffer[2] = sim1;
            buffer[3] = sim2;
            
            return buffer;
        }*/
        
        if(str.length()<3){
            //System.out.println("Bad Syntax!");
            buffer[0] = str.charAt(0);
            buffer[1] = str.charAt(1);
            return buffer;
        }
        
       if(str.length()<4){
            //System.out.println("Bad Syntax!");
            buffer[0] = str.charAt(0);
            buffer[1] = str.charAt(1);
            buffer[3] = str.charAt(2);
            return buffer;
        }
        
       if(str.length()==4){
            //System.out.println("Bad Syntax!");
            buffer[0] = str.charAt(0);
            buffer[1] = str.charAt(1);
            buffer[2] = str.charAt(2);
            buffer[3] = str.charAt(3);
            return buffer;
        }
       
        return buffer;
    }
    
    private char[] get4BytesFromDat(String str){
        char[] buffer = new char[4];
        for(int i = 0; i<4; i++){
            buffer[i] = '0';
        }
        int j = 3;
        if(str.length()>4){
            //System.out.println("Bad Syntax!");
             RM.setPI((byte)2);
             return null;
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
    
    /*private String getBytesInChar( int howMany) {    
     String buffer = "";
     index += howMany; 
     for(int i = index-howMany; i < index; i++){
         buffer += hddMemory[i];
     }
     return buffer;
    }*/
    
}
