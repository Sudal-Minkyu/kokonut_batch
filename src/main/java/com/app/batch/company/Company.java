package com.app.batch.company;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-12-22
 * Time :
 * Remark : 기업 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "companyId")
@Data
@NoArgsConstructor
@Table(name="kn_company")
public class Company {

    @Id
    @Column(name = "company_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "cp_name")
    private String cpName;

    @Column(name = "cp_table_count")
    private Integer cpTableCount;

    @Column(name = "cp_electronic")
    private Integer cpElectronic;

    @Column(name = "cp_electronic_date")
    private LocalDate cpElectronicDate;

    @Column(name = "cpi_id")
    private Long cpiId;

    @Column(name = "cp_subscribe")
    private String cpSubscribe;

    @Column(name = "cp_valid_start")
    private LocalDate cpValidStart;

    @Column(name = "cp_subscribe_date")
    private LocalDateTime cpSubscribeDate;

    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @Column(name = "modify_email")
    private String modify_email;

    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
