package com.app.batch.awskmshistory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(of = "akhIdx")
@Data
@NoArgsConstructor
@Table(name="kn_aws_kms_history")
public class AwsKmsHistory {

    @Id
    @Column(name = "akh_idx", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long akhIdx;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "akh_count")
    private Long akhCount;

    @Column(name = "akh_yyyymm")
    private String akhYyyymm;

    @Column(name = "modify_date")
    private LocalDate modify_date;

}
