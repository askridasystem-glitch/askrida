<html>

<head>
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>DecimalFormat is a concrete subclass of NumberFormat that formats decimal 
numbers</title>
</head>

<body>

<p><code>DecimalFormat</code> is a concrete subclass of <code>NumberFormat</code> 
that formats decimal numbers. It has a variety of features designed to make it 
possible to parse and format numbers in any locale, including support for 
Western, Arabic, and Indic digits. It also supports different kinds of numbers, 
including integers (123), fixed-point numbers (123.4), scientific notation 
(1.23E4), percentages (12%), and currency amounts ($123). All of these can be 
localized. </p>
<p>To obtain a <code>NumberFormat</code> for a specific locale, including the 
default locale, call one of <code>NumberFormat</code>'s factory methods, such as
<code>getInstance()</code>. In general, do not call the <code>DecimalFormat</code> 
constructors directly, since the <code>NumberFormat</code> factory methods may 
return subclasses other than <code>DecimalFormat</code>. If you need to 
customize the format object, do something like this: </p>
<blockquote>
  <pre> NumberFormat f = NumberFormat.getInstance(loc);
 if (f instanceof DecimalFormat) {
     ((DecimalFormat) f).setDecimalSeparatorAlwaysShown(true);
 }
 </pre>
</blockquote>
<p>A <code>DecimalFormat</code> comprises a <em>pattern</em> and a set of <em>
symbols</em>. The pattern may be set directly using <code>applyPattern()</code>, 
or indirectly using the API methods. The symbols are stored in a <code>
DecimalFormatSymbols</code> object. When using the <code>NumberFormat</code> 
factory methods, the pattern and symbols are read from localized <code>
ResourceBundle</code>s. </p>
<h4>Patterns</h4>
<p><code>DecimalFormat</code> patterns have the following syntax: </p>
<blockquote>
  <pre> <i>Pattern:</i>
         <i>PositivePattern</i>
         <i>PositivePattern</i> ; <i>NegativePattern</i>
 <i>PositivePattern:</i>
         <i>Prefix<sub>opt</sub></i> <i>Number</i> <i>Suffix<sub>opt</sub></i>
 <i>NegativePattern:</i>
         <i>Prefix<sub>opt</sub></i> <i>Number</i> <i>Suffix<sub>opt</sub></i>
 <i>Prefix:</i>
         any Unicode characters except \uFFFE, \uFFFF, and special characters
 <i>Suffix:</i>
         any Unicode characters except \uFFFE, \uFFFF, and special characters
 <i>Number:</i>
         <i>Integer</i> <i>Exponent<sub>opt</sub></i>
         <i>Integer</i> . <i>Fraction</i> <i>Exponent<sub>opt</sub></i>
 <i>Integer:</i>
         <i>MinimumInteger</i>
         #
         # <i>Integer</i>
         # , <i>Integer</i>
 <i>MinimumInteger:</i>
         0
         0 <i>MinimumInteger</i>
         0 , <i>MinimumInteger</i>
 <i>Fraction:</i>
         <i>MinimumFraction<sub>opt</sub></i> <i>OptionalFraction<sub>opt</sub></i>
 <i>MinimumFraction:</i>
         0 <i>MinimumFraction<sub>opt</sub></i>
 <i>OptionalFraction:</i>
         # <i>OptionalFraction<sub>opt</sub></i>
 <i>Exponent:</i>
         E <i>MinimumExponent</i>
 <i>MinimumExponent:</i>
         0 <i>MinimumExponent<sub>opt</sub></i>
 </pre>
</blockquote>
<p>A <code>DecimalFormat</code> pattern contains a positive and negative 
subpattern, for example, <code>&quot;#,##0.00;(#,##0.00)&quot;</code>. Each subpattern has 
a prefix, numeric part, and suffix. The negative subpattern is optional; if 
absent, then the positive subpattern prefixed with the localized minus sign 
(code&gt;'-' in most locales) is used as the negative subpattern. That is, <code>
&quot;0.00&quot;</code> alone is equivalent to <code>&quot;0.00;-0.00&quot;</code>. If there is an 
explicit negative subpattern, it serves only to specify the negative prefix and 
suffix; the number of digits, minimal digits, and other characteristics are all 
the same as the positive pattern. That means that <code>&quot;#,##0.0#;(#)&quot;</code> 
produces precisely the same behavior as <code>&quot;#,##0.0#;(#,##0.0#)&quot;</code>. </p>
<p>The prefixes, suffixes, and various symbols used for infinity, digits, 
thousands separators, decimal separators, etc. may be set to arbitrary values, 
and they will appear properly during formatting. However, care must be taken 
that the symbols and strings do not conflict, or parsing will be unreliable. For 
example, either the positive and negative prefixes or the suffixes must be 
distinct for <code>DecimalFormat.parse()</code> to be able to distinguish 
positive from negative values. (If they are identical, then <code>DecimalFormat</code> 
will behave as if no negative subpattern was specified.) Another example is that 
the decimal separator and thousands separator should be distinct characters, or 
parsing will be impossible. </p>
<p>The grouping separator is commonly used for thousands, but in some countries 
it separates ten-thousands. The grouping size is a constant number of digits 
between the grouping characters, such as 3 for 100,000,000 or 4 for 1,0000,0000. 
If you supply a pattern with multiple grouping characters, the interval between 
the last one and the end of the integer is the one that is used. So <code>
&quot;#,##,###,####&quot;</code> == <code>&quot;######,####&quot;</code> == <code>&quot;##,####,####&quot;</code>.
</p>
<h4>Special Pattern Characters</h4>
<p>Many characters in a pattern are taken literally; they are matched during 
parsing and output unchanged during formatting. Special characters, on the other 
hand, stand for other characters, strings, or classes of characters. They must 
be quoted, unless noted otherwise, if they are to appear in the prefix or suffix 
as literals. </p>
<p>The characters listed here are used in non-localized patterns. Localized 
patterns use the corresponding characters taken from this formatter's <code>
DecimalFormatSymbols</code> object instead, and these characters lose their 
special status. Two exceptions are the currency sign and quote, which are not 
localized. </p>
<blockquote>
  <table cellSpacing="3" cellPadding="0" summary="Chart showing symbol,
  location, localized, and meaning." border="0">
    <tr bgColor="#ccccff">
      <th align="left">Symbol </th>
      <th align="left">Location </th>
      <th align="left">Localized? </th>
      <th align="left">Meaning </th>
    </tr>
    <tr vAlign="top">
      <td><code>0</code> </td>
      <td>Number </td>
      <td>Yes </td>
      <td>Digit </td>
    </tr>
    <tr vAlign="top" bgColor="#eeeeff">
      <td><code>#</code> </td>
      <td>Number </td>
      <td>Yes </td>
      <td>Digit, zero shows as absent </td>
    </tr>
    <tr vAlign="top">
      <td><code>.</code> </td>
      <td>Number </td>
      <td>Yes </td>
      <td>Decimal separator or monetary decimal separator </td>
    </tr>
    <tr vAlign="top" bgColor="#eeeeff">
      <td><code>-</code> </td>
      <td>Number </td>
      <td>Yes </td>
      <td>Minus sign </td>
    </tr>
    <tr vAlign="top">
      <td><code>,</code> </td>
      <td>Number </td>
      <td>Yes </td>
      <td>Grouping separator </td>
    </tr>
    <tr vAlign="top" bgColor="#eeeeff">
      <td><code>E</code> </td>
      <td>Number </td>
      <td>Yes </td>
      <td>Separates mantissa and exponent in scientific notation. <em>Need not 
      be quoted in prefix or suffix.</em> </td>
    </tr>
    <tr vAlign="top">
      <td><code>;</code> </td>
      <td>Subpattern boundary </td>
      <td>Yes </td>
      <td>Separates positive and negative subpatterns </td>
    </tr>
    <tr vAlign="top" bgColor="#eeeeff">
      <td><code>%</code> </td>
      <td>Prefix or suffix </td>
      <td>Yes </td>
      <td>Multiply by 100 and show as percentage </td>
    </tr>
    <tr vAlign="top">
      <td><code>\u2030</code> </td>
      <td>Prefix or suffix </td>
      <td>Yes </td>
      <td>Multiply by 1000 and show as per mille </td>
    </tr>
    <tr vAlign="top" bgColor="#eeeeff">
      <td><code>¤</code> (<code>\u00A4</code>) </td>
      <td>Prefix or suffix </td>
      <td>No </td>
      <td>Currency sign, replaced by currency symbol. If doubled, replaced by 
      international currency symbol. If present in a pattern, the monetary 
      decimal separator is used instead of the decimal separator. </td>
    </tr>
    <tr vAlign="top">
      <td><code>'</code> </td>
      <td>Prefix or suffix </td>
      <td>No </td>
      <td>Used to quote special characters in a prefix or suffix, for example,
      <code>&quot;'#'#&quot;</code> formats 123 to <code>&quot;#123&quot;</code>. To create a single 
      quote itself, use two in a row: <code>&quot;# o''clock&quot;</code>. </td>
    </tr>
  </table>
</blockquote>
<h4>Scientific Notation</h4>
<p>Numbers in scientific notation are expressed as the product of a mantissa and 
a power of ten, for example, 1234 can be expressed as 1.234 x 10^3. The mantissa 
is often in the range 1.0 &lt;= x &lt; 10.0, but it need not be. <code>DecimalFormat</code> 
can be instructed to format and parse scientific notation <em>only via a pattern</em>; 
there is currently no factory method that creates a scientific notation format. 
In a pattern, the exponent character immediately followed by one or more digit 
characters indicates scientific notation. Example: <code>&quot;0.###E0&quot;</code> 
formats the number 1234 as <code>&quot;1.234E3&quot;</code>. </p>
<ul>
  <li>The number of digit characters after the exponent character gives the 
  minimum exponent digit count. There is no maximum. Negative exponents are 
  formatted using the localized minus sign, <em>not</em> the prefix and suffix 
  from the pattern. This allows patterns such as <code>&quot;0.###E0 m/s&quot;</code>.
  </li>
  <li>The minimum and maximum number of integer digits are interpreted together:
  <ul>
    <li>If the maximum number of integer digits is greater than their minimum 
    number and greater than 1, it forces the exponent to be a multiple of the 
    maximum number of integer digits, and the minimum number of integer digits 
    to be interpreted as 1. The most common use of this is to generate <em>
    engineering notation</em>, in which the exponent is a multiple of three, 
    e.g., <code>&quot;##0.#####E0&quot;</code>. Using this pattern, the number 12345 
    formats to <code>&quot;12.345E3&quot;</code>, and 123456 formats to <code>&quot;123.456E3&quot;</code>.
    </li>
    <li>Otherwise, the minimum number of integer digits is achieved by adjusting 
    the exponent. Example: 0.00123 formatted with <code>&quot;00.###E0&quot;</code> yields
    <code>&quot;12.3E-4&quot;</code>. </li>
  </ul>
  </li>
  <li>The number of significant digits in the mantissa is the sum of the <em>
  minimum integer</em> and <em>maximum fraction</em> digits, and is unaffected 
  by the maximum integer digits. For example, 12345 formatted with <code>
  &quot;##0.##E0&quot;</code> is <code>&quot;12.3E3&quot;</code>. To show all digits, set the 
  significant digits count to zero. The number of significant digits does not 
  affect parsing. </li>
  <li>Exponential patterns may not contain grouping separators. </li>
</ul>
<h4>Rounding</h4>
<p><code>DecimalFormat</code> uses half-even rounding (see
<a href="../java/math/BigDecimal.html#ROUND_HALF_EVEN"><code>ROUND_HALF_EVEN</code></a>) 
for formatting. </p>
<h4>Digits</h4>
<p>For formatting, <code>DecimalFormat</code> uses the ten consecutive 
characters starting with the localized zero digit defined in the <code>
DecimalFormatSymbols</code> object as digits. For parsing, these digits as well 
as all Unicode decimal digits, as defined by
<a href="../java/lang/Character.html#digit(char, int)"><code>Character.digit</code></a>, 
are recognized. </p>
<h4>Special Values</h4>
<p><code>NaN</code> is formatted as a single character, typically <code>\uFFFD</code>. 
This character is determined by the <code>DecimalFormatSymbols</code> object. 
This is the only value for which the prefixes and suffixes are not used. </p>
<p>Infinity is formatted as a single character, typically <code>\u221E</code>, 
with the positive or negative prefixes and suffixes applied. The infinity 
character is determined by the <code>DecimalFormatSymbols</code> object. </p>
<p>Negative zero (<code>&quot;-0&quot;</code>) parses to <code>Double(-0.0)</code>, unless
<code>isParseIntegerOnly()</code> is true, in which case it parses to <code>
Long(0)</code>. </p>
<h4><a name="synchronization">Synchronization</a></h4>
<p>Decimal formats are generally not synchronized. It is recommended to create 
separate format instances for each thread. If multiple threads access a format 
concurrently, it must be synchronized externally. </p>
<h4>Example</h4>
<blockquote>
  <pre> <strong>// Print out a number using the localized number, integer, currency,
 // and percent format for each locale</strong>
 Locale[] locales = NumberFormat.getAvailableLocales();
 double myNumber = -1234.56;
 NumberFormat form;
 for (int j=0; j&lt;4; ++j) {
     System.out.println(&quot;FORMAT&quot;);
     for (int i = 0; i &lt; locales.length; ++i) {
         if (locales[i].getCountry().length() == 0) {
            continue; // Skip language-only locales
         }
         System.out.print(locales[i].getDisplayName());
         switch (j) {
         case 0:
             form = NumberFormat.getInstance(locales[i]); break;
         case 1:
             form = NumberFormat.getIntegerInstance(locales[i]); break;
         case 2:
             form = NumberFormat.getCurrencyInstance(locales[i]); break;
         default:
             form = NumberFormat.getPercentInstance(locales[i]); break;
         }
         if (form instanceof DecimalFormat) {
             System.out.print(&quot;: &quot; + ((DecimalFormat) form).toPattern());
         }
         System.out.print(&quot; -&gt; &quot; + form.format(myNumber));
         try {
             System.out.println(&quot; -&gt; &quot; + form.parse(form.format(myNumber)));
         } catch (ParseException e) {}
     }
 }
</pre>
</blockquote>

</body>

</html>
