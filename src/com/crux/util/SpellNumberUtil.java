/**
 * Created by IntelliJ IDEA.
 * User: dono
 * Date: Jan 17, 2005
 * Time: 3:38:31 PM
 * To change this template use Options | File Templates.
 */
package com.crux.util;

public class SpellNumberUtil {
    public static String readNumber(String number) {
        number = formattingNumber(number);
        String centSpell = "";

        if (number.indexOf(".") > -1) {
            number = number.substring(0, number.indexOf("."));
            centSpell = centSpell(number);
        }

        int lengthNumber = number == null ? 0 : number.length();
        double lengthCatDigit = lengthNumber / 3;
        long newCatDigit = java.lang.Math.round(lengthCatDigit) + 1;
        String spell = "";
        String tmpspell = "";

        for (long i = 0; i < newCatDigit; i++) {
            if (number != null) {
                String tmpNumber = "";
                if (number.length() >= 3) {
                    tmpNumber = number.substring(lengthNumber - 3);
                    number = number.substring(0, lengthNumber - 3);
                    lengthNumber = number.length();
                    tmpspell = readNumberThreeDigit(tmpNumber);
                    System.out.println("track1 : " + tmpNumber + " | " + number + " | " + tmpspell);
                } else if (number.length() == 2) {
                    tmpspell = readNumberTwoDigit(number);
                    tmpNumber = number;
                    number = "";
                    System.out.println("track2 : " + tmpNumber + " | " + tmpspell);
                } else if (number.length() == 1) {
                    tmpspell = readNumberOneDigit(number);
                    tmpNumber = number;
                    number = "";
                    System.out.println("track3 : " + tmpNumber + " | " + tmpspell);
                }

                if (i == 0) {
                    spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + spell);
                } else if (i == 1) {
                    spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " RIBU " + spell);
                } else if (i == 2) {
                    spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " JUTA " + spell);
                } else if (i == 3) {
                    spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " MILYAR " + spell);
                } else if (i == 4) {
                    spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " TRILYUN " + spell);
                }
            }
            System.out.println(i + " spelllll: " + spell);
        }
        spell = spell + " RUPIAH " + centSpell;
        return spell;
    }

    private static String readNumberOneDigit(String stOneDigitNumber) {
        if ("1".equals(stOneDigitNumber)) {
            return "SATU";
        } else if ("2".equals(stOneDigitNumber)) {
            return "DUA";
        } else if ("3".equals(stOneDigitNumber)) {
            return "TIGA";
        } else if ("4".equals(stOneDigitNumber)) {
            return "EMPAT";
        } else if ("5".equals(stOneDigitNumber)) {
            return "LIMA";
        } else if ("6".equals(stOneDigitNumber)) {
            return "ENAM";
        } else if ("7".equals(stOneDigitNumber)) {
            return "TUJUH";
        } else if ("8".equals(stOneDigitNumber)) {
            return "DELAPAN";
        } else if ("9".equals(stOneDigitNumber)) {
            return "SEMBILAN";
        }

        return "";
    }

    private static String readNumberTwoDigit(String stTwoDigitNumber) {
        if (stTwoDigitNumber == null || stTwoDigitNumber.length() > 2) {
            return "";
        } else {
            String firstTwoDigit = stTwoDigitNumber.substring(0, 1);
            String secondTwoDigit = stTwoDigitNumber.substring(1);
            if ("0".equals(firstTwoDigit)) {
                return readNumberOneDigit(secondTwoDigit);
            } else if ("1".equals(firstTwoDigit)) {
                if ("10".equals(stTwoDigitNumber)) {
                    return "SEPULUH";
                } else if ("11".equals(stTwoDigitNumber)) {
                    return "SEBELAS";
                } else if ("12".equals(stTwoDigitNumber)) {
                    return "DUA BELAS";
                } else if ("13".equals(stTwoDigitNumber)) {
                    return "TIGA BELAS";
                } else if ("14".equals(stTwoDigitNumber)) {
                    return "EMPAT BELAS";
                } else if ("15".equals(stTwoDigitNumber)) {
                    return "LIMA BELAS";
                } else if ("16".equals(stTwoDigitNumber)) {
                    return "ENAM BELAS";
                } else if ("17".equals(stTwoDigitNumber)) {
                    return "TUJUH BELAS";
                } else if ("18".equals(stTwoDigitNumber)) {
                    return "DELAPAN BELAS";
                } else if ("19".equals(stTwoDigitNumber)) {
                    return "SEMBILAN BELAS";
                }
            } else {
                return readNumberOneDigit(firstTwoDigit) + " PULUH " + readNumberOneDigit(secondTwoDigit);
            }
        }
        return "";
    }

    private static String readNumberThreeDigit(String stThreeDigitNumber) {
        if (stThreeDigitNumber == null || stThreeDigitNumber.length() > 3) {
            return "";
        } else {
            String firstDigit = stThreeDigitNumber.substring(0, 1);
            String secondTwoDigit = stThreeDigitNumber.substring(1);
            if ("0".equals(firstDigit)) {
                return readNumberTwoDigit(secondTwoDigit);
            } else if ("1".equals(firstDigit)) {
                return "SERATUS " + readNumberTwoDigit(secondTwoDigit);
            } else {
                return readNumberOneDigit(firstDigit) + " RATUS " + readNumberTwoDigit(secondTwoDigit);
            }
        }
    }

    private static boolean isAllDigitZero(String number) {
        if (number != null) {
            if (number.length() == 0) {
                return true;
            } else {
                if (number.length() == 1) {
                    if ("0".equals(number)) {
                        return true;
                    }
                } else if (number.length() == 2) {
                    if ("00".equals(number)) {
                        return true;
                    }
                } else if (number.length() == 3) {
                    if ("000".equals(number)) {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private static String formattingNumber(String number) {
        if (number != null) {
            //remove all comma in number
            number = number.replaceAll(",", "");

            //formatting number with maximum 2 digit number after period
            int idxLastPeriod = number.lastIndexOf(".");
            String digitNumberNumerik = "";
            String digitNumberCent = "";

            if (idxLastPeriod == -1) {
                digitNumberNumerik = number;
                digitNumberCent = null;
                return digitNumberNumerik;
            } else {
                digitNumberNumerik = number.substring(0, idxLastPeriod);
                digitNumberCent = number.substring(idxLastPeriod + 1);
            }
            if (digitNumberCent != null) {
                if (digitNumberCent.length() > 2) {
                    digitNumberCent = digitNumberCent.substring(0, 2);
                }
            }

            return digitNumberNumerik + "." + digitNumberCent;

        }
        return null;
    }

    private static String centSpell(String number) {
        String cent = number.substring(number.indexOf(".") + 1);
        return readNumberTwoDigit(cent) + " SEN ";
    }
}
