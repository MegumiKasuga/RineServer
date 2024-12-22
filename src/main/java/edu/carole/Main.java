package edu.carole;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Random;
import java.util.Scanner;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Server server;
    public static final Random random = new Random();

    public static void main(String[] args) {
        Config config = new Config(gson, "server_properties.json");
        Server s = new Server(config);
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("exit")) {
                System.out.println("Goodbye!");
                server.closeSocket();
                break;
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}