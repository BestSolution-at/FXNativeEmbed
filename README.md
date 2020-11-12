# FXNativeEmbed
In contrast to JFXPanel FXEmbed uses native window reparenting to
embed JavaFX applications into Swing.

This has adavantages:
* should perform better on HiDPI-Screens as there's no buffer copying is going on
* allows to run JavaFX into another process

and disadvantages:
* You can only occupy rectangular areas
* You can not apply transformations

Currently only Windows 32/64 bit is supported.

# Usage

```java
JPanel p = new JPanel();
p.setLayout(new BorderLayout());

// Embed in same process
FXEmbed em = FXEmbed.create( (Scene s) -> {
  // setup your JavaFX application
});
p.add(em,BorderLayout.CENTER);

// Embed from other process
long handle = ...:
FXEmbed em = FXEmbed.createWithHandle(handle);
```
