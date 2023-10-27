package OS_Linux.src;//package OS_Linux.src;//package OS_Linux.src;

import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;


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
            // split returns an array of strings , where each string is a word in the command
            commandName = temp[0]; // the first word is the command name
            args = new String[temp.length-1]; // the remaining words are the arguments
            for(int i=1;i<temp.length;i++){
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
    public void echo(String[] args){
        // if (>) is in arguments
        if(Arrays.asList(args).contains(">")){
            // get the index of >
            int index = Arrays.asList(args).indexOf(">");
            // get the content before >
            String content = "";
            for(int i=0;i<index;i++){
                content += args[i]+" ";
            }
            // get the file name after >
            String fileName = args[index+1];
            // redirect the content to the file
            redirect(content,fileName);
            return;
        }
        for(int i=0;i<args.length;i++){
            System.out.print(args[i]+" ");
        }
        System.out.println();
    }
    public void pwd(String[] args){
        if(Arrays.asList(args).contains('>')){
            int index = Arrays.asList(args).indexOf(">");
            String fileName = args[index+1];
            String path = System.getProperty("user.dir");
            redirect(path,fileName);
            return;
        }
        String path = System.getProperty("user.dir"); // get the current working directory
        System.out.println(path);
        // The pwd method uses the **System.getProperty("user.dir")** method to get the current working directory and prints it
    }
    public void mkdir(String[] args) throws IOException {
        // The mkdir method creates a new directory with the name given in the argument
        // For example, if the command is "mkdir newFolder", then a new folder with the name "newFolder" should be created in the current working directory
        for(int i=0;i<args.length;i++){
           // a path is given
            if(args[i].contains("\\")){
                String path = args[i];
                // add directory to the path
                java.nio.file.Files.createDirectory(java.nio.file.Paths.get(path));

            }
            else{
                String path = System.getProperty("user.dir")+"/"+args[i];
                java.io.File file = new java.io.File(path);
                if(!file.exists()){
                    file.mkdir();
                }
            }
        }
    }
    public void redirect(String content , String fileName){
        java.io.File file = new java.io.File(fileName);
        try{
           // split the content by space
            String[] temp = content.split(" ");
            // write the content to the file
            java.io.PrintWriter output = new java.io.PrintWriter(file);
            for(int i=0;i<temp.length;i++){
                output.print(temp[i]+" ");
            }
            output.close();

        }
        catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void cd(String[] args){
        // The cd method changes the current working directory to the directory given in the argument
        // For example, if the command is "cd newFolder", then the current working directory should be changed to the directory "newFolder"
        String newDir = args[0];
        String path = System.getProperty("user.dir")+"/"+newDir;
        System.setProperty("user.dir",path);

    }
    public static void main(String[] args){
        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print(">> ");
            String input = scanner.nextLine();
            if(input.equals("exit")){ // if the user enters exit, then exit the program
                break;
            }
            Parser parser = new Parser();
            if(parser.parse(input)){ // if the command is valid
                String commandName = parser.getCommandName();
                String[] arguments = parser.getArgs();
                if(commandName.equals("echo")){
                    terminal.echo(arguments);
                }
                else if(commandName.equals("pwd")){
                    terminal.pwd(arguments);
                }
                else if(commandName.equals("mkdir")){
                    try{
                        terminal.mkdir(arguments);
                    }
                    catch(IOException e){
                        System.out.println("Error: "+e.getMessage());
                    }
                }
                else{
                    System.out.println("Invalid command");
                }
            }
            else{
                System.out.println("Invalid command");
            }
        }
    }

}
