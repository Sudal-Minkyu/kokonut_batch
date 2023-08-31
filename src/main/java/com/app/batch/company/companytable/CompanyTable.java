package com.app.batch.company.companytable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ctId")
@Data
@NoArgsConstructor
@Table(name="kn_company_table")
public class CompanyTable {

    @Id
    @Column(name = "ct_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ctId;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "ct_name")
    private String ctName;

    @Column(name = "ct_table_count")
    private String ctTableCount;

    @Column(name = "ct_designation")
    private String ctDesignation;

    @Column(name = "ct_add_column_count")
    private Integer ctAddColumnCount;

    @Column(name = "ct_add_column_security_count")
    private Integer ctAddColumnSecurityCount;

    @Column(name = "ct_add_column_unique_count")
    private Integer ctAddColumnUniqueCount;

    @Column(name = "ct_add_column_sensitive_count")
    private Integer ctAddColumnSensitiveCount;

    @Column(name = "ct_name_status")
    private String ctNameStatus;

    @Column(name = "ct_phone_status")
    private String ctPhoneStatus;

    @Column(name = "ct_gender_status")
    private String ctGenderStatus;

    @Column(name = "ct_email_status")
    private String ctEmailStatus;

    @Column(name = "ct_birth_status")
    private String ctBirthStatus;

    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @Column(name = "modify_email")
    private String modify_email;

    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
