import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) {
        FileWithReport src = new FileWithReport("/home/mikhail/Games/src");
        FileWithReport res = new FileWithReport("/home/mikhail/Games/res");
        FileWithReport savegames = new FileWithReport("/home/mikhail/Games/savegames");
        FileWithReport temp = new FileWithReport("/home/mikhail/Games/temp");
        FileWithReport main = new FileWithReport("/home/mikhail/Games/src/main");
        FileWithReport test = new FileWithReport("/home/mikhail/Games/src/test");
        FileWithReport drawables = new FileWithReport("/home/mikhail/Games/res/drawables");
        FileWithReport vectors = new FileWithReport("/home/mikhail/Games/res/vectors");
        FileWithReport icons = new FileWithReport("/home/mikhail/Games/res/icons");
        FileWithReport fileMain = new FileWithReport("/home/mikhail/Games/src/main/Main.java");
        FileWithReport fileUtils = new FileWithReport("/home/mikhail/Games/src/main/Utils.java");
        if (src.mkdir() &&
                res.mkdir() &&
                savegames.mkdir() &&
                temp.mkdir() &&
                main.mkdir() &&
                test.mkdir() &&
                drawables.mkdir() &&
                vectors.mkdir() &&
                icons.mkdir() &&
                fileMain.createNewFile() &&
                fileUtils.createNewFile()
        ) {
            System.out.println("Установка успешно выполнена!");
        }
        GameProgress one = new GameProgress(80, 20, 1, 125.5);
        GameProgress two = new GameProgress(180, 42, 2, 893.67);
        GameProgress three = new GameProgress(210, 68, 3, 2145.17);
        GameProgress.saveGame("/home/mikhail/Games/savegames/save1.dat", one);
        GameProgress.saveGame("/home/mikhail/Games/savegames/save2.dat", two);
        GameProgress.saveGame("/home/mikhail/Games/savegames/save3.dat", three);
        zipFiles("/home/mikhail/Games/savegames/");
        openZip("/home/mikhail/Games/savegames/zip_output.zip", "/home/mikhail/Games/savegames/");
        System.out.println(openProgress("/home/mikhail/Games/savegames/save1.dat"));

    }

    public static void zipFiles(String path) {
        List<Path> list;
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            list = paths
                    .filter(Files::isRegularFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path + "/zip_output.zip"))) {
            for (Path s : list) {
                String filepath = s.toString();
                try (FileInputStream fis = new FileInputStream(filepath)) {
                    ZipEntry entry = new ZipEntry(s.getFileName().toString());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    deleteFile(filepath);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public static void openZip(String filesPath, String directoryPath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(filesPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(directoryPath + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}