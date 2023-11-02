package OS_Linux.src;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

class Parser{
    String commandName;
    String[] args;
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input){ //returns true if the command is valid
        String[] command = input.split(" ");
        commandName = command[0];
        if(command.length > 1 && Objects.equals(command[1], "-r")){
            commandName = command[0] + " " + command[1];
            args = Arrays.copyOfRange(command, 2, command.length);
        }
        else{
            args = Arrays.copyOfRange(command, 1, command.length);
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
    public void ls(String[] arg)
    {
        String currentPath = System.getProperty("user.dir");
        File[] files = new File(currentPath).listFiles(file -> !file.isHidden());
        if(files != null)
        {
            if(Arrays.asList(arg).contains('>')){
                String content = "";
                Arrays.sort(files);
                for (File file: files)
                {
                    content += file.getName() + " ";
                }
                int index = Arrays.asList(arg).indexOf('>');
                if(index == arg.length - 1){
                    System.out.println("Error: missing file path");
                    return;
                }
                String path = arg[index + 1];
                redirect(content, path);
                return;
            }
            if(Arrays.asList(arg).contains(">>")){
                String content = "";
                Arrays.sort(files);
                for (File file: files)
                {
                    content += file.getName() + " ";
                }
                int index = Arrays.asList(arg).indexOf(">>");
                if(index == arg.length - 1){
                    System.out.println("Error: missing file path");
                    return;
                }
                String path = arg[index + 1];
                append(content, path);
                return;
            }
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
            if(index == args.length - 1){
                System.out.println("Error: missing file path");
                return;
            }
            // get the file name after >
            String fileName = args[index+1];
            // redirect the content to the file
            redirect(content,fileName);
            return;
        }
        else if(Arrays.asList(args).contains(">>")){
            // get the index of >>
            int index = Arrays.asList(args).indexOf(">>");
            // get the content before >>
            String content = "";
            for(int i=0;i<index;i++){
                content += args[i]+" ";
            }
            if(index == args.length - 1){
                System.out.println("Error: missing file path");
                return;
            }
            // get the file name after >>
            String fileName = args[index+1];
            // append the content to the file
            append(content,fileName);
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
            if(index == args.length - 1){
                System.out.println("Error: missing file path");
                return;
            }
            String fileName = args[index+1];
            String path = System.getProperty("user.dir");
            redirect(path,fileName);
            return;
        }
        else if(Arrays.asList(args).contains(">>")){
            int index = Arrays.asList(args).indexOf(">>");
            if (index == args.length - 1) {
                System.out.println("Error: missing file path");
                return;
            }
            String fileName = args[index+1];
            String path = System.getProperty("user.dir");
            append(path,fileName);
            return;
        }
        String path = System.getProperty("user.dir"); // get the current working directory
        System.out.println(path);
        // The pwd method uses the **System.getProperty("user.dir")** method to get the current working directory and prints it
    }
    public void mkdir(String[] args) {
        // The mkdir method creates a new directory with the name given in the argument
        // For example, if the command is "mkdir newFolder", then a new folder with the name "newFolder" should be created in the current working directory
        for(int i=0;i<args.length;i++){
            if(args[i].contains("\\") || args[i].contains("/")){
                String path = args[i];
                File file = new File(path);
                if(!file.mkdir()){
                    System.out.println("Error: Directory not created");
                }
            }
            else{
                String path = System.getProperty("user.dir")+"\\"+args[i];
                File file = new File(path);
                if(!file.mkdir()){
                    System.out.println("Error: Directory not created");
                }
            }
        }
    }
    public void redirect(String content , String fileName){
        try{
            // split the content by space
            String[] temp = content.split(" ");
            // write the content to the file\
            String path = System.getProperty("user.dir")+"\\"+fileName;
                File file = new File(path);
                FileWriter fr = new FileWriter(file);
                for(int i=0;i<temp.length;i++){
                    fr.write(temp[i]+" ");
                }
                fr.close();

        }
        catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void append(String content , String fileName){
        try{
            // split the content by space
            String[] temp = content.split(" ");
            // append the content to the file
            String path = System.getProperty("user.dir")+"\\"+fileName;
            File file1 = new File(path);
            FileWriter fr = new FileWriter(file1,true);
            for(int i=0;i<temp.length;i++){
                fr.write(temp[i]+" ");
            }
            fr.close();
        }
        catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void lsR(String []arg){
        String currentDir = System.getProperty("user.dir");
        File[] files = new File(currentDir).listFiles(file -> !file.isHidden());

        if(files != null){
            Arrays.sort(files, Collections.reverseOrder());
            //comparing the names of the files in reverse order
            //using a custom comparator for sorting
            if(Arrays.asList(arg).contains('>')){
                int index = Arrays.asList(arg).indexOf(">");
                if(index == arg.length - 1){
                    System.out.println("Error: missing file path");
                    return;
                }
                String content = "";
                for(File f : files){
                    content += f.getName()+" ";
                }
                String fileName = arg[index+1];
                redirect(content,fileName);
                return;
            }
            else if(Arrays.asList(arg).contains(">>")){
                int index = Arrays.asList(arg).indexOf(">>");
                if(index == arg.length - 1){
                    System.out.println("Error: missing file path");
                    return;
                }
                String content = "";
                for(File f : files){
                    content += f.getName()+" ";
                }
                String fileName = arg[index+1];
                append(content,fileName);
                return;
            }
            for(File f : files){
                System.out.print(f.getName() + " ");
            }
            System.out.println();
        }
        else {
            System.out.println("Failed to list directory contents.");
        }
    }
    public void cp(String sourcePath, String destinationPath) {
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        if (!sourceFile.isAbsolute()) {
            // If sourceFile is relative to the current directory
            String currentDir = System.getProperty("user.dir");
            sourceFile = new File(currentDir, sourcePath);
        }

        if (!destinationFile.isAbsolute()) {
            // If destinationFile is relative to the current directory
            String currentDir = System.getProperty("user.dir");
            destinationFile = new File(currentDir, destinationPath);
        }

        if (sourceFile.exists()) {
            try {
                //convert file object to path to use it with Files.copy() method
                //handle the case if the destination file already exists
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
           if(!file.delete()){
               System.out.println("Error: File not deleted");
           }
        }
        else {
            System.err.println("Error: File '" + fileName + "' does not exist in the current directory.");
        }
    }
    public void cat(String[]fileNames) {
        if (fileNames.length == 1) {
            String fileName = fileNames[0];
            try {
                String currentDir = System.getProperty("user.dir");
                File file = new File(currentDir, fileName);

                if (file.exists()) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    System.out.println(content);
                } else {
                    System.err.println("Error: File '" + fileName + "' does not exist in the current directory.");
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else if(fileNames.length == 2){
            String fileName1 = fileNames[0];
            String fileName2 = fileNames[1];
            try {
                String currentDir = System.getProperty("user.dir");
                File file1 = new File(currentDir, fileName1);
                File file2 = new File(currentDir, fileName2);

                if (file1.exists() && file2.exists()) {
                    String content1 = new String(Files.readAllBytes(file1.toPath()));
                    String content2 = new String(Files.readAllBytes(file2.toPath()));
                    System.out.println(content1 + content2);
                } else {
                    System.err.println("Error: One or both of the specified files do not exist in the current directory.");
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        else{
            System.out.println("Error: command must have only one or two arguments with no space");
        }
    }

    //This method will choose the suitable command method to be called
    public void chooseCommandAction(String input){
        Parser parser = new Parser();
        if(parser.parse(input)){ // if the command is valid
            String commandName = parser.getCommandName();
            String[] arguments = parser.getArgs();
            switch (commandName) {
                case "echo" -> echo(arguments);
                case "ls" -> ls(arguments);
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
                case "pwd" -> pwd(arguments);
                case "mkdir" -> mkdir(arguments);
                case "cat" -> cat(arguments);
                case "ls -r" -> lsR(arguments);
                case "cp" ->
                {
                    if(arguments.length == 2) cp(arguments[0],arguments[1]);
                    else System.out.println("Error: command must have two arguments");
                }
                case "rm" ->
                {
                    if(arguments.length == 1) rm(arguments[0]);
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