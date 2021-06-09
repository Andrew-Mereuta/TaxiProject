package taxi.project.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.entities.Student;
import taxi.project.demo.repositories.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class SimpleController {

    private StudentRepository studentRepository;

    @Autowired
    public SimpleController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public void createStudent(@RequestBody Student student) {
        studentRepository.save(student);
    }

    @GetMapping("/allStudents")
    public List<Student> allStudents() {
        List<Student> students = studentRepository.findAll();
        return students;
    }
}
