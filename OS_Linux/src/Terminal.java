//package OS_Linux.src;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
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
    public Terminal()
    {
        parser = new Parser();
    }
    public void ls()
    {
        String currentPath = System.getProperty("user.dir");
        File[] files = new File(currentPath).listFiles(file -> !file.isHidden());
        if(files != null)
        {
            Arrays.sort(files);
            // Traverse through the files array
            for (File file: files)
            {
                System.out.print(file.getName() + " ");
            }
            System.out.print("\n");
        }
    }

    public void touch(String filePath)
    {
        String newPath = System.getProperty("user.dir");
        try
        {
            File relativePath = new File(newPath);
            if(relativePath.exists()){
                File relativePathFile = new File(newPath + "/" + filePath);
                if (!relativePathFile.createNewFile())
                {
                    System.out.println("File already exists ");
                }
            } else {
                File absolutePath = new File(filePath);
                if (!absolutePath.createNewFile())
                {
                    System.out.println("File already exists ");
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void cd(String[] arg)
    {
        String newDirectory;
        // if more than one argument is given
        if(arg.length > 1)
        {
            System.out.println("Error: command must have zero or one argument with no space");
            return;
        }
        // if no args were given
        if(arg.length == 0)
        {
            newDirectory = System.getProperty("user.home");
            System.setProperty("user.dir", newDirectory);
        }
        // get the parent directory in case cd ..
        else if(arg[0].equals(".."))
        {
            String currentDirectory = System.getProperty("user.dir");
            File file = new File(currentDirectory);
            newDirectory = file.getParent();
            System.setProperty("user.dir", newDirectory);
        }
        // change directory to the given absolute/relative path
        else
        {
            String newPath = System.getProperty("user.dir") +  "/" + arg[0];
            File relativePath = new File(newPath);
            File absolutePath = new File(arg[0]);
            // checks whether the given path can be relative or absolute
            if(relativePath.exists())
            {
                System.setProperty("user.dir", newPath);
            }
            else if(absolutePath.exists())
            {
                System.setProperty("user.dir", arg[0]);
            }
            else
            {
                System.out.println("Error: No such file or directory exist");
            }
        }
    }

    public void rmdir(String args)
    {
        if(args.equals("*")){
            String currentPath = System.getProperty("user.dir");
            File[] files = new File(currentPath).listFiles(file -> !file.isHidden());
            if(files != null)
            {
                boolean flag = false;
                // Traverse through the files array
                for (File file: files)
                {
                    if(file.isDirectory())
                    {
                        File[] dirFiles = file.listFiles();
                        if(dirFiles == null || dirFiles.length == 0)
                        {
                            file.delete();
                            flag = true;
                        }
                    }
                }
                if(!flag)
                {
                    System.out.println("No empty folders found in current directory");
                }
            }
            else
            {
                System.out.println("The current directory is already empty!");
            }
        }
        else
        {
            String currDir = System.getProperty("user.dir");
            File relativeCurrDir = new File(currDir + "/" + args);
            File currentDir = new File(args);
            if(relativeCurrDir.exists()){
                if(relativeCurrDir.isDirectory())
                {
                    File[] files = relativeCurrDir.listFiles();
                    if(files == null || files.length == 0)
                    {
                        relativeCurrDir.delete();
                    }
                    else
                    {
                        System.out.println(args + " is not empty!");
                    }
                }
                else
                {
                    System.out.println(args + " is not a directory!");
                }
            }
            else if(currentDir.exists())
            {
                if(currentDir.isDirectory())
                {
                    File[] files = currentDir.listFiles();
                    if(files == null || files.length == 0)
                    {
                        currentDir.delete();
                    }
                    else
                    {
                        System.out.println(args + " is not empty!");
                    }
                }
                else
                {
                    System.out.println(args + " is not a directory!");
                }
            }
            else
            {
                System.out.println("Error: No such file or directory exist");
            }
        }
    }
    public void echo(String[] args){ // echo command in linux prints the arguments
        for(int i=0;i<args.length;i++){
            System.out.print(args[i]+" "); //print the arguments
        }
        System.out.println();
    }
    //This method will choose the suitable command method to be called
    public void chooseCommandAction(String input){
        Parser parser = new Parser();
        if(parser.parse(input)){ // if the command is valid
            String commandName = parser.getCommandName();
            String[] arguments = parser.getArgs();
            switch (commandName) {
                case "echo" -> echo(arguments);
                case "ls" -> ls();
                case "touch" ->
                {
                    if(arguments.length == 1) touch(arguments[0]);
                    else System.out.println("Error: command must have only one argument with no space");
                }
                case "cd" ->
                {
                    cd(arguments);
                }
                case "rmdir" ->
                {
                    if(arguments.length == 1) rmdir(arguments[0]);
                    else System.out.println("Error: command must have only one argument with no space");
                }
                default -> System.out.println("Invalid command");
            }
        }
        else
        {
            System.out.println("Invalid command");
        }
    }
    public static void main(String[] args){
        Terminal terminal = new Terminal();
        while(true)
        {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            // if the user enters exit, then exit the program
            if(input.equals("exit"))
            {
                break;
            }
            terminal.chooseCommandAction(input);
        }

    }

}
