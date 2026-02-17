package com.farmabook.farmAbook.tractor.service.impl;

import com.farmabook.farmAbook.tractor.service.DieselRateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

    @Service
    public class DieselRateServiceImpl implements DieselRateService {

        @Override
        public BigDecimal getDieselRatePerLitre() {
            return BigDecimal.valueOf(92.50);
        }
    }
