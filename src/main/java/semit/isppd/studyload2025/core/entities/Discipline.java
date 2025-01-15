package semit.isppd.studyload2025.core.entities;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="discipline")
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String shortName;

    @OneToMany(mappedBy = "discipline")
    private Set<StudyloadRow> studyloadRows = new HashSet<>();

}