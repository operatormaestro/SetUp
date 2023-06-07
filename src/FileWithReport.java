import java.io.*;

public class FileWithReport extends File {
    public String pathname;
    public static File reportFile = new File("/home/mikhail/Games/temp.txt");
    public static int count = 0;


    public FileWithReport(String pathname) {
        super(pathname);
        this.pathname = pathname;
    }

    @Override
    public boolean mkdir() {
        String report = "Успешно создана папка " + this.pathname;
        try (FileWriter fileWriter = new FileWriter(reportFile, true)) {
            fileWriter.write(++count + ". " + report + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return super.mkdir();
    }

    @Override
    public boolean createNewFile() {
        String report = "Успешно создан файл " + this.pathname;
        try (FileWriter fileWriter = new FileWriter(reportFile, true)) {
            fileWriter.write(++count + ". " + report + "\n");
            return super.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
