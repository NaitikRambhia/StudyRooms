package com.example.cs348project.controller;

import com.example.cs348project.model.Reservation;
import com.example.cs348project.repository.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReservationRepository reservations;

    public ReportController(ReservationRepository reservations) {
        this.reservations = reservations;
    }

    @GetMapping("/reservations")
    public String getReservations(@RequestParam String start,
                                  @RequestParam String end,
                                  Model model) {

        List<Reservation> list = reservations.getReservationsBetween(start, end);
        model.addAttribute("reservations", list);

        return "reservationsReport";
    }
    @GetMapping("/all")
    public String allReservations(Model model) {
        List<Reservation> list = reservations.getAllReservations();
        model.addAttribute("reservations", list);
        return "allReservations";
    }

    @PostMapping("/delete")
    public String deleteReservation(@RequestParam int id) {
        reservations.deleteReservation(id);
        return "redirect:/report/all?deleted=true";
    }

}
