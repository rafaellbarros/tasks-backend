package br.ce.wcaquino.taskbackend.utils;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class DateUtilsTest {

    @Test
    public void deveRetornarTrueParaDatasFuturas() {
        LocalDate date = LocalDate.of(2030, 01, 01);
        boolean expected = DateUtils.isEqualOrFutureDate(date);
        Assert.assertTrue(expected);
    }

    @Test
    public void deveRetornarFalseParaDatasFuturas() {
        LocalDate date = LocalDate.of(2010, 01, 01);
        boolean expected = DateUtils.isEqualOrFutureDate(date);
        Assert.assertFalse(expected);
    }

    @Test
    public void deveRetornarTrueParaDatasAtual() {
        LocalDate date = LocalDate.now();
        boolean expected = DateUtils.isEqualOrFutureDate(date);
        Assert.assertTrue(expected);
    }
}
