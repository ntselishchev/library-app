package com.ntselishchev.libraryapp.service;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class InOutServiceImpl implements InOutService {

    private final Scanner scanner;

    public InOutServiceImpl() {
        scanner = new Scanner(System.in);
    }

    public void print(String message) {
        System.out.println(message);
    }

    public String getUserInputMessage() {
        return scanner.nextLine().trim();
    }

    public int getUserIntInputMessage() {
        try {
            return Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e) {
            print("Not a number");
        }
        return getUserIntInputMessage();
    }
}
