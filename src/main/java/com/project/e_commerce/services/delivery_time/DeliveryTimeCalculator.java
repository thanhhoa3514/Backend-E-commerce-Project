package com.project.e_commerce.services.delivery_time;


import com.project.e_commerce.models.DeliveryTimeRange;
import com.project.e_commerce.models.enums.ShippingMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DeliveryTimeCalculator {

    public DeliveryTimeRange calculateDeliveryTime(String shippingMethod, String address) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Lấy thông tin shipping method
        ShippingMethod method = ShippingMethod.valueOf(shippingMethod.toUpperCase());

        // Tính ngày bắt đầu (từ ngày)
        calendar.add(Calendar.DAY_OF_MONTH, method.getMinDays());
        Date fromDate = calendar.getTime();

        // Tính ngày kết thúc (đến ngày)
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, method.getMaxDays());
        Date toDate = calendar.getTime();

        return new DeliveryTimeRange(fromDate, toDate);
    }
}
