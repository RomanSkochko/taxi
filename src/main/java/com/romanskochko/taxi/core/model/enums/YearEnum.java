package com.romanskochko.taxi.core.model.enums;

public enum YearEnum {
    Y1990, Y1991, Y1992, Y1993, Y1994, Y1995, Y1996, Y1997, Y1998, Y1999,
    Y2000, Y2001, Y2002, Y2003, Y2004, Y2005, Y2006, Y2007, Y2008, Y2009,
    Y2010, Y2011, Y2012, Y2013, Y2014, Y2015, Y2016, Y2017, Y2018, Y2019,
    Y2020, Y2021, Y2022, Y2023, Y2024, Y2025;

    public int toYear() {
        return Integer.parseInt(this.name().substring(1));
    }
}
