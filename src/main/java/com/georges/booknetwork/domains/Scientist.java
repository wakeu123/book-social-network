package com.georges.booknetwork.domains;

import com.georges.booknetwork.domains.base.MainObject;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity(name = "Scientist")
@Table(name = "BCP_SCIENTISTS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scientist extends MainObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scientist_seq")
    @SequenceGenerator(name = "scientist_seq", sequenceName = "scientist_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    private String photoUrl;

    @Override
    public Scientist copy() {
        return null;
    }
}
