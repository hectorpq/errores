//package com.example.jpc.student.service;
//import com.example.jpc.student.service.StudentService;
//import java.util.List;
//import java.util.Optional;
//
//
//import com.example.jpc.student.dto.StudentDTO;
//import java.util.List;
//
//public interface StudentService {
//    List<StudentDTO> getAllStudents();
//    StudentDTO getStudentById(Long id);
//    StudentDTO createStudent(StudentDTO studentDTO);
//    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
//    void deleteStudent(Long id);
//}


package com.example.ms.proveedor.service;

import com.example.ms.proveedor.dto.StudentDTO;
import com.example.ms.proveedor.entity.Proveedor;
import com.example.ms.proveedor.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Proveedor student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return convertToDTO(student);
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        if (studentRepository.existsByDocumentNumber(studentDTO.getDocumentNumber())) {
            throw new RuntimeException("Document number already exists");
        }
        Proveedor student = convertToEntity(studentDTO);
        return convertToDTO(studentRepository.save(student));
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Proveedor existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Check if document number already exists for a different student
        if (!existingStudent.getDocumentNumber().equals(studentDTO.getDocumentNumber()) &&
                studentRepository.existsByDocumentNumber(studentDTO.getDocumentNumber())) {
            throw new RuntimeException("Document number already exists");
        }

        existingStudent.setDocumentNumber(studentDTO.getDocumentNumber());
        existingStudent.setName(studentDTO.getName());
        existingStudent.setCareer(studentDTO.getCareer());
        existingStudent.setStatus(studentDTO.getStatus());
        existingStudent.setCurrentCycle(studentDTO.getCurrentCycle());

        return convertToDTO(studentRepository.save(existingStudent));
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    private StudentDTO convertToDTO(Proveedor student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setDocumentNumber(student.getDocumentNumber());
        dto.setName(student.getName());
        dto.setCareer(student.getCareer());
        dto.setStatus(student.getStatus());
        dto.setCurrentCycle(student.getCurrentCycle());
        return dto;
    }

    private Proveedor convertToEntity(StudentDTO dto) {
        Proveedor student = new Proveedor();
        student.setId(dto.getId());
        student.setDocumentNumber(dto.getDocumentNumber());
        student.setName(dto.getName());
        student.setCareer(dto.getCareer());
        student.setStatus(dto.getStatus());
        student.setCurrentCycle(dto.getCurrentCycle());
        return student;
    }

}