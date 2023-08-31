package com.app.batch.company.companypayment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark : 기업 카드빌링 Table Entity
 */
@Entity
@EqualsAndHashCode(of="cpiId")
@Data
@NoArgsConstructor
@Table(name="kn_company_payment")
public class CompanyPayment {

    @Id
    @Column(name = "cpi_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpiId;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "cpi_billing_key")
    private String cpiBillingKey;

    @Column(name = "cpi_billing_date")
    private LocalDateTime cpiBillingDate;

    @Column(name = "cpi_billing_expire_date")
    private LocalDateTime cpiBillingExpireDate;

    @Column(name = "cpi_receipt_id")
    private String cpiReceiptId;

    @Column(name = "cpi_subscription_id")
    private String cpiSubscriptionId;

    @Column(name = "cpi_valid_start")
    private LocalDate cpiValidStart;

    @Column(name = "cpi_pay_type")
    private String cpiPayType;

    @Column(name = "cpi_pay_amount")
    private Integer cpiPayAmount;

    @Column(name = "cpi_pay_expire_date")
    private LocalDateTime cpiPayExpireDate;

    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @Column(name = "modify_email")
    private String modify_email;

    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
