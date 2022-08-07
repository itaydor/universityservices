package com.ius.curse;

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
public class Curse {

    @Id
    @SequenceGenerator(
            name = "curse_id_sequence",
            sequenceName = "curse_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "curse_id_sequence"
    )
    private Integer curseID;
    private String name;

}
