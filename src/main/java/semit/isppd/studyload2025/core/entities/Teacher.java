package semit.isppd.studyload2025.core.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String fullName;
    private String posada;
    private String naukStupin;
    private String vchZvana;
    private String stavka;
    private String note;
    private String emailAddress;
    private String emailedDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_hours_id")
    private TeacherHours teacherHours = new TeacherHours();

    @OneToMany(mappedBy = "teacher")
    private Set<StudyloadRow> studyloadRows = new HashSet<>();

}
