//package OS_Linux.src;

import java.util.Scanner;

class Parser{
    String commandName;
    String[] args;
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input){ //returns true if the command is valid
     // input is the command
        commandName = input;
        // args should be the arguments of the command
        // for example, if the command is "echo hello world", then commandName = "echo" and args = ["hello", "world"]
        // if the command is "pwd", then commandName = "pwd" and args = []
        args = new String[0];
        if(input.contains(" ")){ // if the command contains space
            String[] temp = input.split(" "); // split the command by space
            commandName = temp[0]; // the first word is the command name
            args = new String[temp.length-1]; // the remaining words are the arguments
            for(int i = 1; i < temp.length; i++){
                args[i-1] = temp[i];
            }
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
    //This method will choose the suitable command method to be called
    public static void chooseCommandAction(String input){
        Parser parser = new Parser();
        if(parser.parse(input)){ // if the command is valid
            String commandName = parser.getCommandName();
            String[] arguments = parser.getArgs();
            if(commandName.equals("echo")){ // if the command is echo
                Terminal terminal = new Terminal();
                terminal.echo(arguments);
            }
            else{
                System.out.println("Invalid command");
            }
        }
        else{
            System.out.println("Invalid command");
        }
    }
    public static void main(String[] args){
        while(true){
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equals("exit")){ // if the user enters exit, then exit the program
                break;
            }
            chooseCommandAction(input);
        }

    }

}
