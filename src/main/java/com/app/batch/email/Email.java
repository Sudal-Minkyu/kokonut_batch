package com.app.batch.email;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "emId")
@Data
@NoArgsConstructor
@Table(name="kn_email")
public class Email {

    @Id
    @Column(name = "em_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emId;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "em_type")
    private String emType;

    @Column(name = "em_reservation_date")
    private LocalDateTime emReservationDate;

    @Column(name = "em_receiver_type")
    private String emReceiverType;

    @Column(name = "em_purpose")
    private String emPurpose;

    @Column(name = "em_etc")
    private String emEtc;

    @Column(name = "em_email_send")
    private String emEmailSend;

    @Column(name = "em_title")
    private String emTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT", name="em_contents")
    private String emContents;

    @Column(name = "em_state")
    private String emState;

    @Column(name = "em_request_id")
    private String emRequestId;

    @Column(name = "em_send_all_count")
    private Integer emSendAllCount;

    @Column(name = "em_send_suc_count")
    private Integer emSendSucCount;

    @Column(name = "em_send_fail_count")
    private Integer emSendFailCount;

    @Column(name = "em_yyyymm")
    private String emYyyymm;

    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @Column(name = "modify_email")
    private String modify_email;

    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
