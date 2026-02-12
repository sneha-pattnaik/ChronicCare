package com.example.chroniccare.api;

import java.util.List;

public class BookingSlotsResponse {
    private List<Slot> slots;
    private String specialty;

    public List<Slot> getSlots() {
        return slots;
    }

    public String getSpecialty() {
        return specialty;
    }

    public static class Slot {
        private String date;
        private String time;
        private String doctor;
        private boolean available;

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getDoctor() {
            return doctor;
        }

        public boolean isAvailable() {
            return available;
        }
    }
}
