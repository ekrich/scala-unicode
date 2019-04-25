# scala-unicode
This project contains scripts to generate data tables used to support
[Unicode](https://unicode.org/) for the **Scala Native** and **Scala.js**
platforms.

The tables or sequences generated are used to support `java.lang.Character`
and also to support `re2s` in Scala Native which is used to support
regular expressions (regex) including `java.util.regex._`.

Scala Native currently tracks JDK8 which uses Unicode 6.2.0. The starting
point for this project is Unicode 7.0.0 which was somewhat arbitrarily
used for the current `uppercase/lowercase` implementation. JDK11 which is
the next production release of Java tracks Unicode 10.0.0. The
disadvantage to the newer standards is that they contain more code
points which translates to more data and larger binary sizes. This runs
in a detrimental direction to goal **#4** below.

The overall goals of this project are as follows:

1. Codify existing algothims used to generate tables used in **Scala Native**
and **Scala.js**.
2. Provide access to the Unicode database reusing code used in transforming
the data as needed.
3. Allow a relatively easy path to upgrade the Unicode data and regenerate
the new data needed.
4. Assess whether the data needed for `regex` and `Character` can be shared
to reduce the code size of **Scala Native** applications.

The documentation and databases can be found on the [Unicode Server](https://www.unicode.org/Public/). Data and docs are added to this project for easy
access. The written documentation can be found in the project `docs`
directory for the time being but could be linked to from this README as
desired.

The Unicode Consortium also provides *Locale* data which could be used in
a similar manner if needed in the future.


