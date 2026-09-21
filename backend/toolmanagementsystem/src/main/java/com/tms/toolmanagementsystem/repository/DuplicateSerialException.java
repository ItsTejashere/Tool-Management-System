package com.tms.toolmanagementsystem.repository;

public class DuplicateSerialException extends RuntimeException {
    public DuplicateSerialException() {
        super("Serial number already exists. Please enter a different serial number.");
    }
}