package com.app.batch.payment.paymenterror;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-22
 * Time :
 * Remark : 결제시도 에러건 카운팅 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "peId")
@Data
@NoArgsConstructor
@Table(name="kn_payment_error")
public class PaymentError {

    @Id
    @Column(name = "pe_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long peId;

    @Column(name = "pay_id")
    private Long payId;

    @Column(name = "pe_state")
    private String peState;

    @Column(name = "pe_count")
    private Integer peCount;

    @Column(name = "insert_date")
    private LocalDateTime insert_date;

    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
