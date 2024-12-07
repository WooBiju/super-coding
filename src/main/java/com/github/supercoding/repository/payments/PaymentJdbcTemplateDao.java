package com.github.supercoding.repository.payments;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentJdbcTemplateDao implements PaymentRepository{

    private JdbcTemplate jdbcTemplate;

    public PaymentJdbcTemplateDao(@Qualifier("jdbcTemplate2") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean savePayment(Payment paymentNew) {
        Integer rowNums = jdbcTemplate.update("INSERT INTO payment(passenger_id,reservation_id,pay_at) VALUES (?,?,?)",
                paymentNew.getPassengerId(),paymentNew.getReservationId(),paymentNew.getPayTime() );
        return rowNums > 0;
    }
}
