package net.jterminal.test.example.scenario;

import java.util.Timer;
import java.util.TimerTask;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.progressbar.LegacyProgressBarStyle;
import net.jterminal.ui.component.progressbar.ProgressBarComponent;
import net.jterminal.ui.component.progressbar.ShadeProgressBarStyle;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;

public class ProgressBarScenarioContainer extends FrameContainer {

  private final Timer timer = new Timer();

  private final ProgressBarComponent progressBar1 = new ProgressBarComponent(0.3F);
  private final ProgressBarComponent progressBar2 = new ProgressBarComponent(0);
  private final ProgressBarComponent progressBar3 = new ProgressBarComponent(0.6F);

  public void create() {
    progressBar1.style(new ShadeProgressBarStyle());
    progressBar2.style(new LegacyProgressBarStyle());
    progressBar3.style(new ShadeProgressBarStyle());

    progressBar1.width(Layout.fill(0.6F));
    progressBar1.x(4);
    progressBar1.y(3);

    progressBar2.width(Layout.relativeTo(progressBar1, Anchor.RIGHT));
    progressBar2.x(Layout.relative(progressBar1, Anchor.LEFT));
    progressBar2.y(Layout.dock(progressBar1, Anchor.BOTTOM), Layout.offset(2));

    progressBar3.width(Layout.relativeTo(progressBar1, Anchor.RIGHT));
    progressBar3.x(Layout.relative(progressBar2, Anchor.LEFT));
    progressBar3.y(Layout.dock(progressBar2, Anchor.BOTTOM), Layout.offset(2));

    add(progressBar1);
    add(progressBar2);
    add(progressBar3);

    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        timerEvent();
      }
    };
    timer.schedule(task, 100L, 100L);
  }

  private void timerEvent() {
    float progress = progressBar1.progress();
    progress += 0.01F;
    progressBar1.progress(progress % 1.01F);

    progress = progressBar2.progress();
    progress += 0.02F;
    progressBar2.progress(progress % 1.01F);

    progress = progressBar3.progress();
    progress += 0.06F;
    progressBar3.progress(progress % 1.01F);
  }

}
