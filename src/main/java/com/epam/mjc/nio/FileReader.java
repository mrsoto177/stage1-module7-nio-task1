package com.epam.mjc.nio;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.StandardOpenOption;


public class FileReader {
	public Profile getDataFromFile(File file) {
        Profile profile = new Profile();

        // Path to the file
        Path path = file.toPath();

        // Ensure the file exists
        if (!Files.exists(path)) {
            System.out.println("File does not exist");
            return profile;
        }

        try (FileChannel inChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate((int) Files.size(path));
            inChannel.read(buffer);
            buffer.flip();

            StringBuilder sb = new StringBuilder();
            while (buffer.hasRemaining()) {
                sb.append((char) buffer.get());
            }

            String fileContent = sb.toString();
            parseProfileData(fileContent, profile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return profile;
    }

    private void parseProfileData(String data, Profile profile) {
        String[] lines = data.split("\n");

        for (String line : lines) {
            if (line.startsWith("Name: ")) {
                profile.setName(line.substring(6).trim());
            } else if (line.startsWith("Age: ")) {
                profile.setAge(Integer.parseInt(line.substring(5).trim()));
            } else if (line.startsWith("Email: ")) {
                profile.setEmail(line.substring(7).trim());
            } else if (line.startsWith("Phone: ")) {
                profile.setPhone(Long.parseLong(line.substring(7).trim()));
            }
        }
    }

    public static void main(String[] args) {
        File file = new File("src/main/resources/Profile.txt");
        FileReader fileReader = new FileReader();
        Profile profile = fileReader.getDataFromFile(file);
        System.out.println(profile);
    }
}


  

