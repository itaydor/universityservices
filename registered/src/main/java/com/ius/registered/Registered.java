package com.ius.registered;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Registered {

    @Id
    @SequenceGenerator(
            name = "registered_id_sequence",
            sequenceName = "registered_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "registered_id_sequence"
    )
    private Integer id;
    private Integer studentID;
    private Integer curseID;
}
