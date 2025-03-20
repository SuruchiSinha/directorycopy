

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class DirectoryContentsCopy {
    public void copyFiles(Path source, Path target) throws IOException {
        if (!Files.exists(source) || !Files.isDirectory(source)) {
            throw new IllegalArgumentException("Source must be an existing directory");
        }
        if (!Files.exists(target)) {
            Files.createDirectories(target);
        }

        try (Stream<Path> stream = Files.walk(source)) {
            stream.forEach(sourcePath -> {
                try {
                    Path targetPath = target.resolve(source.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath); // Create subdirectories
                    } else {
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error copying file: " + sourcePath, e);
                }
            });
        }
    }

    public static void main(String[] args) {
        try {
            if(args.length != 2) {
                throw new IllegalArgumentException("Invalid input.");
            }
            Path sourceDir = Paths.get(args[0]);
            Path targetDir = Paths.get(args[1]);
            DirectoryContentsCopy  directoryContentsCopy = new DirectoryContentsCopy();
            directoryContentsCopy.copyFiles(sourceDir, targetDir);
            System.out.println("Directory copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}