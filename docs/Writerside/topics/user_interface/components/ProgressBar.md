# ProgressBar

The `ProgressBar` can visually display a process from 0 to 100 percent.

<code-block lang="java">
ProgressBarComponent legacyProgressBar = new ProgressBarComponent();
legacyProgressBar.progress(.7F);
legacyProgressBar.style(new LegacyProgressBarStyle());
legacyProgressBar.width(30);

ProgressBarComponent shadeProgressBar = new ProgressBarComponent();
shadeProgressBar.progress(.5F);
shadeProgressBar.style(new ShadeProgressBarStyle());
shadeProgressBar.width(30);
</code-block>
<img src="progressbar.png" alt="progressbar example"/>

## Methods

| Method                                      | Description                 |
|---------------------------------------------|-----------------------------|
| <code>void progress(float)</code>           | Sets the status value (0-1) |
| <code>float progress()</code>               | Return current status value |
| <code>void style(ProgressBarStyle)</code>   | Sets the style              |
| <code>ProgressBarStyle style()</code>       | Returns the style           |