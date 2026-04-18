package com.sbi.lms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter @Setter
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String ifscCode;

    private String city;

    @OneToMany(mappedBy = "branch")
    @JsonIgnore   // prevent circular serialization
    private List<LoanApplication> applications;

    @Override
    public String toString() {
        return "Branch{id=" + id + ", ifsc=" + ifscCode + "}";
    }
}
