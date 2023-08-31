package com.app.batch.payment.paymentprivacycount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-06-15
 * Time :
 * Remark : 일일 개인정보 수 카운팅 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "ppcId")
@Data
@NoArgsConstructor
@Table(name="kn_payment_privacy_count")
public class PaymentPrivacyCount {

    @Id
    @Column(name = "ppc_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ppcId;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "ct_name")
    private String ctName;

    @Column(name = "ppc_count")
    private Integer ppcCount;

    @Column(name = "ppc_date")
    private LocalDate ppcDate;

}
