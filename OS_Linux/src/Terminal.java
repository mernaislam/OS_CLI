//package OS_Linux.src;

import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.nio.file.StandardCopyOption;

class Parser{
    String commandName;
    String[] args;
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input){ //returns true if the command is valid
        // Split the input by spaces
        String[] temp = input.split(" ");

        // The first part is the command name
        commandName = temp[0];

        // Initialize arguments
        args = new String[0];

        //check if the command includes a space followed by "-"
        if (temp.length > 1 && temp[1].startsWith("-")) {
            // Add the second part to the command name
            commandName += " " + temp[1];

            // The remaining parts are arguments
            args = Arrays.copyOfRange(temp, 2, temp.length);
        } else if (temp.length > 1) {
            // The remaining parts are arguments
            args = Arrays.copyOfRange(temp, 1, temp.length);
        }
        return true;
    }
    public String getCommandName(){ //returns the command name
        return commandName;
    }
    public String[] getArgs(){ //returns the arguments
        return args;
    }

}

public class Terminal{
    Parser parser;
    String path;
    public Terminal(){
        parser = new Parser();
        path = System.getProperty("user.dir");
    }
    public String getPath(){
        return path;
    }
    public void echo(String[] args){ // echo command in linux prints the arguments
        for(int i=0;i<args.length;i++){
            System.out.print(args[i]+" "); //print the arguments
        }
        System.out.println();
    }
    public void lsR(){
        String currentDir = System.getProperty("user.dir");
        File[] files = new File(currentDir).listFiles(file -> !file.isHidden());

        if(files != null){
            //comparing the names of the files in reverse order
            //using a custom comparator for sorting
            Arrays.sort(files, (f1, f2) -> f2.getName().compareTo(f1.getName()));
            for(File f : files){
                System.out.print(f.getName() + " ");
            }
        }
        else {
            System.out.println("Failed to list directory contents.");
        }
    }
    public void cp(String sourcePath, String destinationPath) {
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        if (sourceFile.exists()) {
            try {
                //convert file object to path to use it with Files.copy() method
                //handle the case if the destination file already exists
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied successfully.");
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            System.err.println("Error: Source file does not exist.");
        }
    }
    public void rm(String fileName) {
        String currentDir = System.getProperty("user.dir");
        File file = new File(currentDir, fileName);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File '" + fileName + "' removed successfully.");
            } else {
                System.err.println("Error: Failed to remove file '" + fileName + "'.");
            }
        } else {
            System.err.println("Error: File '" + fileName + "' does not exist in the current directory.");
        }
    }


    //This method will choose the suitable command method to be called
    public void chooseCommandAction(String input){
        Parser parser = new Parser();
        if(parser.parse(input)){ // if the command is valid
            String commandName = parser.getCommandName();
            String[] arguments = parser.getArgs();
            switch (commandName){
                case "echo" -> echo(arguments);
                case "ls -r" -> lsR();
                case "cp" -> {
                    if (arguments.length == 2) {
                        cp(arguments[0], arguments[1]);
                    } else {
                        System.err.println("Expected: cp <source> <destination>");
                    }
                }
                case "rm" -> {
                    if (arguments.length == 1) {
                        rm(arguments[0]);
                    } else {
                        System.err.println("Expected: rm <file>");
                    }
                }
                default -> System.out.println("Invalid command");
            }
        }
        else{
            System.out.println("Invalid command");
        }
    }
    public static void main(String[] args){
        Terminal terminal = new Terminal();
        while(true){
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equals("exit")){ // if the user enters exit, then exit the program
                break;
            }
            terminal.chooseCommandAction(input);
        }

    }

}
