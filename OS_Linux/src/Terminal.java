//package OS_Linux.src;

import java.util.Scanner;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

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
    //This method will choose the suitable command method to be called
    public void chooseCommandAction(String input){
        Parser parser = new Parser();
        if(parser.parse(input)){ // if the command is valid
            String commandName = parser.getCommandName();
            String[] arguments = parser.getArgs();
            switch (commandName){
                case "echo" : echo(arguments);
                case "ls -r" : lsR();
                default : System.out.println("Invalid command");
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
