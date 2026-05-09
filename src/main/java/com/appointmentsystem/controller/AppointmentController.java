package com.appointmentsystem.controller;

import com.appointmentsystem.model.AvailabilitySlot;
import com.appointmentsystem.model.AppointmentHistoryEntry;
import com.appointmentsystem.repository.AppointmentRepository;
import com.appointmentsystem.service.AppointmentManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentManagementService appointmentManagementService;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                  AppointmentManagementService appointmentManagementService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentManagementService = appointmentManagementService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        List<AvailabilitySlot> slots = appointmentRepository.findAllSlots();
        Map<Long, String> providers = tutorNames();

        model.addAttribute("slots", slots);
        model.addAttribute("providers", providers);
        return "appointments";
    }

    @GetMapping("/slots/new")
    public String newSlotForm(Model model) {
        model.addAttribute("providers", Map.of(1L, "John Smith", 2L, "Jane Lee", 3L, "Michael Chen"));
        model.addAttribute("subjects", subjectOptions());
        return "add-slot";
    }

    @PostMapping("/slots")
    public String createSlot(@RequestParam long providerId,
                             @RequestParam String subject,
                             @RequestParam String startTime,
                             @RequestParam String endTime) {
        appointmentRepository.insertSlot(providerId, subject, startTime, endTime);
        return "redirect:/appointments";
    }

    @GetMapping("/history")
    public String appointmentHistory(Model model) {
        List<AppointmentHistoryEntry> appointments = appointmentManagementService.findHistory();
        Map<Long, String> providers = tutorNames();

        model.addAttribute("appointments", appointments);
        model.addAttribute("providers", providers);
        return "history";
    }

    @PostMapping("/appointments/{appointmentId}/cancel")
    public String cancelAppointment(@PathVariable long appointmentId) {
        appointmentManagementService.cancelAppointment(appointmentId);
        return "redirect:/history";
    }

    @GetMapping("/book")
    public String bookForm() {
        return "book";
    }

    @PostMapping("/book")
    public String submitBook(@RequestParam String studentName,
                             @RequestParam String appointmentSlot,
                             @RequestParam String subject,
                             Model model) {
        model.addAttribute("studentName", studentName);
        model.addAttribute("appointmentSlot", appointmentSlot);
        model.addAttribute("subject", subject);
        return "confirmation";
    }

    private Map<Long, String> tutorNames() {
        Map<Long, String> providers = new HashMap<>();
        providers.put(1L, "John Smith");
        providers.put(2L, "Jane Lee");
        providers.put(3L, "Michael Chen");
        return providers;
    }

    private Map<String, String> subjectOptions() {
        Map<String, String> subjects = new HashMap<>();
        subjects.put("Mathematics", "Mathematics");
        subjects.put("Physics", "Physics");
        subjects.put("Computer Science", "Computer Science");
        subjects.put("English", "English");
        subjects.put("Chemistry", "Chemistry");
        return subjects;
    }

}
