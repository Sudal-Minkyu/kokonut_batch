package com.app.batch.company.companypaymentinfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark : 카드빌링 정보 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "cpiInfoId")
@Data
@NoArgsConstructor
@Table(name="kn_company_payment_info")
public class CompanyPaymentInfo {

    @Id
    @Column(name = "cpi_info_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpiInfoId;

    @Column(name = "cpi_id")
    private Long cpiId;

    @Column(name = "cpi_info_card_name")
    private String cpiInfoCardName;

    @Column(name = "cpi_info_card_no")
    private String cpiInfoCardNo;

    @Column(name = "cpi_info_card_type")
    private String cpiInfoCardType;

    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @Column(name = "modify_email")
    private String modify_email;

    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
