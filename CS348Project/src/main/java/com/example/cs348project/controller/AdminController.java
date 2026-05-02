package com.example.cs348project.controller;

import com.example.cs348project.repository.RoomRepository;
import com.example.cs348project.model.StudyRoom;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RoomRepository rooms;

    public AdminController(RoomRepository rooms) {
        this.rooms = rooms;
    }

    @PostMapping("/addRoom")
    public String addRoom(@RequestParam int roomId,
                          @RequestParam String name,
                          @RequestParam int capacity) {

        boolean added = rooms.addRoom(roomId, name, capacity);

        if (!added) {
            return "redirect:/adminPage?addError=true";
        }

        return "redirect:/adminPage?added=true";
    }



    @PostMapping("/deleteRoom")
    public String deleteRoom(@RequestParam int id) {

        String result = rooms.deleteRoom(id);

        return switch (result) {
            case "NOT_FOUND" -> "redirect:/adminPage?deleteNotFound=true";
            case "HAS_RESERVATIONS" -> "redirect:/adminPage?deleteError=true";
            case "DELETED" -> "redirect:/adminPage?deleted=true";
            default -> "redirect:/adminPage";
        };

    }

    @GetMapping("/viewRooms")
    public String viewRooms(Model model) {
        List<StudyRoom> allRooms = rooms.getAllRooms();
        model.addAttribute("rooms", allRooms);
        return "viewRooms";
    }


    @PostMapping("/editRoom")
    public String editRoom(@RequestParam int roomId,
                           @RequestParam String name,
                           @RequestParam int capacity) {

        boolean edited = rooms.editRoom(roomId, name, capacity);

        if (!edited) {
            return "redirect:/adminPage?editError=true";
        }

        return "redirect:/adminPage?edited=true";
    }

}
