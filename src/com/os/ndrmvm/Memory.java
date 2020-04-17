package com.os.ndrmvm;

import java.util.Arrays;

public class Memory {
    public final int BLOCK_COUNT = 64;
    public final int BLOCK_SIZE = 16;
    public final int WORD_SIZE = 4;

    //protected Word[][] memory = new Word[BLOCK_COUNT][BLOCK_SIZE];
    private char[][][] memory = new char[BLOCK_COUNT][BLOCK_SIZE][WORD_SIZE];
    private boolean[] blockTaken = new boolean[BLOCK_COUNT];
    
    
    public int usedCODEBlocks = 0;
    public int usedDATABlocks = 0;



    protected int offset = 0;

    public Memory() {
        
        for(int i = 0; i < BLOCK_COUNT; i++){
            for(int j = 0; j < BLOCK_SIZE; j++ ){
                for(int k = 0; k < WORD_SIZE; k++){
                    memory[i][j][k] = '`';
                }
            }
        }
        
        for(int i = 0; i<BLOCK_COUNT; i++){
            blockTaken[i] = false;
        }
        
    }



    public char[] getWord(int x1, int x2) {
        return memory[x1][x2].clone();
    }

    public void writeWord(char[] wrd, int x1, int x2) {
        memory[x1][x2] = wrd.clone();
        
        //offset++;
    }

    public void writeBlock(char[][] blck, int x1){
        memory[x1] = blck.clone();
        blockTaken[x1] = true;
    }
    
    public void replaceBlock(char[][] blck, int x1){
        memory[x1] = blck.clone();
    }
    
    public char[][] getBlock(int x1){
        return memory[x1].clone();
    }
    
    public void clearBlock(int x1){
        
        for(int i = 0; i < BLOCK_SIZE; i++){
            for(int j = 0; j < WORD_SIZE; j++){
                memory[x1][i][j] = '`';
            }
        }
        blockTaken[x1] = false;
        
    }
    
    public boolean checkAvailableSpace(){
        int available = 0;
        for(int i = 0; i<BLOCK_COUNT; i++){
            if(blockTaken[i] == false){
                available++;
                if(available >= 17){
                    return true;
                }
            }
        }
        
        if(available < 17){
            return false;
        }
        
        return true;
        
    }
    
    public boolean isBlockEmpty(int x1){
        
        if( blockTaken[x1] == false ){
            return true;
        }
        
        return false;
        
    }

    public void display() {
       /* for (int x = 0; x < BLOCK_COUNT; ++x) {
            for (int y = 0; y < BLOCK_SIZE; ++y) {
                System.out.print(memory[x][y] + "|");
            }
            System.out.println();
        }
        RM.sMemory.display();*/
        //RM.sharedMemory.display();
    }

    @Override
    public String toString() {
        return "Memory{" +
                "BLOCK_COUNT=" + BLOCK_COUNT +
                ", BLOCK_SIZE=" + BLOCK_SIZE +
                ", memory=" + Arrays.toString(memory);
    }
}

