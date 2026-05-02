package com.example.cs348project.controller;

import com.example.cs348project.repository.RoomRepository;
import com.example.cs348project.repository.ReservationRepository;
import com.example.cs348project.model.StudyRoom;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.cs348project.model.Reservation;


import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final RoomRepository rooms;
    private final ReservationRepository reservations;

    public StudentController(RoomRepository rooms, ReservationRepository reservations) {
        this.rooms = rooms;
        this.reservations = reservations;
    }

    @PostMapping("/reserve")
    @ResponseBody
    public String reserve(@RequestParam int roomId,
                          @RequestParam String studentName,
                          @RequestParam String start,
                          @RequestParam String end) {

        boolean ok = reservations.reserve(roomId, studentName, start, end);

        if (!ok) {
            return "Reservation failed: time conflict";
        }

        return "Reservation created";
    }


    // HTML table view for students
    @GetMapping("/viewRooms")
    public String viewRooms(Model model) {
        model.addAttribute("rooms", rooms.getAllRooms());
        return "studentRooms"; // studentRooms.html
    }

    @GetMapping("/roomReservations")
    public String roomReservations(@RequestParam int roomId, Model model) {
        List<Reservation> list = reservations.findByRoom(roomId);
        model.addAttribute("reservations", list);
        model.addAttribute("roomId", roomId);
        return "roomReservations"; // roomReservations.html
    }

}
