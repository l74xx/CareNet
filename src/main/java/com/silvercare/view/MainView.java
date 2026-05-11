package com.silvercare.view;

import com.silvercare.model.User;
import java.util.Scanner;

public class MainView {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthView authView = new AuthView(scanner);
    private User currentUser;

    public void start() {
        while (currentUser == null) {
            printBanner();
            System.out.println("\n[1] 登入  [2] 註冊  [0] 離開");
            System.out.print("請選擇：");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    authView.login();
                    break;
                case "2":
                    authView.register();
                    break;
                case "0":
                    System.out.println("感謝使用，再見！");
                    break;
                default:
                    System.out.println("請輸入有效選項。");
            }
        }
    }

    private void printBanner() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          📋 期末專題模板系統 📋        ║");
        System.out.println("║      CareNet - 銀髮健康陪伴社區系統    ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
}
