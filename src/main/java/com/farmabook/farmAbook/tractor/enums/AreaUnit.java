package com.farmabook.farmAbook.tractor.enums;

public enum AreaUnit {

    ACRE(1.0),
    HECTARE(2.47105),
    GUNTA(0.025),
    BIGHA(0.625);

    private final double toAcreFactor;

    AreaUnit(double toAcreFactor) {
        this.toAcreFactor = toAcreFactor;
    }

    public double toAcres(double value) {
        return value * toAcreFactor;
    }
}
