package com.stats.dto

/**
 * Created by admin on 7/20/2017.
 */
class DateUtil {

    static today() {
        Calendar now = Calendar.getInstance()
        Calendar today = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
        return today.getTime()
    }

    static tommorow() {
        Calendar now = Calendar.getInstance()
        Calendar tommorow = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
        tommorow.add(Calendar.DAY_OF_MONTH, 1)
        return tommorow.getTime()
    }

    static yesterday() {
        Calendar now = Calendar.getInstance()
        Calendar tommorow = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
        tommorow.add(Calendar.DAY_OF_MONTH, -1)
        return tommorow.getTime()
    }
}
