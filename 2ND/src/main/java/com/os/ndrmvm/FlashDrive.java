package com.os.ndrmvm;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FlashDrive {
    private String[] memory;
    
    public void loadProgram(String fileName) throws FileNotFoundException, IOException{
        
        
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(fileName), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        String buffer = contentBuilder.toString();
        
        memory = buffer.split("\\r?\\n"); //System.getProperty("line.separator")
        
        for(int i = 0; i<memory.length; i++){
            memory[i] = memory[i].replace("\n", "").replace("\r", "");
        }
        
        
    }
     
    public String[] getReadMem(){
        //memory = memory.replace("\n", "").replace("\r", "");
        return memory;
    }
    
}
