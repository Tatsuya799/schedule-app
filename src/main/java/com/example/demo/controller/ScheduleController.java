package com.example.demo.controller;

import com.example.demo.model.Schedule;
import com.example.demo.repository.ScheduleRepository;
import org.springframework.stereotype.Controller; // RestControllerから変更
import org.springframework.ui.Model; // データを画面に渡すために必要
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller // 画面を表示するために @Controller に変更
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 【追加】ブラウザで「/」にアクセスした時に index.html を表示する
     */
    @GetMapping({"/","/index.html"})
    public String index(Model model) {
        // データベースから全データを取得
        List<Schedule> schedules = scheduleRepository.findAll();
        // "schedules" という名前でHTML側にデータを渡す
        model.addAttribute("schedules", schedules);
        // templates/index.html を探して表示する
        return "index.html";
    }

    // --- 以下、データ操作用のメソッドは @ResponseBody を付けて維持します ---

    // 予定一覧をデータ（JSON）として取得
    @GetMapping("/schedules")
    @ResponseBody
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // 予定を追加
    @PostMapping("/schedules")
    @ResponseBody
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // 予定を編集
    @PutMapping("/schedules/{id}")
    @ResponseBody
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleDetails) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        schedule.setDate(scheduleDetails.getDate());
        schedule.setState(scheduleDetails.getState());
        schedule.setName(scheduleDetails.getName());
        schedule.setColor(scheduleDetails.getColor());

        return scheduleRepository.save(schedule);
    }

    // 予定を削除
    @DeleteMapping("/schedules/{id}")
    @ResponseBody
    public String deleteSchedule(@PathVariable Long id) {
        scheduleRepository.deleteById(id);
        return "Schedule deleted successfully!";
    }
}